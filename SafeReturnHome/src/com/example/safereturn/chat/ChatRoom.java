package com.example.safereturn.chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.safereturn.R;

public class ChatRoom extends Activity implements OnClickListener{

	public static Handler mHandler = new Handler();
	
	private MessageAdapter msgAdapter;
	private ListView msgList;
	private SimpleDateFormat dateFormat;
	private SimpleDateFormat timeFormat;

	private Button btnMsgSend, btnSendTest, btnReceiveTest, btnDateTest;
	private EditText edtMsgBox;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.chatroom);
	    
	    //Initialize variables start
	    btnMsgSend = (Button)findViewById(R.id.chatroom_btnMsgSend);
	    btnMsgSend.setOnClickListener(this);
	    
	    btnSendTest= (Button)findViewById(R.id.chatroom_btnSendTest);
	    btnSendTest.setOnClickListener(this);
	    
	    btnReceiveTest = (Button)findViewById(R.id.chatroom_btnReceiveTest);
	    btnReceiveTest.setOnClickListener(this);
	    
	    btnDateTest= (Button)findViewById(R.id.chatroom_btnDate);
	    btnDateTest.setOnClickListener(this);
	    
	    edtMsgBox = (EditText)findViewById(R.id.chatroom_edtMsg);
	    msgList = (ListView)findViewById(R.id.chatroom_chatListView);
	    
	    
	    ArrayList<Message> msgArray = new ArrayList<Message>();
	    msgAdapter = new MessageAdapter(msgArray);
	    msgList.setAdapter(msgAdapter);
	    
	    dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
	    timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
	    //Initialize variables end
	    
	}

	@Override
	public void onClick(View v) {
		
		Message msgData = null;
		
		switch (v.getId()) {
		
		//type 0 is user send, type 1 is user receive, type 2 is date display 
		case R.id.chatroom_btnMsgSend:
			msgData = new Message(0, edtMsgBox.getText().toString(), timeFormat.format(new Date()));
			break;
		case R.id.chatroom_btnSendTest:
			msgData = new Message(0, edtMsgBox.getText().toString(), timeFormat.format(new Date()));
			break;
		case R.id.chatroom_btnReceiveTest:
			msgData = new Message(1, edtMsgBox.getText().toString(), timeFormat.format(new Date()));
			break;
		case R.id.chatroom_btnDate:
			msgData = new Message(2, dateFormat.format(new Date()));
			break;
		}
		
		//add instance to adapter
		msgAdapter.add(msgData);
		
		//scroll focus add area
		msgList.smoothScrollToPosition(msgAdapter.getCount()-1);
	}
	
	
	//inner class adapter 
	public class MessageAdapter extends BaseAdapter{

		private LayoutInflater inflater;
		private ArrayList<Message> msgList;
		
		public MessageAdapter(ArrayList<Message> msgList) {
			this.msgList = msgList; 
			inflater = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		}

		public void add(Message msgData) {
			msgList.add(msgData);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return msgList.size();
		}

		@Override
		public Message getItem(int position) {
			return msgList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			return msgList.get(position).getType();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			
			int type = getItemViewType(position);
			
			//convertview is current view instance. 
			if (convertView == null) {
				switch (type) {
					case 0:
						view = inflater.inflate(R.layout.chat_item0, null);
						break;
					case 1:
						view = inflater.inflate(R.layout.chat_item1, null);
						break;
					case 2:
						view = inflater.inflate(R.layout.chat_item2, null);
						break;
				}
			} else {
				view = convertView;
			}
			
			Message msgInstance = msgList.get(position);
			
			//date is not null
			if (msgInstance != null) {
				if (type == 0) {
					TextView txtMsgSend, txtSendTime;
					txtMsgSend = (TextView)view.findViewById(R.id.chatroom_txtSendMsg);
					txtSendTime = (TextView)view.findViewById(R.id.chatroom_txtSendTime);
					
					txtMsgSend.setText(msgInstance.getMessage());
					txtSendTime.setText(msgInstance.getTimeFormat());
				}
				
				if (type == 1) {
					TextView txtName, txtMsgReceive, txtReceiveTime;
					txtName = (TextView)view.findViewById(R.id.chatroom_txtName);
					txtMsgReceive = (TextView)view.findViewById(R.id.chatroom_txtReceiveMsg);
					txtReceiveTime = (TextView)view.findViewById(R.id.chatroom_txtReceiveTime);
					
					txtName.setText(msgInstance.getName());
					txtMsgReceive.setText(msgInstance.getMessage());
					txtReceiveTime.setText(msgInstance.getTimeFormat());
				}
				
				if (type == 2) {
					TextView txtDate = (TextView)view.findViewById(R.id.chatroom_txtDate);
					
					txtDate.setText(msgInstance.getDayFormat());
				}
			}
			
			return view;
		}
	}

}
