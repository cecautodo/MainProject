package com.example.pranav.autodo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.widget.Toast;

public class wifies extends Service {
	
	WifiManager wm;
	/* (non-Javadoc)
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		Toast.makeText(getApplicationContext(), "start service", Toast.LENGTH_LONG).show();
		
		wm=(WifiManager)getSystemService(Context.WIFI_SERVICE);
		
		ConnectivityManager manager = (ConnectivityManager)getSystemService(wifies.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		if(mWifi==null){
			Toast.makeText(getBaseContext(), "CAn't handle wifi", Toast.LENGTH_LONG).show();
		}
		else
		{
			wm.setWifiEnabled(true);
			Toast.makeText(getBaseContext(), "Wifi is ON", Toast.LENGTH_LONG).show();
			//finish();
		}
		return START_STICKY;
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		wm.setWifiEnabled(false);
		Toast.makeText(getBaseContext(), "Wifi is OFF", Toast.LENGTH_LONG).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
