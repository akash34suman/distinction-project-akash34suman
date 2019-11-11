package com.swin.edu.au.chatbox.Model;

public class ChatList {
    String userID;



    public ChatList()
    {

    }

    public  ChatList(String userID)
    {
        this.userID=userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
