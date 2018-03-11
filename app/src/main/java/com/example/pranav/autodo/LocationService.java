package com.example.pranav.autodo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import android.R.string;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;

import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;



public class LocationService extends Service {

	private LocationManager locationManager;
	private Boolean locationChanged;
	String url = "http://192.168.43.97/WebService.asmx";
	String namespace = "http://tempuri.org/";
	public static String location, walaper, wifi, mode, r_tone, calblk, autosms, sms1;

	private Handler handler = new Handler();
	public static Location curLocation;
	public static boolean isService = true;
	String method3 = "autosmslist";
	String soapaction3 = namespace + method3;
	public static String lati = "", logi = "", place = "";
	String[] id, num;
	ArrayList<String> sms;
	String phoneid = "", tmplocs = "";

	String method2 = "customlocationcheck_pf";
	String soapaction2 = namespace + method2;
	String method = "location";
	String soap = namespace + method;

	LocationListener locationListener = new LocationListener() {

		public void onLocationChanged(Location location) {
			if (curLocation == null) {
				curLocation = location;
				locationChanged = true;
			} else if (curLocation.getLatitude() == location.getLatitude() && curLocation.getLongitude() == location.getLongitude()) {
				locationChanged = false;
				return;
			} else
				locationChanged = true;
			curLocation = location;

			if (locationChanged)
				locationManager.removeUpdates(locationListener);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			if (status == 0)// UnAvailable
			{
			} else if (status == 1)// Trying to Connect
			{
			} else if (status == 2) {// Available
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		curLocation = getBestLocation();

		if (curLocation == null) {
			System.out.println("starting problem.........3...");
			Toast.makeText(this, "GPS problem..........", Toast.LENGTH_SHORT).show();
		} else {
			// Log.d("ssssssssssss", String.valueOf("latitude2.........."+curLocation.getLatitude()));
		}
		isService = true;
	}

	final String TAG = "LocationService";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onStart(Intent intent, int startId) {
//	   Toast.makeText(this, "Start services", Toast.LENGTH_SHORT).show();

		String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if (!provider.contains("gps")) { //if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			sendBroadcast(poke);
		}
		handler.postDelayed(GpsFinder, 100);
	}

	@Override
	public void onDestroy() {
		String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if (provider.contains("gps")) { //if gps is enabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			sendBroadcast(poke);
		}

		handler.removeCallbacks(GpsFinder);
		handler = null;
//	   Toast.makeText(this, "Service Stopped..!!", Toast.LENGTH_SHORT).show();
		isService = false;
	}

	public Runnable GpsFinder = new Runnable() {
		public void run() {
			String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

			if (!provider.contains("gps")) { //if gps is disabled
				final Intent poke = new Intent();
				poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
				poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
				poke.setData(Uri.parse("3"));
				sendBroadcast(poke);
			}

			TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

			phoneid = telephonyManager.getDeviceId().toString();

			Location tempLoc = getBestLocation();

			if (tempLoc != null) {
				//Toast.makeText(getApplicationContext(), phoneid, Toast.LENGTH_LONG).show();

				curLocation = tempLoc;
				// Log.d("MyService", String.valueOf("latitude"+curLocation.getLatitude()));

				lati = String.valueOf(curLocation.getLatitude());
				logi = String.valueOf(curLocation.getLongitude());

				//updatelocation(lati, logi);

				// Toast.makeText(getApplicationContext(),URL+" received", Toast.LENGTH_SHORT).show();
				//Toast.makeText(getApplicationContext(),"\nlat.. and longi.."+ lati+"..."+logi, Toast.LENGTH_LONG).show();

				String loc = "";
				String address = "";
				Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
				try {
					List<Address> addresses = geoCoder.getFromLocation(curLocation.getLatitude(), curLocation.getLongitude(), 1);

					if (addresses.size() > 0) {
						for (int index = 0; index < addresses.get(0).getMaxAddressLineIndex(); index++)
							address += addresses.get(0).getAddressLine(index) + " ";
						//Log.d("get loc...", address);
						//place=addresses.get(0).getFeatureName().toString();
						//	place=addresses.get(0).getSubLocality().toString();

						place = addresses.get(0).getLocality().toString();

						//Toast.makeText(getApplicationContext(),place,Toast.LENGTH_LONG).show();
					} else {
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Toast.makeText(getBaseContext(), "locality-"+place, Toast.LENGTH_SHORT).show();
				//  place="palakkad";
				// if(!place.equalsIgnoreCase(tmplocs)){
				checkPf(place);
				//  }
				tmplocs = place;
			}
			handler.postDelayed(GpsFinder, 45000);// register again to start after 20 seconds...
		}

		private void checkPf(String place) {
			try {
				SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				url = sh.getString("url", "");
				String imei = sh.getString("imei", "");
				SoapObject request = new SoapObject(namespace, method2);
				request.addProperty("lati", LocationService.lati);
				request.addProperty("longi", LocationService.logi);
//				request.addProperty("l_name", place);
				request.addProperty("imei", imei);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.setOutputSoapObject(request);
				envelope.dotNet = true;
				HttpTransportSE htse = new HttpTransportSE(url);
				htse.call(soapaction2, envelope);

				String response = envelope.getResponse().toString();

//				Toast.makeText(getApplicationContext(), "Response jab : " + response, Toast.LENGTH_SHORT).show();

				if (!response.equalsIgnoreCase("failed")) {
					if (response.equalsIgnoreCase("anytype{}")) {
						Toast.makeText(getApplicationContext(), "No data found !", Toast.LENGTH_SHORT).show();
					} else {
					Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
						String[] r = response.split("#");
						walaper = r[2];
						mode = r[0];
						wifi = r[1];
						r_tone = r[3];
						calblk = r[4];
						autosms = r[5];
						//sms1=r[7];
						//Autospotty.pid = Integer.parseInt(locid);
						//Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

						try {
//							Bitmap decodedSampleBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + walaper);
							Bitmap decodedSampleBitmap = BitmapFactory.decodeFile(walaper);
							WallpaperManager wm = WallpaperManager.getInstance(getApplicationContext());
							wm.setBitmap(decodedSampleBitmap);
						} catch (Exception e) {
							Log.e(TAG, "Cannot set image as wallpaper", e);
						}
						if (wifi.equalsIgnoreCase("on")) {
							Intent i = new Intent(getApplicationContext(), wifies.class);
							startService(i);
						} else {
							Intent i = new Intent(getApplicationContext(), wifies.class);
							stopService(i);
						}
						//Toast.makeText(getApplicationContext(),mode,Toast.LENGTH_LONG).show();
						if (mode.equalsIgnoreCase("silent")) {
//							Toast.makeText(getApplicationContext(), "silent mode activated", Toast.LENGTH_LONG).show();
							AudioManager am = (AudioManager) getSystemService(getApplicationContext().AUDIO_SERVICE);
							am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
						} else if (mode.equalsIgnoreCase("vibrate")) {
							AudioManager am = (AudioManager) getSystemService(getApplicationContext().AUDIO_SERVICE);
							am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
						} else {
							try {
								AudioManager am = (AudioManager) getSystemService(getApplicationContext().AUDIO_SERVICE);
								am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

								// meee
								//code for ringtone
								String filepath = r_tone;
								File ringtoneFile = new File(filepath);
								Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringtoneFile.getAbsolutePath());
								RingtoneManager.setActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_RINGTONE, uri);
							} catch (Exception e) {
								Toast.makeText(getApplicationContext(), "Ringtone : " + e.toString(), Toast.LENGTH_SHORT).show();
							}
						}
						if (calblk.equals("on")) {
							Home.bk = 1;
							startService(new Intent(getApplicationContext(), call.class));
						} else {
							Home.bk = 0;
							stopService(new Intent(getApplicationContext(), call.class));
						}
						if (autosms.equalsIgnoreCase("on")) {
//							Toast.makeText(getApplicationContext(), "inside autosms", Toast.LENGTH_LONG).show();

							getnumbers();

							Log.d("message", sms1);
						}
					}
				}
			} catch (Exception e) {
//				Toast.makeText(getApplicationContext(), "error : Loc " + e.getMessage(), Toast.LENGTH_LONG).show();
//				Log.v("jjjjjjjjjjjjjjjjjjjj", e.toString());
				//e.printStackTrace();
			}
		}

		private void getnumbers() {
			try {
//				Log.d("get numbers function", msg);
//				Toast.makeText(getApplicationContext(), "inside getnumbers", Toast.LENGTH_LONG).show();

				sms = new ArrayList<String>();
				try {
					SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
					url = sh.getString("url", "");
					String imei = sh.getString("imei", "");

					SoapObject request = new SoapObject(namespace, method3);
					request.addProperty("imei", imei);

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.setOutputSoapObject(request);
//					envelope.dotNet = true;

					HttpTransportSE htse = new HttpTransportSE(url);
					htse.call(soapaction3, envelope);

					String response = envelope.getResponse().toString();

					//Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

					if (!response.equalsIgnoreCase("na")) {
						String[] msgs;
						String[] re = response.split("@");
						if (re.length > 0) {
							num = new String[re.length];
							id = new String[re.length];
							msgs = new String[re.length];

							for (int i = 0; i < re.length; i++) {
								String[] r = re[i].split("#");
								id[i] = r[0];
								num[i] = r[2];
								msgs[i] = r[1];
								sms.add(num[i]);
							}

							for (int i = 0; i < sms.size(); i++) {
								SmsManager sm = SmsManager.getDefault();
								sm.sendTextMessage(sms.get(i), null, msgs[i], null, null);
								deleteSms(id[i]);
//								Toast.makeText(getApplicationContext(), "message send  ",Toast.LENGTH_LONG).show();
								Thread.sleep(1000);
							}
						}
					}
				} catch (Exception e) {
//					Toast.makeText(getApplicationContext(), "error" + e.getMessage() + "", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			} catch (Exception e) {

			}
		}

		private void deleteSms(String aid)
		{
			String meth = "removeFromList";
			String soapact = namespace + meth;
			try {
				SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				url = sh.getString("url", "");

				SoapObject request = new SoapObject(namespace, meth);
				request.addProperty("sid", aid);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.setOutputSoapObject(request);
//				envelope.dotNet = true;

				HttpTransportSE htse = new HttpTransportSE(url);
				htse.call(soapact, envelope);

				String response = envelope.getResponse().toString();
			} catch (Exception e) {

			}
		}
	};

	private Location getBestLocation() {
		Location gpslocation = null;
		Location networkLocation = null;
		if (locationManager == null) {
			locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		}
		try {
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);// here you can set the 2nd argument time interval also that after how much time it will get the gps location
				gpslocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				//  System.out.println("starting problem.......7.11....");

			}
			if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
				networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		} catch (IllegalArgumentException e) {
			Log.e("error", e.toString());
		}
		if (gpslocation == null && networkLocation == null)
			return null;

		if (gpslocation != null && networkLocation != null) {
			if (gpslocation.getTime() < networkLocation.getTime()) {
				gpslocation = null;
				return networkLocation;
			} else {
				networkLocation = null;
				return gpslocation;
			}
		}
		if (gpslocation == null) {
			return networkLocation;
		}
		if (networkLocation == null) {
			return gpslocation;
		}
		return null;
	}

	protected void updatelocation(String longitude, String latitude)
	{
		SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		try {
			//SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			url = sh.getString("url", "");
			String imei = sh.getString("imei", "");
			SoapObject sop = new SoapObject(namespace, method);

			sop.addProperty("longit", longitude);
			sop.addProperty("lat", latitude);
			sop.addProperty("imei", imei);

			SoapSerializationEnvelope sen = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			sen.setOutputSoapObject(sop);

			HttpTransportSE http = new HttpTransportSE(url);
			http.call(soap, sen);

			String tyy = sen.getResponse().toString();

		} catch (Exception ex) {
			ex.printStackTrace();
			//Toast.makeText(getApplicationContext(), "error"+ex, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
