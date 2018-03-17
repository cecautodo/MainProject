package com.example.pranav.autodo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class add_trustdevice extends AppCompatActivity {
    EditText trust_search;
    Button bt_search;
    Button bt_ok;
    String phone_no;
    String method = "searchtrustdevice";
    String namespace = "http://tempuri.org/";
    String soapaction = namespace + method;
    String url = "http://192.168.43.97/WebService.asmx";
    String first,last,imei;
    String phoneid;
    TextView trust_device;

    String method2="addtrustdevice";
            String soapaction2=namespace+method2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trustdevice);
        trust_search=(EditText)findViewById(R.id.ed_trust_search);
        bt_search=(Button)findViewById(R.id.bt_trust_search);
        bt_ok=(Button)findViewById(R.id.bt_trust_ok);
trust_device=(TextView) findViewById(R.id.trust_view);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            phone_no=trust_search.getText().toString();
                try {
                    SoapObject sop = new SoapObject(namespace,method);
                    sop.addProperty("phone_num",phone_no );

                    SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    env.setOutputSoapObject(sop);
                    env.dotNet = true;
                    HttpTransportSE hp = new HttpTransportSE(url);
                    hp.call(soapaction,env);
                    String result = env.getResponse().toString();
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                    if (result.equals("failed")) {
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                    }
                    else {
                        String t[]=result.split("\\$");
                        first=t[0];
                        last=t[1];
                        imei=t[2];
//Toast.makeText(getApplicationContext(),first,Toast.LENGTH_LONG).show();

  //    Toast.makeText(getApplicationContext(),last,Toast.LENGTH_LONG).show();

//                        Toast.makeText(getApplicationContext(),imei,Toast.LENGTH_LONG).show();
                        trust_device.setText(first);

                    }


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Failed"+e,Toast.LENGTH_SHORT).show();
                }
            }
        });

bt_ok.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {



        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
phoneid=telephonyManager.getDeviceId().toString();

//Toast.makeText(getApplicationContext(),imei+"1st",Toast.LENGTH_LONG).show();
//Toast.makeText(getApplicationContext(),phoneid,Toast.LENGTH_LONG).show();

        try {
            SoapObject sop = new SoapObject(namespace, method2);
            sop.addProperty("imei",imei);
            sop.addProperty("phoneid",phoneid);

            ////Toast.makeText(getApplicationContext(),imei,Toast.LENGTH_LONG).show();

           // Toast.makeText(getApplicationContext(),phoneid,Toast.LENGTH_LONG).show();


            SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            env.setOutputSoapObject(sop);
            env.dotNet = true;
            HttpTransportSE hp = new HttpTransportSE(url);
            hp.call(soapaction2, env);
            String result = env.getResponse().toString();
           Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
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
}
