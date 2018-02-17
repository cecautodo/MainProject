package com.example.pranav.autodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class Reminder extends AppCompatActivity {


    EditText ed_loc, ed_msg;

     Button bt;

    int request=1;

    String lati = "", longi = "", locname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

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