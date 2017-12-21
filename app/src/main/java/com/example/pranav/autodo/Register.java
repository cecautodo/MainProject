package com.example.pranav.autodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    EditText first;
    EditText last;
    EditText pass;
    EditText conf;
    EditText email;
    EditText phone;
    Button reg;
    String pass1,pass2;
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
        reg = (Button) findViewById(R.id.register);



            reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if(pass.getText().toString().equals(conf.getText().toString())&&
                        (first.getText().toString().equals("user"))&&
                        last.getText().toString().equals("name")&&
                        email.getText().toString().equals("user@gmail.com")&&
                        phone.getText().toString().equals("12345")) {
                    Intent p = new Intent(getApplicationContext(), Menu.class);
                    startActivity(p);

                    /*if(pass1.equals(pass2)) {
                         }*/
                }
                }
            });

        }
}
