package com.example.pranav.autodo;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



import android.R.integer;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;


public class notiservise extends Service {
	Handler hd;
	String namespace = "http://tempuri.org/";
	String method = "readNews";
	String url = "http://192.168.43.97/WebService.asmx";
	String soapaction = namespace + method;
	String[] news, description, date;
	String phoneid;
	String message;
	public static String[] title, id;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		try {
			if (Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		hd = new Handler();
		hd.post(r);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		hd.removeCallbacks(r);
	}
	public Runnable r = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
//			SimpleDateFormat df=new SimpleDateFormat("yyy-MM-dd");
//			String curdate=df.format(new java.util.Date());

			//SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//			 String logid=sh.getString("logid", "");
			TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

			phoneid = telephonyManager.getDeviceId().toString();
			try {
				//SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			//	url = sh.getString("url", "");
				//String logid=sh.getString("logid","");

				SoapObject sop = new SoapObject(namespace, method);
				sop.addProperty("lati", LocationService.lati);
				sop.addProperty("longi", LocationService.logi);
sop.addProperty("imei",phoneid);
				SoapSerializationEnvelope snv = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				snv.setOutputSoapObject(sop);
//				snv.dotNet = true;
				HttpTransportSE hp = new HttpTransportSE(url);
				hp.call(soapaction, snv);

				String result = snv.getResponse().toString();
				Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

				if (!result.equalsIgnoreCase("failed")) {
					message=result;
					notis();
					/*	String[] temp = result.split("\\#");
					if (temp.length > 0) {
		//				inItArray(temp.length);
						for (int z = 0; z < temp.length; z++) {
							String[] temp1 = temp[z].split("\\@");
							news[z] = temp1[0];
							description[z] = temp1[1];
							date[z] = temp1[2];
						}

					} else if (result.equalsIgnoreCase("failed")) {
						Toast.makeText(getApplicationContext(), "No Category..", Toast.LENGTH_LONG).show();
					}*/
				}
			} catch (Exception e) {
				//Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
			}
			hd.postDelayed(r, 45000);
		}
/*
		private void inItArray(int length) {
			news = new String[length];
			description = new String[length];
			date = new String[length];
		}*/
	};

	public void notis() {
		// TODO Auto-generated method stub
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
			.setSmallIcon(R.drawable.geolocatelogo)
				.setContentTitle("Reminder")
				.setContentText(message)
				.setAutoCancel(true);
//Creates an explicit intent for an Activity in your app
	Intent resultIntent = new Intent(getApplicationContext(), LocationFullNews.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(Locationnews.class);
// Adds the Intent that starts the Activity to the top of the stack
	//	stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
//mBuilder.setVibrate();
		Notification note = mBuilder.build();
		note.defaults |= Notification.DEFAULT_VIBRATE;
		note.defaults |= Notification.DEFAULT_SOUND;

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
		mNotificationManager.notify(100, note);// mBuilder.build());
	}
}