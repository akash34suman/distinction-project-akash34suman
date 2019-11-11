package com.swin.edu.au.chatbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginUserActivity extends AppCompatActivity {
    Toolbar tbar;
    MaterialEditText  email,pwd;
    Button login;
    FirebaseAuth auth;
    String emailtxt,pwdtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        tbar= findViewById(R.id.toolbar);
        setSupportActionBar(tbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        email = findViewById(R.id.emailEdt);
        pwd = findViewById(R.id.pwdEdt);
        auth= FirebaseAuth.getInstance();
        login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailtxt =  email.getText().toString();
                pwdtxt = pwd.getText().toString();
                if(TextUtils.isEmpty(emailtxt)|| TextUtils.isEmpty(pwdtxt))
                {
                    Toast.makeText(LoginUserActivity.this, "All fields are required to fill!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    auth.signInWithEmailAndPassword(emailtxt,pwdtxt)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful())
                                    {
                                        Intent i = new Intent(LoginUserActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(LoginUserActivity.this, "Invalid Username or Password!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });



    }
}
