package com.example.pranav.autodo;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.android.internal.telephony.ITelephony;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class call extends Service {

	TelephonyManager telephonyManager;
	String namespace = "urn:demo";
	String url = "";
	String method = "blockincomingcall";
	String soapaction = namespace + method;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub		
		super.onCreate();

		telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
		telephonyManager.listen(MyPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		// j1=(String)intent.getExtras().get("Extras");
		super.onStart(intent, startId);
	}

	private PhoneStateListener MyPhoneListener = new PhoneStateListener() {
		public void onCallStateChanged(int State, String incoming) {
			try {
				switch (State) {
					case TelephonyManager.CALL_STATE_RINGING:

//						Toast.makeText(getApplicationContext(), "CALL_STATE_RINGING  " + incoming + " home.kb " + Autospotty.bk, Toast.LENGTH_SHORT).show();

						if (Home.bk == 1) {

							incoming = incoming.trim();
							int a = checknumber(incoming.substring(incoming.length() - 10, incoming.length()));

//							Toast.makeText(getApplicationContext(), a + "  incoming  " + incoming, Toast.LENGTH_LONG).show();
							if (a == 1) {
								//--Blocking calls on receiving----
								telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
								Class<?> c = Class.forName(telephonyManager.getClass().getName());
								Method m = c.getDeclaredMethod("getITelephony");
								m.setAccessible(true);



									ITelephony telephonyService = (ITelephony) m.invoke(telephonyManager);
								telephonyService.endCall();
								Toast.makeText(getApplicationContext(), "blocked", Toast.LENGTH_SHORT).show();
							}
						}
						break;

					case TelephonyManager.CALL_STATE_OFFHOOK:
//						Toast.makeText(getApplicationContext(), "CALL_STATE_OFFHOOK", Toast.LENGTH_SHORT).show();
						break;

					case TelephonyManager.CALL_STATE_IDLE:
//						Toast.makeText(getApplicationContext(), "CALL_STATE_IDLE", Toast.LENGTH_SHORT).show();
						break;

					default:
//						Toast.makeText(getApplicationContext(), "default", Toast.LENGTH_SHORT).show();
						Log.i("Default", "Unknown phone state=" + State);
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage() + "", Toast.LENGTH_LONG).show();
				Log.i("Exception", "PhoneStateListener() e = " + e);
			}
		}

		private int checknumber(String incoming) {
			int a = 0;
			try {
				SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				url = sh.getString("url", "");
				SoapObject request = new SoapObject(namespace, method);
				request.addProperty("incoming", incoming);
//			request.addProperty("l_name", LocationService.place);
				request.addProperty("imei", sh.getString("imei", ""));
//				Toast.makeText(getApplicationContext(), "incoming call number....." + incoming, Toast.LENGTH_LONG).show();
//			request.addProperty("profid",Autospotty.pid);
//				Toast.makeText(getApplicationContext(), "profile id for block:" + Autospotty.pid, Toast.LENGTH_LONG).show();
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.setOutputSoapObject(request);
//				envelope.dotNet = true;
				HttpTransportSE htse = new HttpTransportSE(url);
				htse.call(soapaction, envelope);

				String response = envelope.getResponse().toString();
				//Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

				if (!response.equalsIgnoreCase("na")) {
					a = 1;
				}

			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getApplicationContext(), "error" + e.getMessage() + "", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			return a;
		}
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//mLocationManager.removeUpdates(mLocationListener);
		telephonyManager.listen(MyPhoneListener, PhoneStateListener.LISTEN_NONE);
//	Toast.makeText(this, "Service to block call cancelled..!!!", Toast.LENGTH_LONG).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
