package com.example.safereturn.chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.example.safereturn.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ChatRoom extends Activity implements OnClickListener{

	
	private MessageAdapter msgAdapter = null;
	private ListView msgList = null;
	private String userName = "정윤성";
	private SimpleDateFormat dateFormat = null;
	private SimpleDateFormat timeFormat = null;

	private Button btnMsgSend, btnSendTest, btnReceiveTest, btnDateTest;
	private EditText edtMsgBox;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    //Initialize variables start
	    btnMsgSend = (Button)findViewById(R.id.btnMsgSend);
	    btnMsgSend.setOnClickListener(this);
	    
	    btnSendTest= (Button)findViewById(R.id.btnSendTest);
	    btnSendTest.setOnClickListener(this);
	    
	    btnReceiveTest = (Button)findViewById(R.id.btnReceiveTest);
	    btnReceiveTest.setOnClickListener(this);
	    
	    btnDateTest= (Button)findViewById(R.id.btnDate);
	    btnDateTest.setOnClickListener(this);
	    
	    edtMsgBox = (EditText)findViewById(R.id.edtMsg);
	    
	    ArrayList<Message> msgList = new ArrayList<Message>();
	    msgAdapter = new MessageAdapter(msgList);
	    
	    dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
	    timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
	    //Initialize variables end
	    
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnMsgSend:
			break;
		case R.id.btnSendTest:
			break;
		case R.id.btnReceiveTest:
			break;
		case R.id.btnDate:
			break;
		}
	}
}
