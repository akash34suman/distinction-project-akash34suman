package com.swin.edu.au.chatbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity2 extends AppCompatActivity {
    Button login,register;
    FirebaseUser firebaseUser;

 @Override
    protected void onStart() {
        super.onStart();

        //gettting current user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //checking if user is null or not
        if(firebaseUser!=null)
        {
            Intent i = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        login   = findViewById(R.id.loginButton);
        register = findViewById(R.id.registerButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(MainActivity2.this,LoginUserActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(MainActivity2.this,RegisterUserActivity.class));
            }
        });
    }
}
