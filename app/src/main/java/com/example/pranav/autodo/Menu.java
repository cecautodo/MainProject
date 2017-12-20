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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        rem=(Button) findViewById(R.id.Reminder);
        profile=(Button) findViewById(R.id.Profile);
        calllist=(Button)findViewById(R.id.calllist);
        parental=(Button)findViewById(R.id.Parental);
        parental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n=new Intent(getApplicationContext(),parental_home.class);
                startActivity(n);
            }
        });

    }
}
