package com.example.pranav.autodo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class profile extends AppCompatActivity {
    RadioGroup radio_profile;
    RadioButton radio_silent;
    RadioButton radio_vibration;
    RadioButton radio_general;
    String mode="";
    Button display;
    String path,fname;
    int flag=0;
    EditText profile_location;
    String lati="",longi="",locname="";
    String method = "profile";
    String namespace = "http://tempuri.org/";
    String soapaction = namespace + method;
    String url = "http://192.168.43.97/WebService.asmx";
    Button done;
    String latitu;
    String longitu;
  //  EditText latitu,longitu;
    String latitude,longiude;
    String phoneid="";
    Switch wifi;
    Button ring,wallpaper;
    String wifi_status;
    File f;
    String ringtone="",wallpaper_path="";
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201;
    private Uri mImageCaptureUri;
    //	private File outPutFile = null;
    private String imagename = "";
    private static final int FILE_SELECT_CODE = 0;


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
       ring=(Button)findViewById(R.id.bt_prof_ring);
       wallpaper=(Button)findViewById(R.id.prof_bt_wifi);
        // done=(Button)findViewById(R.id.profile_done);
       // latitu=(EditText)findViewById(R.id.ed_profile_lati);
        //longitu=(EditText)findViewById(R.id.ed_profile_longi);
wifi=(Switch) findViewById(R.id.switch_wifi);
        profile_location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(profile.this), 1);
                    flag = 3;
                    }
                catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e){
                    e.printStackTrace();
                }

            }
        });
        ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=2;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //getting all types of files
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"),FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(getApplicationContext(), "Please install a File Manager.",Toast.LENGTH_SHORT).show();
                }
            }
        });

wallpaper.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        flag=1;
        selectImageOption();
    }
});
        try {
            if (Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        } catch (Exception e) {

        }


               display.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       if(wifi.isChecked())
                       {
                       wifi_status="on";
                       }
                       else {
                           wifi_status="off";
                       }

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
                         //   latitu.setText(lati);
                          //  longitu.setText((longi));
                           //latitude = latitu.getText().toString();
                           //longiude = longitu.getText().toString();
                       TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

                       phoneid = telephonyManager.getDeviceId().toString();

//latitu=lati;
//longitu=longi;
//latitu="5654";
//longitu="54654";
                        try {
                           SoapObject sop = new SoapObject(namespace,method);
                           sop.addProperty("Lattitude", lati);
                           sop.addProperty("Longitude", longi);
                           sop.addProperty("ringtone",ringtone);
                           sop.addProperty("wallpaper",wallpaper_path);
                           sop.addProperty("status",mode);
                           sop.addProperty("imei",phoneid);
                           sop.addProperty("wifi",wifi_status);

                            //sop.addProperty("Loc_name",locname);
                           SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                           env.setOutputSoapObject(sop);
                           env.dotNet=true;
                           HttpTransportSE hp = new HttpTransportSE(url);
                           hp.call(soapaction,env);
                           String result = env.getResponse().toString();
                            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                       } catch (Exception e) {

                            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                        }

                   }
               });


    }
    private void selectImageOption() {
        final CharSequence[] items = {"Capture Photo","Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Capture Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Date date = new Date();
                    DateFormat df = new SimpleDateFormat("-mm-ss");
                    imagename = df.format(date) + ".jpg";
                    f = new File(android.os.Environment.getExternalStorageDirectory(), imagename);
                    mImageCaptureUri = Uri.fromFile(f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(intent, CAMERA_CODE);

                } else if (items[item].equals("Choose from Gallery")) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, GALLERY_CODE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(flag==1) {
            if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) {

                mImageCaptureUri = data.getData();
                System.out.println("Gallery Image URI: " + mImageCaptureUri);
                //   CropingIMG();

                try {
                    Uri selectedImage = data.getData();
                    String  path=getRealPathFromURI(selectedImage);
                    File file = new File(path);
                    wallpaper_path = path;
                    Toast.makeText(getApplicationContext(),wallpaper_path,Toast.LENGTH_SHORT).show();
//	                pathname=path.substring(path.lastIndexOf("/")+1);

                    // image = decodeFile(path);
                    byte[] byteArray = null;
                    try
                    {
                        InputStream inputStream = new FileInputStream(file);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] b = new byte[2048*8];
                        int bytesRead =0;

                        while ((bytesRead = inputStream.read(b)) != -1)
                        {
                            bos.write(b, 0, bytesRead);
                        }

                        byteArray = bos.toByteArray();
                        Bitmap bm= BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                        //iml.setImageBitmap(bm);
                    }
                    catch (IOException e)
                    {
                        Log.d("=err====", e.getMessage()+"");
                        // Toast.makeText(this,"String :"+e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }

//	                String str = Base64.encodeToString(byteArray, Base64.DEFAULT);
//	                encodedImage = str;
                } catch (Exception e) {

                }
            } else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {

                System.out.println("Camera Image URI : " + mImageCaptureUri);
                //  CropingIMG();

//	            String  path = f.getAbsolutePath();

                Uri selectedImage = data.getData();
                String  path=getRealPathFromURI(selectedImage);
                File file = new File(path);
                wallpaper_path = path;

//	            Bitmap image = decodeFile(path); //sha corrected
                try {
                    Bundle extras = data.getExtras();
                    Bitmap image = (Bitmap) extras.get("data");
                    //Bitmap image = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                    //im1.setImageBitmap(image);
                } catch (Exception e) {
                    // Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
        if(flag==2)
        {
            switch (requestCode)
            {
                case FILE_SELECT_CODE:
                    if (resultCode == RESULT_OK)
                    {

                        Uri uri = data.getData();
                        Log.d("File Uri", "File Uri: " + uri.toString());
                        try
                        {


                            path = com.example.pranav.autodo.FileUtils.getPath(this, uri);
                            ringtone = com.example.pranav.autodo.FileUtils.getPath(this, uri);

//	        	                        File fil = new File(path);
                            //float fln=(float) (fil.length()/1024);
                            fname=path.substring(path.lastIndexOf("/")+1);
                            //e22.setText(fname);
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(), "Ringtone chooser", Toast.LENGTH_SHORT).show();
                        }
                    }
            }
        }



       if(flag==3) {
           if (requestCode == 1) {

               if (resultCode == RESULT_OK) {
                   Place place = PlacePicker.getPlace(profile.this, data);
                   StringBuilder stBuilder = new StringBuilder();
                   Toast.makeText(getApplicationContext(), place.getLatLng().latitude + ", he he " + place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
                   lati = place.getLatLng().latitude + "";
                   longi = place.getLatLng().longitude + "";
                   locname = place.getName() + "";
                   profile_location.setText(locname);

               }
           }

       }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String path;
        Cursor cursor = getContentResolver()
                .query(contentURI, null, null, null, null);
        if (cursor == null)
            path=contentURI.getPath();

        else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            path=cursor.getString(idx);

        }
        if(cursor!=null)
            cursor.close();
        return path;
    }

    }
