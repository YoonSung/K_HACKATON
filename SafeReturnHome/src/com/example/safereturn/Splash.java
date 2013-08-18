package com.example.safereturn;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class Splash extends Activity {

	public final String LOG_ERROR = "SafeReturn Splash.java";
//	Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			startActivity(new Intent(Splash.this, Main.class));
//			finish();
//		};
//	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
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
}
