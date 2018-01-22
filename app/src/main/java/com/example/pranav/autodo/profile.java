package com.example.pranav.autodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class profile extends AppCompatActivity {
    RadioGroup radio_profile;
    RadioButton radio_silent;
    RadioButton radio_vibration;
    RadioButton radio_general;
    String mode;
    Button display;
    EditText profile_location;
    String lati="",longi="",locname="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        display = (Button) findViewById(R.id.profile_done);
        radio_profile=(RadioGroup)findViewById(R.id.profile_mode);
        radio_general =(RadioButton)findViewById(R.id.profile_general);
        radio_silent=(RadioButton)findViewById(R.id.profile_silent);
        radio_vibration=(RadioButton)findViewById(R.id.profile_vibration);
        profile_location=(EditText)findViewById(R.id.prof_loc);
        profile_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(profile.this), 1);
//                    flag = 3;
                }
                catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e){
                    e.printStackTrace();
                }

            }
        });


               display.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       if(radio_general.isChecked())
                       {
                           mode="General";
                       }
                       if(radio_vibration.isChecked())
                       {
                           mode="Vibration";
                       }
                       if(radio_silent.isChecked())
                       {
                           mode="Silent";
                       }
                       Toast.makeText(getApplicationContext(),mode,Toast.LENGTH_SHORT).show();
                   }
               });

            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                Place place=PlacePicker.getPlace(profile.this, data);
                Toast.makeText(getApplicationContext(), place.getLatLng().latitude + ", he he " + place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
                lati = place.getLatLng().latitude + "";
                longi = place.getLatLng().longitude + "";
                locname = place.getName() + "";
                profile_location.setText(locname);
            }
        }

    }

    }
