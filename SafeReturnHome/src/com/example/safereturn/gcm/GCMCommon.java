package com.example.safereturn.gcm;

import android.content.Context;
import android.telephony.TelephonyManager;
import com.example.safereturn.util.*;
import com.example.safereturn.util.Common;
import com.google.android.gcm.GCMRegistrar;

public class GCMCommon {
	
	public static final String SERVER_URL = "http://54.250.171.142";
    public static final String SENDER_ID = "563625196391";
    public static final String API_KEY = "AIzaSyATuU0glOHHavZAU9VAzIEy5EB-ho2f4FQ";
    public static final String LOCATION_PUSH_URL = "/location/push";
	public static final String LOCATION_PULL_URL = "/location/pull";
	public static final String SEND_MESSAGE_URL  = "/message/create";
	public static final String JOIN_URL 	   	 = "/register";
    
	private static String regId;

    //for test
	//public String REG_ID = "APA91bF77zEj0j4SzMYGiiItZA0atAn4z9JfvUJvdZCCCXCwU6CUjWKlDlJoVOrQqEYn_ho1T1ZqpKVL23wRVNv_vSN6wa6ENdkGEHMLVjZdECKLD7p3ilAm3Cvx0PPgM15eKGt5BkC2J3NfxU7-uCQcc6ly3OdIzg";
	
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
        // Once GCM returns a registration id, we need to register it in the server. 
        try {
        		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        		String phoneNumber = tm.getLine1Number();
        	
        		JSONCreator jsonCreator = new JSONCreator();
        		jsonCreator.add("regId", regId);
        		jsonCreator.add("phone_number", phoneNumber);
        		String jsonString = jsonCreator.getJSONString();
        		
        		
                Common.sendHttp(JOIN_URL, jsonString);
                GCMRegistrar.setRegisteredOnServer(context, true);
                return true;
            } catch (Exception e) {
            	e.printStackTrace();
            }
        return false;
    }

    /**
     * Unregister this account/device pair within the server.
     */
    public static void unregister(final Context context, final String regId) {
        try {
        	//post("/message/create");
            GCMRegistrar.setRegisteredOnServer(context, false);
        } catch (Exception e) {
        	e.printStackTrace();
        	//handle error (todo)
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
        }
    }
}