package com.swin.edu.au.chatbox.AdapterClass;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swin.edu.au.chatbox.Model.Chat;
import com.swin.edu.au.chatbox.Model.User;
import com.swin.edu.au.chatbox.MsgActivity;
import com.swin.edu.au.chatbox.R;

import java.util.List;

import javax.microedition.khronos.opengles.GL;

public class UserFragmentAdapter extends RecyclerView.Adapter<UserFragmentAdapter.ViewHolder> {

    private Context ctx;
    private List<User> users;
    String lastMsg;
    boolean chatExist;


    public  UserFragmentAdapter(Context ctx, List<User> users, Boolean chatExist)
    {
        this.ctx=ctx;
        this.users=users;
        this.chatExist=chatExist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(ctx).inflate(R.layout.user_view_layout,parent,false);

        return new UserFragmentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserFragmentAdapter.ViewHolder holder, int position) {


        final User user= users.get(position);
        holder.userText.setText(user.getUserName());

        if(user.getImgURL().equals("default"))
        {
            holder.pf_img.setImageResource(R.mipmap.ic_launcher);

        }
        else
        {
            Glide.with(ctx).load(user.getImgURL()).into(holder.pf_img);
        }

        if(chatExist)
        {
            lastMsg(user.getUserID(), holder.lastTextMsg);

        }
        else
        {
            holder.lastTextMsg.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx,MsgActivity.class);
                i.putExtra("userID",user.getUserID());
                ctx.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userText;
        public TextView lastTextMsg;

        public ImageView pf_img;
        public  ViewHolder(View v)
        {
            super(v);

            userText=v.findViewById(R.id.userName);
            pf_img=v.findViewById(R.id.pf_image);
            lastTextMsg=v.findViewById(R.id.lastMsg);

        }
    }
//checking and showing last msg
    private  void  lastMsg(final String userID, final TextView lastTextMsg)
    {
        lastMsg="default";

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Chat");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            for(DataSnapshot snapshot:dataSnapshot.getChildren())
            {
                Chat chat = snapshot.getValue(Chat.class);

                        if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userID) ||

                                chat.getReceiver().equals(userID) && chat.getSender().equals(firebaseUser.getUid()))
                {
                    lastMsg=chat.getMsg();
                }
            }

            switch (lastMsg)
            {

                case "default":
                lastTextMsg.setText("");
                break;

                default:
                    lastTextMsg.setText(lastMsg);
                    break;

            }

            lastMsg="default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
