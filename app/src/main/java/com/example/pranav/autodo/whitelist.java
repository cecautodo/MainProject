package com.example.pranav.autodo;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.ContactsContract;
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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class whitelist extends AppCompatActivity {
EditText ed_loc;
String lati="",longi="",locname="";
Button done;
    String method = "register";
    String namespace = "http://dbcon/";
    String soapaction = namespace + method;
    String url = "";

   Button bt_cont,bt_calldone;
    private static final String TAG = whitelist.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;     // contacts unique ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whitelist);
           ed_loc=(EditText)findViewById(R.id.call_loc);
   bt_cont=(Button)findViewById(R.id.bt_contact);

         bt_calldone = (Button) findViewById(R.id.bt_calldone);
   ed_loc.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
           try {
               startActivityForResult(builder.build(whitelist.this), 1);
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
bt_calldone.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        try {
            SoapObject sop = new SoapObject(namespace,method);
            sop.addProperty("Lattitude", lati);
            sop.addProperty("Longitude", longi);


            sop.addProperty("Loc_name",locname);
            SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            env.setOutputSoapObject(sop);
            env.dotNet=true;
            HttpTransportSE hp = new HttpTransportSE(url);
            hp.call(soapaction,env);
            String result = env.getResponse().toString();
        } catch (Exception e) {}
    }
});

bt_cont.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI),REQUEST_CODE_PICK_CONTACTS);
    }
});
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                Place place=PlacePicker.getPlace(whitelist.this, data);
                Toast.makeText(getApplicationContext(), place.getLatLng().latitude + ", he he " + place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
                lati = place.getLatLng().latitude + "";
                longi = place.getLatLng().longitude + "";
                locname = place.getName() + "";
                ed_loc.setText(locname);
            }
        }
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            retrieveContactName();
            retrieveContactNumber();
            //  retrieveContactPhoto();

        }

    }
    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        Toast.makeText(getApplicationContext(),contactNumber,Toast.LENGTH_LONG).show();    }

    private void retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName);

    }
}
