package com.example.pranav.autodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {
    Button rem;
    Button profile;
    Button calllist;
    Button parental;
    Button Logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        rem=(Button) findViewById(R.id.Reminder);
        profile=(Button) findViewById(R.id.Profile);
        calllist=(Button)findViewById(R.id.calllist);
        parental=(Button)findViewById(R.id.Parental);
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
    Logout=(Button)findViewById(R.id.Logout);
    Logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent l=new Intent(getApplicationContext(),Home.class);
            startActivity(l);
        }
    });
    }
}
