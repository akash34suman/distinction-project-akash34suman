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
import com.swin.edu.au.chatbox.Model.Chat;
import com.swin.edu.au.chatbox.Model.User;
import com.swin.edu.au.chatbox.MsgActivity;
import com.swin.edu.au.chatbox.R;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {


    public  static  final int MSG_LEFT=0;
    public  static  final int MSG_RIGHT=1;

    private Context ctx;
    private List<Chat> chats;
    private String imgURL;
    FirebaseUser firebaseUser;
    Chat chat;

    public  MsgAdapter(Context ctx, List<Chat> chats,String imgURL)
    {
        this.ctx=ctx;
        this.chats=chats;
        this.imgURL=imgURL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==MSG_RIGHT)
        {
            View v = LayoutInflater.from(ctx).inflate(R.layout.chat_right_view,parent,false);

            return new MsgAdapter.ViewHolder(v);
        }
        else
        {
            View v = LayoutInflater.from(ctx).inflate(R.layout.chat_left_view,parent,false);

            return new MsgAdapter.ViewHolder(v);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull MsgAdapter.ViewHolder holder, int position) {

        chat=chats.get(position);
        holder.show_msg.setText(chat.getMsg());

            if(imgURL.equals("default"))
            {
                holder.pf_img.setImageResource(R.mipmap.ic_launcher);

            }
            else
            {
                Glide.with(ctx).load(imgURL).into(holder.pf_img);
            }



    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView show_msg;
        public ImageView pf_img;
        public  ViewHolder(View v)
        {
            super(v);

            show_msg=v.findViewById(R.id.show_msg);
            pf_img=v.findViewById(R.id.pf_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chats.get(position).getSender().equals(firebaseUser.getUid()))
        {

            return MSG_RIGHT;
        }
        else
        {
            return MSG_LEFT;
        }

    }
}
