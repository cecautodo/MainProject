package com.example.pranav.autodo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.telephony.TelephonyManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Reminder extends AppCompatActivity {


    EditText ed_loc, ed_msg,latitu,longitu;
    String msg;
String phoneid;
     Button bt;
    String method = "reminder";
    String namespace = "http://tempuri.org/";
    String soapaction = namespace + method;
    String url = "http://192.168.43.97/WebService.asmx";
String status="1";


    int request=1;

    String lati = "", longi = "", locname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        latitu=(EditText)findViewById(R.id.ed_lati);
        longitu=(EditText)findViewById(R.id.ed_longi);
        ed_loc = (EditText) findViewById(R.id.ed_rem_loc);
        ed_msg = (EditText) findViewById(R.id.ed_rem_message);
        bt = (Button) findViewById(R.id.btn_rem_Done);

        ed_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(Reminder.this),request);
//                    flag = 3;
                }
                catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e){
                    e.printStackTrace();
                }
            }
        });
        try {
            if (Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        } catch (Exception e) {

        }

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg=ed_msg.getText().toString();
                latitu.setText(lati);
                longitu.setText(longi);
                lati=latitu.getText().toString();
                longi=longitu.getText().toString();
              //TelephonyManager telephonyManager=(TelephonyManager)getApplicationContext().getSystemService(Context.TELECOM_SERVICE);
                TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

                phoneid = telephonyManager.getDeviceId().toString();
                try {
                    SoapObject sop = new SoapObject(namespace,method);
                    //sop.addProperty("user_id", userid);
                    sop.addProperty("Lattitude", lati);
                    sop.addProperty("Longitude", longi);
                    sop.addProperty("Message",msg);
                    sop.addProperty("imei",phoneid);
                sop.addProperty("status",status);
             //   sop.addProperty("imei",phoneid);
//                    sop.addProperty("Loc_name",locname);
                    SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    env.setOutputSoapObject(sop);
                    env.dotNet=true;
                    HttpTransportSE hp = new HttpTransportSE(url);
                    hp.call(soapaction,env);
                    String result = env.getResponse().toString();
                    if (result.equals("ok"))
                    {
                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(),"Failed"+e,Toast.LENGTH_LONG).show();

                }

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){

            Log.d("debug","sdsdsd");
            if(resultCode==RESULT_OK){
                Log.d("debug","dfdfd");
                Place place=PlacePicker.getPlace(Reminder.this, data);
              Toast.makeText(getApplicationContext(), place.getLatLng().latitude + ", he he " + place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
                lati = place.getLatLng().latitude + "";
                longi = place.getLatLng().longitude + "";
                locname = place.getName() + "";
                Log.d("debug",locname);
                ed_loc.setText(locname);

            }
        }

    }
}