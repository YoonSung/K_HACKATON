package com.example.safereturn;

import com.example.safereturn.chat.ChatRoom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Main extends Activity implements OnClickListener{

	ImageButton btnPlus, btnSetting, btnChatting;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    btnPlus = (ImageButton)findViewById(R.id.btnPlus);
	    btnPlus.setOnClickListener(this);
	    btnSetting = (ImageButton)findViewById(R.id.btnSetting);
	    btnSetting.setOnClickListener(this);
	    btnChatting = (ImageButton)findViewById(R.id.btnChat);
	    btnChatting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnChat:
			startActivity(new Intent(Main.this, ChatRoom.class));
			break;
		case R.id.btnPlus:
			break;
		case R.id.btnSetting:
			break;
		}
	}

}