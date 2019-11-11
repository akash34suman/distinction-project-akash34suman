package com.swin.edu.au.chatbox.Model;

public class User {



    String userID;
    String userName;
    String imgURL;
    String search;







    public User()
    {

    }

    public  User(String userID, String userName, String imgURL, String search)
    {
        this.userID= userID;
        this.userName=userName;
        this.imgURL=imgURL;
        this.search=search;
    }



    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

}
