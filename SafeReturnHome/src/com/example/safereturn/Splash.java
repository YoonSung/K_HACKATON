package com.example.safereturn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Splash extends Activity {

	public final String LOG_ERROR = "SafeReturn Splash.java";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					Log.e(LOG_ERROR, LOG_ERROR);
					e.printStackTrace();
				}
				startActivity(new Intent(Splash.this, Main.class));
				finish();
			}
		});
	}
}
