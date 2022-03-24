package com.vital.santasecret.WorkWithDB;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DbHelper {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CollectionReference USERS_REF = db.collection("Users");
    //public static final String ASK_TO_FRIENDS = "AskToFriends";
    //public static final String DISPLAY_NAME = "DisplayName";
    //public static final String EMAIL = "Email";
    //public static final String FRIENDS = "Friends";
    //public static final String NICKNAME = "NickName";
    //public static final String PHOTO_URL = "PhotoUrl";
    //public static final String USER_ID = "Uid";

}
