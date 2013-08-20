package com.example.safereturn.chat;

import java.io.IOException;
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
import com.example.safereturn.gcm.GCMCommon;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class ChatRoom extends Activity implements OnClickListener{

	public static Handler mHandler = new Handler();
	public static boolean VISIBLE = false;
	
	@Override
	protected void onResume() {
		super.onResume();
		ChatRoom.VISIBLE = true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		ChatRoom.VISIBLE = false;
	}
	
	
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
	    //btnMsgSend.setOnClickListener(this);
	    
	    //test
	    btnMsgSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//TEST CODE START
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						Sender sender = new Sender(GCMCommon.API_KEY);
						Message message = new Message.Builder().addData("title", "")
															   .addData("msg", edtMsgBox.getText().toString()).build();
						
						try {
							String regId = GCMCommon.getRegId();//GCMRegistrar.getRegistrationId(ChatRoom.this);
							System.out.println("   chatroom   "+regId);
							Result result = sender.send(message, regId, 5);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
				
				//TEST CODE END
			}
		});
	    
	    
	    
	    
	    
	    btnSendTest= (Button)findViewById(R.id.chatroom_btnSendTest);
	    btnSendTest.setOnClickListener(this);
	    
	    btnReceiveTest = (Button)findViewById(R.id.chatroom_btnReceiveTest);
	    btnReceiveTest.setOnClickListener(this);
	    
	    btnDateTest= (Button)findViewById(R.id.chatroom_btnDate);
	    btnDateTest.setOnClickListener(this);
	    
	    edtMsgBox = (EditText)findViewById(R.id.chatroom_edtMsg);
	    msgList = (ListView)findViewById(R.id.chatroom_chatListView);
	    msgList.setDivider(null);
	    
	    
	    ArrayList<ChatMessage> msgArray = new ArrayList<ChatMessage>();
	    msgAdapter = new MessageAdapter(msgArray);
	    msgList.setAdapter(msgAdapter);
	    
	    dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
	    timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
	    //Initialize variables end
	    
	}

	@Override
	public void onClick(View v) {
		
		ChatMessage msgData = null;
		
		switch (v.getId()) {
		
		//type 0 is user send, type 1 is user receive, type 2 is date display 
		case R.id.chatroom_btnMsgSend:
			msgData = new ChatMessage(0, edtMsgBox.getText().toString(), timeFormat.format(new Date()));
			break;
		case R.id.chatroom_btnSendTest:
			msgData = new ChatMessage(0, edtMsgBox.getText().toString(), timeFormat.format(new Date()));
			break;
		case R.id.chatroom_btnReceiveTest:
			msgData = new ChatMessage(1, edtMsgBox.getText().toString(), timeFormat.format(new Date()));
			break;
		case R.id.chatroom_btnDate:
			msgData = new ChatMessage(2, dateFormat.format(new Date()));
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
		private ArrayList<ChatMessage> msgList;
		
		public MessageAdapter(ArrayList<ChatMessage> msgList) {
			this.msgList = msgList; 
			inflater = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		}

		public void add(ChatMessage msgData) {
			msgList.add(msgData);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return msgList.size();
		}

		@Override
		public ChatMessage getItem(int position) {
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
			
			ChatMessage msgInstance = msgList.get(position);
			
			//date is not null
			if (msgInstance != null) {
				if (type == 0) {
					
					String message = msgInstance.getMessage(); 
					String time = msgInstance.getTimeFormat();
					
					TextView txtMsgSend, txtSendTime;
					txtMsgSend = (TextView)view.findViewById(R.id.chatroom_txtSendMsg);
					txtSendTime = (TextView)view.findViewById(R.id.chatroom_txtSendTime);
					
					if(message != null)
					txtMsgSend.setText(message);
					txtSendTime.setText(time);
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
