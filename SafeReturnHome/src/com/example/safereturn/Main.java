package com.example.safereturn;

import com.example.safereturn.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Main extends Activity implements OnClickListener{
	
	//private final String LOG_ERROR = "SafeReturn Main.java";
	
	
	Button btnPlus, btnSetting, btnChatting;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    btnPlus = (Button)findViewById(R.id.main_btnPlus);
	    btnPlus.setOnClickListener(this);
	    btnSetting = (Button)findViewById(R.id.main_btnSetting);
	    btnSetting.setOnClickListener(this);
	    btnChatting = (Button)findViewById(R.id.btnChat);
	    btnChatting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnChat:
			startActivity(new Intent(Main.this, ChatRoom.class));
			break;
		case R.id.main_btnPlus:
			break;
		case R.id.main_btnSetting:
			break;
		}
	}

}
