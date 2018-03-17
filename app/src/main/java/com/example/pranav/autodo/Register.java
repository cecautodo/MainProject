package com.example.pranav.autodo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Register extends AppCompatActivity {
    EditText first;
    EditText last;

    EditText pass;
    EditText conf;
    EditText email;
    EditText phone;
    Button reg;
    String pass1,pass2;
    String method = "register";
    String namespace = "http://tempuri.org/";
    String soapaction = namespace + method;
    String url = "http://192.168.43.97/WebService.asmx";
    String first_name,last_name,Email,Phone,password;
    String phoneid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        first=(EditText) findViewById(R.id.first_name);
        last=(EditText)findViewById(R.id.last_name);
        pass=(EditText)findViewById(R.id.password);
        conf=(EditText)findViewById(R.id.confirm);
        email=(EditText)findViewById(R.id.email);
        phone=(EditText)findViewById(R.id.phone);
        reg = (Button) findViewById(R.id.register);


        try {
            if (Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        } catch (Exception e) {

        }



        reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    /*if(pass1.equals(pass2)) {

                         }*/
                    TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

                    phoneid = telephonyManager.getDeviceId().toString();
                    Toast.makeText(getApplicationContext(),phoneid,Toast.LENGTH_LONG).show();

                    first_name = first.getText().toString();
                    last_name = last.getText().toString();
                    password = pass.getText().toString();
                    Email = email.getText().toString();
                    Phone = phone.getText().toString();
                    if (pass.getText().toString().equals(conf.getText().toString())) {
                        try {
                            SoapObject sop = new SoapObject(namespace, method);
                            sop.addProperty("first_name", first_name);
                            sop.addProperty("last_name", last_name);
                            sop.addProperty("Email", Email);
                            sop.addProperty("password", password);
                            sop.addProperty("Phoneno", Phone);
                            sop.addProperty("imei",phoneid);
                            SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            env.setOutputSoapObject(sop);
                            env.dotNet = true;
                            HttpTransportSE hp = new HttpTransportSE(url);
                            hp.call(soapaction, env);
                            String result = env.getResponse().toString();
                            if (result.equals("ok"))
                            {
                                Intent p = new Intent(getApplicationContext(), Menu.class);
                                startActivity(p);
                            }
                        } catch (Exception e) {

                            Toast.makeText(getApplicationContext(),"Failed"+e,Toast.LENGTH_LONG).show();
                        }


                    }
                }
            });

        }
}
