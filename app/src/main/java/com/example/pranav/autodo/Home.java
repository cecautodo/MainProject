package com.example.pranav.autodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Home extends AppCompatActivity {
    EditText e1;
    EditText e2;
    Button bregister;
    Button blogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        e1=(EditText)findViewById(R.id.editText1);
        e2=(EditText)findViewById(R.id.editText2);
        bregister=(Button)findViewById(R.id.button1);
        blogin=(Button)findViewById(R.id.button2);
        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Chumma",Toast.LENGTH_SHORT).show();
                Intent n= new Intent(getApplicationContext(),Menu.class);
                startActivity(n);
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
