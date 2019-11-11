package com.swin.edu.au.chatbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterUserActivity extends AppCompatActivity {
    MaterialEditText userName, email,pwd;
    Button register;
    FirebaseAuth auth;
    DatabaseReference ref;
    Toolbar tbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        tbar= findViewById(R.id.toolbar);
        setSupportActionBar(tbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        userName= findViewById(R.id.usernameEdt);
        email= findViewById(R.id.emailEdt);
        pwd= findViewById(R.id.pwdEdt);
        register = findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userTxt =userName.getText().toString();
                String emailTxt =email.getText().toString();
                String pwdTxt =pwd.getText().toString();

                if(TextUtils.isEmpty(userTxt)|| TextUtils.isEmpty(emailTxt) || TextUtils.isEmpty(pwdTxt))
                {
                    Toast.makeText(RegisterUserActivity.this,"All fields are required to fill!!",Toast.LENGTH_SHORT).show();
                }
                else  if (pwdTxt.length() <6)
                {
                    Toast.makeText(RegisterUserActivity.this,"Password must be atleat 6 characters!!",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    registerUser(userTxt,emailTxt,pwdTxt);
                }

            }
        });
        auth =  FirebaseAuth.getInstance();


    }

    private  void registerUser(final String userName, final String email, final String pwd)
    {
        auth.createUserWithEmailAndPassword(email,pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public  void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userID = firebaseUser.getUid();
                            ref= FirebaseDatabase.getInstance().getReference("Users").child(userID);

                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("userID",userID);
                            hashMap.put("userName",userName);
                            hashMap.put("pwd",pwd);
                            hashMap.put("email",email);
                            hashMap.put("imgURL","default");
                            hashMap.put("search",userName.toLowerCase());

                            ref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Intent i = new Intent(RegisterUserActivity.this,MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(RegisterUserActivity.this,"Enter valid user name,email and password",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
