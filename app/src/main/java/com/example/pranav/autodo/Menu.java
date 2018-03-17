package com.example.pranav.autodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Menu extends AppCompatActivity {
    Button rem;
    Button profile;

    Button calllist,add_trust;
    Button parental;
    //Button Logout;
    //Button emer;
    ImageButton logout;
    ImageButton emerg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent s = new Intent(getApplicationContext(), LocationService.class);
        startService(s);
        setContentView(R.layout.activity_menu);
        rem=(Button) findViewById(R.id.Reminder);
        profile=(Button) findViewById(R.id.Profile);
        calllist=(Button)findViewById(R.id.calllist);
        parental=(Button)findViewById(R.id.Parental);
add_trust=(Button)findViewById(R.id.bt_addtrust);
      //  emer=(Button)findViewById(R.id.bt_emerg);

        logout=(ImageButton)findViewById(R.id.logout);
        emerg=(ImageButton)findViewById(R.id.imgbt_emergency);
        rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Reminder.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), profile.class));
            }
        });

        parental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n=new Intent(getApplicationContext(),parental_home.class);
                startActivity(n);


            }

        });
        calllist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent c=new Intent(getApplicationContext(),whitelist.class);
                startActivity(c);

            }
        });
   // Logout=(Button)findViewById(R.id.Logout);
    logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent l=new Intent(getApplicationContext(),Home.class);
            startActivity(l);
        }
    });
    emerg.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent k=new Intent(getApplicationContext(),emergency_service.class);
            startActivity(k);

        }

    });
    add_trust.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent w=new Intent(getApplicationContext(),add_trustdevice.class);
            startActivity(w);
        }
    });
    }
}
