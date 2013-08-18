package com.example.safereturn.util;


import java.io.File;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safereturn.R;
import com.example.safereturn.chat.ChatRoom;

public class Common {
	
	Context context;
	
	public Common(Context context) {
		this.context = context; 
	}
	
	public void showMsg(String message) {
		if(ChatRoom.VISIBLE)
			return;
		
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Intent intent = new Intent(context, AlertDialog.class);
		intent.putExtra("msg", message);
		
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,intent, PendingIntent.FLAG_CANCEL_CURRENT);
		Notification notification = new Notification(R.drawable.ic_launcher,
				"새로운 알림이 도착했습니다.", System.currentTimeMillis());
		notification.setLatestEventInfo(context, "SafeReturn",
				message, pendingIntent);
		
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		int notiId = (int) System.currentTimeMillis();
		
		//vibrate
		Vibrator vibe = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		vibe.vibrate(500);
		
		//rington
		String uriPath = "android.resource://"+context.getPackageName()+File.separator+R.raw.sound;
		notification.sound = Uri.parse(uriPath);
		
		nm.notify(notiId, notification);
		
		
    	if (isScreenOn()) {
    		alertToast(message);
    	} else {
    		
    	}
    }
    
    private Vibrator getSystemService(String vibratorService) {
		// TODO Auto-generated method stub
		return null;
	}

	private void alertToast(String message) {
    	Toast toast;
		int nWidth, nHeight;
        Display display=((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        nWidth=(int)display.getWidth();
        nHeight=(int)display.getHeight()-50;
        if(nWidth>nHeight){
          nWidth=nHeight;
        }
		toast=Toast.makeText(context, message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, (nHeight/3));
		LinearLayout layout=(LinearLayout)toast.getView();
		layout.setOrientation(LinearLayout.HORIZONTAL);
		TextView tv=(TextView) layout.getChildAt(0);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setPadding(nWidth/30, 0, 0, 0);
		layout.removeAllViews();
		LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(nWidth/8,nWidth/8);

		LinearLayout.LayoutParams lp2=
			new LinearLayout.LayoutParams
			(LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.FILL_PARENT);

		lp.setMargins(0, nWidth/32, 0, nWidth/32);

		ImageView image=new ImageView(context);
		image.setImageResource(R.drawable.ic_launcher);

		layout.addView(image, lp);
		layout.addView(tv, lp2);
		toast.show();
	}

	private boolean isScreenOn(){
    	PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
    	return pm.isScreenOn();
    }
}
