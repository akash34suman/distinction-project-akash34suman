package com.swin.edu.au.chatbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swin.edu.au.chatbox.AdapterClass.MsgAdapter;
import com.swin.edu.au.chatbox.Model.Chat;
import com.swin.edu.au.chatbox.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MsgActivity extends AppCompatActivity {
    CircleImageView pf_image;
    TextView userName;
    FirebaseUser firebaseUser;
    DatabaseReference ref;
    Intent i;
    Toolbar tbar;
    ImageButton sendButton;
    EditText sendText;
    MsgAdapter msgAdapter;
    List<Chat> chats;
    RecyclerView recyclerView;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        tbar= findViewById(R.id.toolbar);
        setSupportActionBar(tbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView=findViewById(R.id.msg_reycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        pf_image= findViewById(R.id.pf_image);
        userName = findViewById(R.id.userName);
        sendText=findViewById(R.id.sendTxt);
        sendButton=findViewById(R.id.sendButton);


        i = getIntent();
        userID=i.getStringExtra("userID");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=sendText.getText().toString();
                if(!msg.equals(""))
                {
                    sendMessage(firebaseUser.getUid(),userID,msg);
                }
                else
                {
                    Toast.makeText(MsgActivity.this, "Enter something to send!!", Toast.LENGTH_SHORT).show();
                }
                sendText.setText("");
            }
        });
        ref= FirebaseDatabase.getInstance().getReference("Users").child(userID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getUserName());

                if(user.getImgURL().equals("default"))
                {
                    pf_image.setImageResource(R.mipmap.ic_launcher);
                }
                else
                {
                    Glide.with(MsgActivity.this).load(user.getImgURL()).into(pf_image);
                }
                readMsg(firebaseUser.getUid(),userID,user.getImgURL());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private  void   sendMessage(String sender, String receiver,String message)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("msg",message);

        ref.child("Chat").push().setValue(hashMap);


//adding user to chat list/fragment
        final DatabaseReference chatref= FirebaseDatabase.getInstance().getReference("ChatList")
                .child(firebaseUser.getUid()).child(userID);

        chatref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    chatref.child("userID").setValue(userID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }

    private  void  readMsg(final String myID, final String userID, final String imgURL)
    {

        chats= new ArrayList<>();
        ref=FirebaseDatabase.getInstance().getReference("Chat");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getReceiver().equals(myID) && chat.getSender().equals(userID)
                            || chat.getReceiver().equals(userID) && chat.getSender().equals(myID))
                    {
                        chats.add(chat);
                    }
                    msgAdapter= new MsgAdapter(MsgActivity.this,chats,imgURL);
                    recyclerView.setAdapter(msgAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
