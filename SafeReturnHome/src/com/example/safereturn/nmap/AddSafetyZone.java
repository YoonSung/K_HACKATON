package com.example.safereturn.nmap;

import com.example.safereturn.R;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapActivity.OnDataProviderListener;
import com.nhn.android.maps.NMapLocationManager.OnLocationChangeListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class AddSafetyZone extends NMapActivity implements OnLocationChangeListener, OnDataProviderListener, Runnable {
	private static final String NAVER_MAP_KEY = "bea527c7818dd8bc1e5adb1f5432c7ff";
	
	private NMapView mMapView;										// NMap view
	private NMapController mMapController;							// NMap view controller
	private NMapLocationManager mMapLocationManager;				// NMap Location Manager
	private NMapOverlayManager mOverlayManager;						// NMap Overlay Manager
	private NMapViewerResourceProvider mMapViewerResourceProvider;	// NMap ResourceProvider
	
	private String[] mCurrentAddress;	// 0: 도 Name, 1: 시 Name, 2: 동 Name
	private Handler handler;
	private boolean flag = false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		////////////////////////////////////////////
		// Initialize NMap View
		
		// Initialize NaverMapView
		mMapView = new NMapView(this);
		mMapView.setApiKey(NAVER_MAP_KEY);
		
		// set NMap View
		setContentView(mMapView);
		
		// set NMap Variables
		mMapView.setClickable(true);
		
		////////////////////////////////////////////
		// Initialize NMap Controller
		mMapController = mMapView.getMapController();
		mMapController.setMapCenter(new NGeoPoint(126.978371, 37.5666091), 11);	// default location - 서울시청
		
		////////////////////////////////////////////
		// Initialize NMap Location Manager
		mMapLocationManager = new NMapLocationManager(this);
		
		////////////////////////////////////////////
		// Initialize NMap Data Provider		
		mCurrentAddress = new String[3];

		////////////////////////////////////////////
		// Initialize NMap Overlay Manager		
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.map_activity, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_findmylocation:
			mMapLocationManager.setOnLocationChangeListener(this);
			mMapLocationManager.enableMyLocation(true);
			flag = true;
			return true;

		case R.id.menu_findmyaddress:
			super.setMapDataProviderListener(this);
			NGeoPoint point = mMapController.getMapCenter();
			findPlacemarkAtLocation(point.longitude, point.latitude);
			Log.v("NHK", String.valueOf(point.longitude));
			Log.v("NHK", String.valueOf(point.latitude));
			return true;

		case R.id.menu_history:
			int markerId = NMapPOIflagType.PIN;

			// TODO: load point data & display
			
			// set POI data
			NMapPOIdata poiData = new NMapPOIdata(3, mMapViewerResourceProvider);
			poiData.beginPOIdata(3);
			poiData.addPOIitem(127.10793762466238, 37.401421806078154, "2013-08-13 12:00:00", markerId, 0); // add pin 1
			poiData.addPOIitem(127.10703862466238, 37.401221206078154, "2013-08-13 14:00:00", markerId, 0); // add pin 2
			poiData.addPOIitem(127.11703862466238, 37.401234636078154, "2013-08-13 18:00:00", markerId, 0); // add pin 2
			poiData.endPOIdata();

			// create POI data overlay
			NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
			poiDataOverlay.showAllPOIdata(0); // show pins
			return true;
			
		case R.id.menu_starttracking:
			handler = new Handler();
			handler.postDelayed(this, 5000);
			return true;
			
		case R.id.menu_stoptracking:
			handler.removeCallbacks(this);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void run() {
		mMapLocationManager.setOnLocationChangeListener(this);
		mMapLocationManager.enableMyLocation(true);
		handler.postDelayed(this, 5000);
		
		Toast.makeText(this, "saving...", Toast.LENGTH_SHORT).show();
		// TODO: save location
	}
	
	//////////////////////////////////////////
	// START NMap location manager callback //
	
	@Override
	public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {
		if (flag) {
			mMapController.setMapCenter(myLocation);
			flag = false;
		}
		return false;
	}
	@Override
	public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLastLocation) {}
	@Override
	public void onLocationUpdateTimeout(NMapLocationManager locationManager) {}
	
	// END NMap location manager callback //
	////////////////////////////////////////
	
	//////////////////////////////////////////
	// START NMap Data Provider callback //

	@Override
	public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {
		mCurrentAddress[0] = placeMark.doName;
		mCurrentAddress[1] = placeMark.siName;
		mCurrentAddress[2] = placeMark.dongName;
		
		Toast.makeText(getApplicationContext(), mCurrentAddress[2], Toast.LENGTH_SHORT).show();
	}
	
	// END NMap location manager callback //
	////////////////////////////////////////
}