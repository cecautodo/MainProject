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

public class Home extends AppCompatActivity {
    EditText userid;
    public static int bk;
    String method = "login";
    String namespace = "http://tempuri.org/";
    String soapaction = namespace + method;
    String url = "http://192.168.43.97/WebService.asmx";

String user_id,password;
    EditText pass;
    Button bregister;
    Button blogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userid=(EditText)findViewById(R.id.editText1);
        pass=(EditText)findViewById(R.id.editText2);
        bregister=(Button)findViewById(R.id.button1);
        blogin=(Button)findViewById(R.id.button2);


        try {
            if (Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        } catch (Exception e) {

        }

        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Chumma",Toast.LENGTH_SHORT).show();
               // if(userid.getText().toString().equals("12345")&&pass.getText().toString().equals("pass")) {

              //  }
              //  Intent n = new Intent(getApplicationContext(), Menu.class);
              //  startActivity(n);
                Intent l = new Intent(getApplicationContext(), Menu.class);
                startActivity(l);

                startService(new Intent(getApplicationContext(),LocationService.class));
startService(new Intent(getApplicationContext(),notiservise.class));
                user_id=userid.getText().toString();
                password=pass.getText().toString();
                try {
                    SoapObject sop = new SoapObject(namespace,method);
                    sop.addProperty("username", user_id);
                    sop.addProperty("password", password);
                    SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    env.setOutputSoapObject(sop);
                    env.dotNet = true;
                    HttpTransportSE hp = new HttpTransportSE(url);
                    hp.call(soapaction,env);
                    String result = env.getResponse().toString();
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                    if (result.equals("ok"))
                    {
                       // Intent l = new Intent(getApplicationContext(), Menu.class);
                       // startActivity(l);

                    }
                    else
                        {
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Failed"+e,Toast.LENGTH_SHORT).show();
                }

            }
        });
        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent m=new Intent(getApplicationContext(),Register.class);
                startActivity(m);
            }
        });
    }

}
