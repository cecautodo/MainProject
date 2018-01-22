package com.example.pranav.autodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Home extends AppCompatActivity {
    EditText userid;

    EditText pass;
    Button bregister;
    Button blogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userid=(EditText)findViewById(R.id.editText1);
        pass=(EditText)findViewById(R.id.editText2);
        bregister=(Button)findViewById(R.id.button1);
        blogin=(Button)findViewById(R.id.button2);
        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Chumma",Toast.LENGTH_SHORT).show();
                if(userid.getText().toString().equals("12345")&&pass.getText().toString().equals("pass")) {
                    Intent n = new Intent(getApplicationContext(), Menu.class);
                    startActivity(n);
                }
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
