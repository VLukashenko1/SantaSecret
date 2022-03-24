package com.vital.santasecret.Model;

public class User {
    String displayName, email, uId;
    String photoUrl;
    String[] requestToFriends;
    String[] Friends;

    String nickName;

    public User(){}
    public User(String displayName, String email, String uId, String photoUrl, String[] requestToFriends, String[] friends, String nickName) {
        this.displayName = displayName;
        this.email = email;
        this.uId = uId;
        this.photoUrl = photoUrl;
        this.requestToFriends = requestToFriends;
        Friends = friends;
        this.nickName = nickName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getuId() {
        return uId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String[] getRequestToFriends() {
        return requestToFriends;
    }

    public String[] getFriends() {
        return Friends;
    }

    public String getNickName() {
        return nickName;
    }


}
