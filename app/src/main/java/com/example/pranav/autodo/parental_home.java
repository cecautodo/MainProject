package com.example.pranav.autodo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class parental_home extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner sp;
    //String child_list[]={"seetha","prasii","pranav","ajula"};
    String method = "parental";
    String namespace = "http://tempuri.org/";
    String soapaction = namespace + method;
    String url = "http://192.168.43.97/WebService.asmx";
    String[] Array = new String[]{"ajula", "pranav", "praseetha", "seetha"};
   // TextView child;
    int user_id=1;
   public static String lattitude,longitude;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parental_home);
done=(Button)findViewById(R.id.bt_parent_done);
     //   child=(TextView)findViewById(R.id.text_child);
    sp=(Spinner) findViewById(R.id.spinner_child);
        ArrayAdapter<String> sr= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,Array);
        sp.setAdapter(sr);
        sp.setOnItemSelectedListener(this);

        try {
            if (Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        } catch (Exception e) {

        }
        /*child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
*/

done.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        /*
        Double x=Double.parseDouble(lattitude);
        Double y=Double.parseDouble(longitude);
        Toast.makeText(getApplicationContext(),"ok"+x,Toast.LENGTH_LONG).show();
        */
        Intent j=new Intent(getApplicationContext(),parental_map.class);
        j.putExtra("latitude",lattitude);
        j.putExtra("longitude",longitude);
        startActivity(j);
    }
});


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
      //  Toast.makeText(getApplicationContext(),Array[i],Toast.LENGTH_SHORT).show();
        try {

            SoapObject sop = new SoapObject(namespace,method);
            sop.addProperty("user_id", user_id);
            //sop.addProperty("password", password);
            SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            env.setOutputSoapObject(sop);
            env.dotNet = true;
            HttpTransportSE hp = new HttpTransportSE(url);
            hp.call(soapaction,env);
            String result = env.getResponse().toString();
           Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            String temp[]=result.split("\\$");
            lattitude=temp[0];
            longitude=temp[1];
            /*Intent j=new Intent(getApplicationContext(),parental_home.class);
            j.putExtra("latitude",lattitude);
            j.putExtra("longitude",longitude);
            startActivity(j);
            *//*if (result.equals("ok"))
            {
                Intent l = new Intent(getApplicationContext(), Menu.class);
                startActivity(l);

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();

            }*/

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Failed"+e,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}


