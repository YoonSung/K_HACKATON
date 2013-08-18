package com.example.safereturn.gcm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.safereturn.chat.ChatRoom;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService{

	private final String LOG_ERROR = "SafeReturn GCMIntentService.java"; 
	
	// push를 받을 때 에러가 날 경우 호출되는 콜백 메서드이며 에러내용을 스트링으로 전달해 준다. 
	@Override
	protected void onError(Context context, String error) {
		Log.e(LOG_ERROR, "onError : "+error);
	}

	// push를 받을 경우 push 메세지에 데이터를 intent값으로 받으며 이 intent값을 분석하여 특정 동작을 수행할 수 있다.
	@Override
	protected void onMessage(Context context, Intent message) {
		if(message == null)
			return;
		final String gcmMsg = "TITLE : "+message.getStringExtra("title")+"\n"+"MSG : "
										+message.getStringExtra("msg");
		
		ChatRoom.mHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), gcmMsg, Toast.LENGTH_LONG).show();
			}
		});
	}

	// gcm을 등록할때 콜백되며 인자값으로 등록된 id값을 받는다.
	@Override
	protected void onRegistered(Context context, String regId) {
		Log.e("LOG_ERROR", "onRegustered : "+regId);		
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		Log.e("LOG_ERROR", "onUnregistered : "+regId);		
	}

}
