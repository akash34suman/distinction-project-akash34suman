package com.swin.edu.au.chatbox.FragmentModel;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swin.edu.au.chatbox.AdapterClass.UserFragmentAdapter;
import com.swin.edu.au.chatbox.Model.Chat;
import com.swin.edu.au.chatbox.Model.ChatList;
import com.swin.edu.au.chatbox.Model.User;
import com.swin.edu.au.chatbox.R;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserFragmentAdapter userFragmentAdapter;
    private List<User> users;
    FirebaseUser firebaseUser;
    DatabaseReference ref;
    private  List<ChatList> userList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

   View v= inflater.inflate(R.layout.fragment_chat, container, false);

   recyclerView=v.findViewById(R.id.chat_recycler_view);
   recyclerView.setHasFixedSize(true);
   recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

   firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
   userList=new ArrayList<>();


   ref=FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());
   ref.addValueEventListener(new ValueEventListener() {
       @Override
       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

           userList.clear();

           for(DataSnapshot snapshot:dataSnapshot.getChildren())
           {
               ChatList chatList = snapshot.getValue(ChatList.class);
               userList.add(chatList);
           }

           getChatList();

       }

       @Override
       public void onCancelled(@NonNull DatabaseError databaseError) {

       }
   });
//   ref= FirebaseDatabase.getInstance().getReference("Chat");
//   ref.addValueEventListener(new ValueEventListener() {
//       @Override
//       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//          userList.clear();
//
//          for(DataSnapshot snapshot:dataSnapshot.getChildren())
//          {
//
//              Chat chat=snapshot.getValue(Chat.class);
//              if(chat.getSender().equals(firebaseUser.getUid()))
//              {
//                  userList.add(chat.getReceiver());
//              }
//
//              if(chat.getReceiver().equals(firebaseUser.getUid()))
//              {
//                  userList.add(chat.getSender());
//              }
//          }
//            getChat();
//
//       }
//
//       @Override
//       public void onCancelled(@NonNull DatabaseError databaseError) {
//
//       }
//   });





   return  v;

    }

    private void getChatList() {


        users =new ArrayList<>();

        ref =FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                users.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                        User user = snapshot.getValue(User.class);
                        for(ChatList chatList:userList)
                        {
                            if (user.getUserID().equals(chatList.getUserID()))
                            {
                                users.add(user);
                            }
                        }
                }
                userFragmentAdapter=new UserFragmentAdapter(getContext(),users,true);
                recyclerView.setAdapter(userFragmentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    private  void getChat()
//    {
//        users=new ArrayList<>();
//        ref=FirebaseDatabase.getInstance().getReference("Users");
//
//        ref.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//                users.clear();
//                //1 user display from chat
//                for (DataSnapshot snapshot:dataSnapshot.getChildren())
//                {
//                    User user=snapshot.getValue(User.class);
//                    for (String userID:userList)
//                    {
//                        if(user.getUserID().equals(userID))
//                         {
//                            if(users.size() != 0)
//                            {
//                                for(User user1: users)
//                                {
//                                    if(!user.getUserID().equals(user1.getUserID()))
//                                    {
//                                        users.add(user);
//                                    }
//                                }
//                            }
//                            else
//                            {
//                                users.add(user);
//                            }
//                        }
//                    }
//                }
//                userFragmentAdapter=new UserFragmentAdapter(getContext(),users);
//                recyclerView.setAdapter(userFragmentAdapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }









}
