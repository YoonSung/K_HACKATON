package com.example.safereturn;

import static com.example.safereturn.gcm.GCMCommon.SENDER_ID;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.safereturn.gcm.GCMCommon;
import com.google.android.gcm.GCMRegistrar;

public class Splash extends Activity {

	private final String LOG_ERROR = "SafeReturn Splash.java";
	AsyncTask<Void, Void, Void> mRegisterTask ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Be remove at publishing
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
		
		setContentView(R.layout.splash);

		final String regId = GCMRegistrar.getRegistrationId(this);
		
		Log.e(LOG_ERROR, "regId : "+regId);
		
        if (regId.equals("")) {
            // Automatically registers application on startup.
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            // Device is already registered on GCM, check server.
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
//                mRegisterTask = new AsyncTask<Void, Void, Void>() {
//
//                    @Override
//                    protected Void doInBackground(Void... params) {
//                        boolean registered =
//                                GCMCommon.register(context, regId);
//                        // At this point all attempts to register with the app
//                        // server failed, so we need to unregister the device
//                        // from GCM - the app will try to register again when
//                        // it is restarted. Note that GCM will send an
//                        // unregistered callback upon completion, but
//                        // GCMIntentService.onUnregistered() will ignore it.
//                        if (!registered) {
//                            GCMRegistrar.unregister(context);
//                        }
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(Void result) {
//                        mRegisterTask = null;
//                    }
//
//                };
//                mRegisterTask.execute(null, null, null);
            }
        }
        GCMCommon.setRegId(GCMRegistrar.getRegistrationId(this));
        Log.e(LOG_ERROR, "final regId :"+regId);
        
        
		WaitingHandler handler = new WaitingHandler(Splash.this);
		handler.sendEmptyMessageDelayed(0, 500);
	}
	
	
	static class WaitingHandler extends Handler {
		
		private final Splash activity;
		
		WaitingHandler (Splash splash) {
			activity = splash;
		}
		
		@Override
		public void handleMessage(Message msg) {
			activity.startActivity(new Intent(activity, Main.class));
			activity.finish();
		}
	}
	
	@Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        GCMRegistrar.onDestroy(this);
        super.onDestroy();
    }
}
