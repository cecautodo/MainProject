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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class contact_select extends AppCompatActivity implements AdapterView.OnItemClickListener{
        ListView contact;
        ArrayList<String> lst_cntct;
    String contactNumber = null;

    int i=0;
    String contactName = null;
    String method = "whitelist";
    String namespace = "http://tempuri.org/";
    String soapaction = namespace + method;
    String url = "http://192.168.43.97/WebService.asmx";

    private static final String TAG = contact_select.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;     // contacts unique ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_select);

        try {
            if (Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        } catch (Exception e) {

        }

        lst_cntct=new ArrayList<>();

    }
    public void onClickSelectContact(View btnSelectContact) {

        // using native contacts selection
        // Intent.ACTION_PICK = Pick an item from the data, returning what was selected.
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();
            retrieveContactName();
            retrieveContactNumber();
           contact=(ListView)findViewById(R.id.lst_contact);
            ArrayAdapter<String> sr=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,lst_cntct);
            contact.setAdapter(sr);
            contact.setOnItemClickListener(this);
          //  retrieveContactPhoto();

        }
    }
/*
    private void retrieveContactPhoto() {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                ImageView imageView = (ImageView) findViewById(R.id.img_contact);
                imageView.setImageBitmap(photo);
            }

            assert inputStream != null;
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    */

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

lst_cntct.add(contactName+"  "+contactNumber);

    }

    private void retrieveContactName() {


        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Toast.makeText(getApplicationContext(),contactName,Toast.LENGTH_LONG).show();


        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName);

    }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//        Toast.makeText(getApplicationContext(),lst_cntct.get(i),Toast.LENGTH_LONG).show();
            String temp[]=lst_cntct.get(i).split("\\  ");
            String cntct_num=temp[1];
            Toast.makeText(getApplicationContext(),cntct_num,Toast.LENGTH_SHORT).show();
            try {
                SoapObject sop = new SoapObject(namespace, method);
                sop.addProperty("loc_id", whitelist.loc_id);
                sop.addProperty("num", cntct_num);

                 SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                env.setOutputSoapObject(sop);
                env.dotNet = true;
                HttpTransportSE hp = new HttpTransportSE(url);
                hp.call(soapaction, env);
                String result = env.getResponse().toString();
                if (result.equals("ok"))
                {
                   // Intent p = new Intent(getApplicationContext(), Menu.class);
                    //startActivity(p);
                }
            } catch (Exception e) {

                Toast.makeText(getApplicationContext(),"Failed"+e,Toast.LENGTH_LONG).show();
            }

    }
    }
