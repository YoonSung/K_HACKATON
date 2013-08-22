package com.example.safereturn;

import java.io.InputStream;
import java.util.ArrayList;

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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.example.safereturn.util.*;

public class AddGroup extends ListActivity {
	//ListView listView;
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

		cursor = getURI();
		ArrayList<Person> personArray = new ArrayList<Person>();

		if (cursor != null) {
			cursor.moveToFirst();
			while (cursor.moveToNext()) {
				personArray.add(new Person(cursor.getString(0), cursor
						.getString(1)));
			}
		}

		PersonAdapter personAdapter = new PersonAdapter( AddGroup.this, R.layout.addgroup_row, personArray );
		setListAdapter(personAdapter);
	}

	private Cursor getURI() {
		Uri people = Contacts.CONTENT_URI;
		String[] projection = new String[] { Contacts._ID,
				Contacts.DISPLAY_NAME };
		String[] selectionArgs = null;
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";
		return managedQuery(people, projection, null, selectionArgs, sortOrder);
	}

	class PersonAdapter extends ArrayAdapter<Person> implements OnClickListener {

		private ArrayList<Person> instanceArray;
		private Person person;
		private Person currentPerson;
		private String dbID;
		private String number;
		private String name;
		private Context context;

		public PersonAdapter(Context context, int textViewResourceId,
				ArrayList<Person> items) {
			super(context, textViewResourceId, items);
			this.context = context;
			this.instanceArray = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.addgroup_row, null);
			}

			person = instanceArray.get(position);
			if (person != null) {
				TextView txtName;
				LinearLayout row;

				row = (LinearLayout) view.findViewById(R.id.row);
				txtName = (TextView) view.findViewById(R.id.AddGroup_listName);
				imageView = (ImageView) view
						.findViewById(R.id.AddGroup_listImg);

				row.setTag(position);
				row.setOnClickListener(this);

				txtName.setText(person.getName());

				if (person.getId() == null)
					return view;

				Uri uri = ContentUris.withAppendedId(
						ContactsContract.Contacts.CONTENT_URI,
						Long.parseLong(person.getId()));
				InputStream in = ContactsContract.Contacts
						.openContactPhotoInputStream(getContext()
								.getContentResolver(), uri);

				if (in == null) {
					return view;
				} else {
					Bitmap bm = BitmapFactory.decodeStream(in);
					imageView.setImageBitmap(bm);
				}

			}

			return view;
		}

		@Override
		public void onClick(View v) {
			int tagPosition = Integer.parseInt(v.getTag().toString());
			row = (LinearLayout) v.findViewById(R.id.row);
			currentPerson = instanceArray.get(tagPosition);

			dbID = currentPerson.getId();
			name = currentPerson.getName();

			Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, 
													   ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "='"+ dbID + "'", null, null);
			phoneCursor.moveToFirst();

			number = phoneCursor
					.getString(phoneCursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			phoneCursor.close();
			
			String message = "이름 : " + name + "\n" + "전화번호 : " + number;

			new AlertDialog.Builder(AddGroup.this)
					.setTitle("그룹에 추가하기")
					.setMessage(message)
					.setPositiveButton("추가",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									new AlertDialog.Builder(AddGroup.this)
											.setTitle(name + "님과 위치정보를 공유하시겠습니까?")
											.setPositiveButton("YES", new DialogInterface.OnClickListener() {
														@Override
														public void onClick( DialogInterface doalog, int which) {
															db.insertUser( currentPerson.getName(),number, true);
															startActivity( new Intent(AddGroup.this, Main.class ) );
															finish();
														}
													})
											.setNegativeButton( "NO", new DialogInterface.OnClickListener() {
														@Override
														public void onClick( DialogInterface dialog, int which ) {
															db.insertUser( currentPerson.getName(), number, false );
															startActivity( new Intent( AddGroup.this, Main.class ) );
															db.close();
															finish();
														}}).create().show();
								}
							})
					.setNegativeButton("취소",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									return;
								}
							}).create().show();
		}
	}

}