package com.example.safereturn;

import java.io.InputStream;
import java.util.ArrayList;

import com.example.safereturn.util.DAO;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class AddGroup extends ListActivity {
	ListView listView;
	SimpleCursorAdapter simplemCursorAdapter;	
	ImageView imageView;
	Button btn;
	ContentResolver contentResolver;
	Cursor cursor;
	LinearLayout row;
	DAO db;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.addgroup);

	    contentResolver = getContentResolver();
		db = new DAO(this);
		db.open();
		
	    cursor =  getURI();
	    ArrayList<Person> m_orders = new ArrayList<Person>();
	    
	    if(cursor!=null){
	    	cursor.moveToFirst();
	    	while(cursor.moveToNext()){
	    		m_orders.add(new Person(cursor.getString(0), cursor.getString(1)));
	    	}
	    }
	    
	    PersonAdapter m_adapter = new PersonAdapter(AddGroup.this, R.layout.addgroup_row, m_orders);
	    setListAdapter(m_adapter);
	}    	

	
	private Cursor getURI() 
	{
		Uri people = Contacts.CONTENT_URI;
		String[] projection = new String[] { Contacts._ID, Contacts.DISPLAY_NAME };
		String[] selectionArgs = null;
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";	
		return managedQuery(people, projection, null, selectionArgs, sortOrder);
	}

	
	@Override
	protected void onStop() {
		super.onStop();
		db.close();
	}

	private class PersonAdapter extends ArrayAdapter<Person> implements OnClickListener {
		
		private ArrayList<Person> instanceArray;
		private Person person;
		private Person currentPerson;
		private String dbId;
		private String number;
		private String name;
		
		public PersonAdapter(Context context, int textViewResourceId, ArrayList<Person> items){
			super(context, textViewResourceId, items);
			this.instanceArray = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.addgroup_row, null);
			}
			
			person = instanceArray.get(position);
			if (person !=null) {
				TextView tname;
				LinearLayout row;
				
				row = (LinearLayout) view. findViewById(R.id.row);
				tname = (TextView) view.findViewById(R.id.AddGroup_listName);
				imageView = (ImageView) view. findViewById(R.id.AddGroup_listImg);
				
				row.setTag(position);
				row.setOnClickListener(this);
				
				tname.setText(person.getName());
				
				if (person.getId() == null) {
					imageView.setImageDrawable(getResources().getDrawable(R.drawable.contacts));
					return view;
				}
				
				Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(person.getId()));
				InputStream in = ContactsContract.Contacts.openContactPhotoInputStream(getContext().getContentResolver(), uri);
				
				if(in==null){
					imageView.setImageDrawable(getResources().getDrawable(R.drawable.contacts));
				}else{
					Bitmap bm = BitmapFactory.decodeStream(in);
					imageView.setImageBitmap(bm);
				}
				
			}	
			
			return view;
		}

		@Override
		public void onClick(View v) {
				int position = Integer.parseInt((v.getTag().toString()));
				row = (LinearLayout)findViewById(R.id.row);
				currentPerson = instanceArray.get(position);
				
				
				dbId = currentPerson.getId();
				name = currentPerson.getName();
				
				Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "='" + dbId + "'", null, null);
				phoneCursor.moveToFirst();
				
				number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				
				String message = "이름 : "+ name + "\n" + "전화번호 : "+number; 
				
				new AlertDialog.Builder(AddGroup.this)
				.setTitle("그룹에 추가하기")
				.setMessage(message)
				.setPositiveButton("추가", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						new AlertDialog.Builder(AddGroup.this)
						.setTitle(name+"님과 위치정보를 공유하시겠습니까?")
						.setPositiveButton("YES", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface doalog, int which) {
								db.insertUser(currentPerson.getName(), number, true);
								startActivity(new Intent(AddGroup.this, Main.class));
								finish();		
							}
						})
						.setNegativeButton("NO", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								db.insertUser(currentPerson.getName(), number, false);
								startActivity(new Intent(AddGroup.this, Main.class));
								finish();		
							}
						}).create().show();
					}
				})
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).create().show();
		}
	}
}


	class Person {

		private String id;
		private String name;

		public Person(String id, String name){
			this.id = id;
			this.name = name;
		}

		public String getId() {
			return id;
		}
		
		public String getName(){
			return name;
		}
}