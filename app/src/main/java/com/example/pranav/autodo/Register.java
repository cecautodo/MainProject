package com.example.pranav.autodo;

import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    String method = "login";
    String namespace = "http://dbcon/";
    String soapaction = namespace + method;
    String url = "";


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
                if(pass.getText().toString().equals(conf.getText().toString())) {
                    Intent p = new Intent(getApplicationContext(), Menu.class);
                    startActivity(p);

                    /*if(pass1.equals(pass2)) {
                         }*/
                }
                else{

                Toast.makeText(getApplicationContext(),"password not match",Toast.LENGTH_LONG).show();
                
                }


                    try {
                        SoapObject sop = new SoapObject(namespace,method);
                        sop.addProperty("First Name", first);
                        sop.addProperty("Last Name", last);
                        sop.addProperty("password",pass);
                        sop.addProperty("Email",email);
                        sop.addProperty("Phoneno",phone);
                        SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                        env.setOutputSoapObject(sop);
                        HttpTransportSE hp = new HttpTransportSE(url);
                        hp.call(soapaction,env);
                        String result = env.getResponse().toString();
                    } catch (Exception e) {}


                }
            });

        }
}
