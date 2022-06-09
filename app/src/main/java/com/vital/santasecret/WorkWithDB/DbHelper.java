package com.vital.santasecret.WorkWithDB;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DbHelper {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public final CollectionReference USERS_REF = db.collection("Users");
    public CollectionReference BOXES_REF = db.collection("Boxes");

    public CollectionReference BOX_MESSAGE = db.collection("Messages");
    public CollectionReference BOX_RESULT = db.collection("DrawResult");

    public static final String ID_OF_BOX_CREATOR = "idOfCreator";
    public static final String NICKNAME = "NickName";
    //public static final String ASK_TO_FRIENDS = "AskToFriends";
    //public static final String DISPLAY_NAME = "DisplayName";
    //public static final String EMAIL = "Email";
    //public static final String FRIENDS = "Friends";
    //public static final String PHOTO_URL = "PhotoUrl";
    //public static final String USER_ID = "Uid";

    public String currentUserID = auth.getUid();
}
