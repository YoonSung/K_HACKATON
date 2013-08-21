package com.example.safereturn.gcm;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.google.android.gcm.GCMRegistrar;

public class GCMCommon {
	
	public static final String SERVER_URL = "http://54.250.171.142";
    public static final String SENDER_ID = "563625196391";
    public static final String API_KEY = "AIzaSyATuU0glOHHavZAU9VAzIEy5EB-ho2f4FQ";
    private static String regId;

    public static String getRegId() {
		return regId;
	}

    public static void setRegId(String regId) {
		GCMCommon.regId = regId;
	}
    
    /**
     * Register this account/device pair within the server.
     */
    public static boolean register(final Context context, final String regId) {
        //String serverUrl = SERVER_URL + "/register";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);

        // Once GCM returns a registration id, we need to register it in the server. 
        try {
                post("/message/create");
                GCMRegistrar.setRegisteredOnServer(context, true);
                return true;
            } catch (IOException e) {
            	e.printStackTrace();
            }
        return true;
        //return false;
    }

    /**
     * Unregister this account/device pair within the server.
     */
    public static void unregister(final Context context, final String regId) {
        //String serverUrl = SERVER_URL + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        try {
        	post("/message/create");
            GCMRegistrar.setRegisteredOnServer(context, false);
        } catch (IOException e) {
        	//handle error (todo)
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
        }
    }
    
    
    private static void post(String endpoint)
            throws IOException {
        URL url;
        String message = "clinet test";
        try {
            url = new URL(GCMCommon.SERVER_URL+endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        
        String json ="[{ \"regId\":\"" + GCMCommon.regId+"\","
        				+"\"message\":\""+ message + "\"}];";
        		
        
        
        byte[] bytes = json.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
//            out.write(bytes);
//            out.close();
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes("hello dongkuk");
            
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
            	//handle error (todo)
            	throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }
}