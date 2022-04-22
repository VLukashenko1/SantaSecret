package com.vital.santasecret.Model;

import java.util.List;

public class User {
    String displayName, email, uId;
    String photoUrl;
    List<String> requestToFriends;
    List<String> friends;

    String nickName;

    public User(){

    }
    public User(String displayName, String email, String uId, String photoUrl, List<String> requestToFriends, List<String> friends, String nickName) {
        this.displayName = displayName;
        this.email = email;
        this.uId = uId;
        this.photoUrl = photoUrl;
        this.requestToFriends = requestToFriends;
        this.friends = friends;
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

    public List<String> getRequestToFriends() {
        return requestToFriends;
    }

    public List<String> getFriends() {
        return friends;
    }

    public String getNickName() {
        return nickName;
    }


}
