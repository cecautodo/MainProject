package com.example.pranav.autodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {
    EditText first;
    EditText last;
    EditText pass;
    EditText conf;
    EditText email;
    EditText phone;
    Button reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        first=(EditText) findViewById(R.id.first_name);
        last=(EditText)findViewById(R.id.last_name);
        pass=(EditText)findViewById(R.id.password);
        conf=(EditText)findViewById(R.id.confirm);
        email=(EditText)findViewById(R.id.email);
        phone=(EditText)findViewById(R.id.phone);
        reg=(Button)findViewById(R.id.register);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent p=new Intent(getApplicationContext(),Menu.class);
                startActivity(p);
            }
        });
    }
}
