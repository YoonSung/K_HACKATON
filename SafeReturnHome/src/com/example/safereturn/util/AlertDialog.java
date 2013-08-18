package com.example.safereturn.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.safereturn.R;
import com.example.safereturn.chat.ChatRoom;

public class AlertDialog extends Activity implements OnClickListener{

	Button btnOpen, btnClose;
	TextView alertMsg;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.alertdialog);
	    
	    btnOpen = (Button)findViewById(R.id.alertdialog_btnShow);
	    btnOpen.setOnClickListener(this);
	    
	    btnClose = (Button)findViewById(R.id.alertdialog_btnClose);
	    btnClose.setOnClickListener(this);
	    
	    alertMsg = (TextView)findViewById(R.id.alertdialog_alertTxt);
	    alertMsg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.alertdialog_btnShow:
				startActivity(new Intent(AlertDialog.this, ChatRoom.class));
				finish();
				break;
			case R.id.alertdialog_btnClose:
				finish();
				break;
		}
	}

}
