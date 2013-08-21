package com.example.safereturn.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.safereturn.R;
import com.example.safereturn.chat.ChatRoom;
import com.example.safereturn.gcm.GCMCommon;

public class Common {
	
	Context context;
	SharedPreferences spf;
	SharedPreferences.Editor editor;
	
	public Common(Context context) {
		this.context = context; 
		spf=PreferenceManager.getDefaultSharedPreferences(context);
		editor=spf.edit();
	}
	
	public void savePreference(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}
	
	public void savePreference(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public String readStringPreference(String key) {
		return spf.getString(key, null);
	}
	
	
	public boolean isFirstLogin(String key) {
		return spf.getBoolean(key, true);
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

	private boolean isScreenOn() {
    	PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
    	return pm.isScreenOn();
    }
	
//	public static String sendHttpData() throws UnsupportedEncodingException {
//		try {
//			HttpClient client = new DefaultHttpClient();
//			String postURL = "http://app.dooit.co.kr/member/login_check";
//			HttpPost post = new HttpPost(postURL);
//			post.addHeader("User-Agent", "DooitLocalResearchApp" + version + ";AppId:"+pushId);
//			List<NameValuePair> params = new ArrayList<NameValuePair>();
//			params.add(new BasicNameValuePair("regId", phone));
//			params.add(new BasicNameValuePair("phone_id", phoneId));
//			params.add(new BasicNameValuePair("push_id", pushId));
//			params.add(new BasicNameValuePair("id", id));
//			params.add(new BasicNameValuePair("password", pw));
//			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
//					HTTP.UTF_8);
//			post.setEntity(ent);
//			HttpResponse responsePOST = client.execute(post);
//			HttpEntity resEntity = responsePOST.getEntity();
//			return EntityUtils.toString(resEntity);
//		} catch (Exception e) {
//			return "0";
//		}
//		}
//	}
	
	public static void sendHttp(String endpoint, String message) {
		URL url;
		
        try {
            url = new URL(GCMCommon.SERVER_URL+endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        
        String parameter ="regId=" + GCMCommon.getRegId()+"&"+"message="+ message;
        
        byte[] bytes = parameter.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");

            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();

            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
            	//handle error (todo)
            	throw new IOException("Post failed with error code " + status);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
	}
}
