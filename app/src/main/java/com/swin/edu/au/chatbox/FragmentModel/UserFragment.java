package com.swin.edu.au.chatbox.FragmentModel;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.swin.edu.au.chatbox.AdapterClass.UserFragmentAdapter;
import com.swin.edu.au.chatbox.Model.User;
import com.swin.edu.au.chatbox.R;

import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserFragmentAdapter userFragmentAdapter;
    private List<User> users;
    EditText  searchUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_user, container, false);


        recyclerView = v.findViewById(R.id.user_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        users=new ArrayList<>();
        getUsers();


        searchUser=v.findViewById(R.id.searchUser);
        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                serchUsers(charSequence.toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return v;
    }

    private void serchUsers(String text) {

        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Query query= FirebaseDatabase.getInstance().getReference("Users").orderByChild("userName")
                .startAt(text)
                .endAt(text+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();

                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User user =snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;
                    if(!user.getUserID().equals(firebaseUser.getUid()))
                    {
                        users.add(user);
                    }
                }

                userFragmentAdapter= new UserFragmentAdapter(getContext(),users,false);
                recyclerView.setAdapter(userFragmentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
         ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(searchUser.getText().toString().equals("")) {
                    users.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.i("msg", "" + snapshot);

                        User user = snapshot.getValue(User.class);
                        //not include the login user

                        assert user != null;
                        assert firebaseUser != null;
                        if (!user.getUserID().equals(firebaseUser.getUid())) {
                            users.add(user);

                        }


                        userFragmentAdapter = new UserFragmentAdapter(getContext(), users, false);
                        recyclerView.setAdapter(userFragmentAdapter);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
