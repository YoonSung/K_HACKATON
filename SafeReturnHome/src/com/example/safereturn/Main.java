package com.example.safereturn;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.safereturn.chat.ChatRoom;
import com.example.safereturn.nmap.MapActivity;
import com.example.safereturn.util.DAO;
import com.example.safereturn.util.Person;


public class Main extends Activity implements OnClickListener{
	
	//private final String LOG_ERROR = "SafeReturn Main.java";
	
	Button btnPlus, btnSetting, btnChatting;
	ListView listView;
	DAO db;
	Cursor cursor;
	PersonAdapter personAdapter ;
	
	//row
	ImageView imgProfile, imgStatus;
	TextView txtName, txtCheckLast;
	
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
	    listView  = (ListView)findViewById(R.id.main_list);
	    
	    ArrayList<Person> personArray = new ArrayList<Person>();
	    personAdapter = new PersonAdapter( Main.this, R.layout.main_row,  personArray);
	    
	    db = new DAO(this);
	    db.open();
	    
	    cursor = db.selectAllUser();
	    
	    
	    
	    if ( cursor != null ) {
	    	boolean inData = cursor.moveToFirst();
	    	System.out.println(      "test     bool : "+inData);
	    	while (inData) {
	    		personAdapter.add( new Person( cursor.getString(0), cursor.getString(1) ) );
	    		System.out.println("      cur      1 : "+cursor.getString(0)+"   / 2 : "+cursor.getString(1));
	    		inData = cursor.moveToNext();
	    		System.out.println(      "test     bool : "+inData);
	    	}
	    }
	    System.out.println(personArray.size());
	    
	    listView.setAdapter(personAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnChat:
			startActivity(new Intent(Main.this, ChatRoom.class));
			break;
		case R.id.main_btnPlus:
			startActivity(new Intent(Main.this, AddGroup.class));
			break;
		case R.id.main_btnSetting:
			break;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		db.close();
	}
	
	class PersonAdapter extends BaseAdapter implements OnClickListener{

		private ArrayList<Person> instanceArray;
		private Person person;
		private Person currentPerson;
		private String dbID;
		private String number;
		private String name;
		private Context context;
		LayoutInflater inflater ;
		
		public PersonAdapter(Context context, int textViewResourceID, ArrayList<Person> items) {
			this.context = context;
			this.instanceArray = items;
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void add(Person person) {
			instanceArray.add(person);
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if(view == null) {
				
				view = inflater.inflate(R.layout.main_row, null);
			}
			
			person = instanceArray.get(position);
			System.out.println("called");
			if(person != null) {
				System.out.println("person is not null");
				LinearLayout row;
				
				//Initialize
				row = (LinearLayout) view.findViewById(R.id.Main_row_linearRow);
				imgProfile = (ImageView) view.findViewById(R.id.Main_row_imgProfile);
				imgStatus = (ImageView) view.findViewById(R.id.Main_row_imgStatus);
				txtName = (TextView) view. findViewById(R.id.Main_row_txtName);
				txtCheckLast = (TextView) view. findViewById(R.id.Main_row_txtstatus);
				String name = person.getName();

				//DB Check
				//Cursor personCursor = db.selectUser(person.getName());
				//
				//////////////////////////////////////////////////////// (todo) check safetytime, etc...
				// 세이프티타임을 확인해서 imgStatus 선택적으로 보여주기, txtCheckLast 선택적으로 노출
				// db영역에 사진에 대한 부분 추가되어야 한다.. db를 사용하는 이유가..과연 이거밖에 없는가? 이거밖에 없으면 여기서 db안써도 된다.
				// 모든 person 부분에 dbID값과 이미지저장경로를 지정하는 것이다.
				
				//Input Content
				txtName.setText(name);
				
				
				//for Click Event
				imgStatus.setTag(position);
				imgStatus.setOnClickListener(this);
				
				imgProfile.setTag(position);
				imgProfile.setOnClickListener(this);
				
				row.setTag(position);
				row.setOnClickListener(this);
				System.out.println("    test    ");
			}
			return view;
		}
		
		@Override
		public void onClick(View v) {
			int tagPosition = Integer.parseInt(v.getTag().toString());
			currentPerson = instanceArray.get(tagPosition);
			
			switch (v.getId()) {
			case R.id.Main_row_imgProfile:
				break;
			case R.id.Main_row_imgStatus:
				startActivity(new Intent(Main.this, MapActivity.class));
				break;
			case R.id.Main_row_linearRow:
				break;
			}
		}

		@Override
		public int getCount() {
			return instanceArray.size();
		}

		@Override
		public Object getItem(int position) {
			
			return instanceArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
	}
}
