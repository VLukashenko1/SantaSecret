package com.vital.santasecret.WorkWithDB;

import com.google.firebase.firestore.FieldValue;
import com.vital.santasecret.WorkWithDB.DbHelper;

import java.util.HashMap;

public class FriendsMover {
    DbHelper dbHelper = new DbHelper();

    public void addToFriends(String currentUserId, String friendId){
        dbHelper.USERS_REF.document(currentUserId).update("friends", FieldValue.arrayUnion(friendId));
        dbHelper.USERS_REF.document(friendId).update("friends", FieldValue.arrayUnion(currentUserId));

        dellUserFromFriendsRequest(currentUserId,friendId);
    }
    public void dellUserFromFriendsRequest(String uId, String frId){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("requestToFriends", FieldValue.arrayRemove(frId) );
        dbHelper.USERS_REF.document(uId).update(hashMap);

        //Видалити заявку в друга, якщо є
        HashMap<String,Object> frHashMap = new HashMap<>();
        frHashMap.put("requestToFriends", FieldValue.arrayRemove(uId) );
        dbHelper.USERS_REF.document(frId).update(frHashMap);
    }

    public void dellUserFromFriend(String uId, String frId){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("friends", FieldValue.arrayRemove(frId) );
        dbHelper.USERS_REF.document(uId).update(hashMap);

        HashMap<String,Object> frHashMap = new HashMap<>();
        frHashMap.put("friends", FieldValue.arrayRemove(uId) );
        dbHelper.USERS_REF.document(frId).update(frHashMap);
    }

}
