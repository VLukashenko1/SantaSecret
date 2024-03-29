package com.vital.santasecret.Model;

import java.util.List;

public class Box {

    String idOfBox;
    String idOfCreator, NameOfBox;
    List<String> listOfUsers;

    public Box(String idOfCreator, String nameOfBox, List<String> listOfUsers, String idOfBox) {
        this.idOfBox = idOfBox;
        this.idOfCreator = idOfCreator;
        this.NameOfBox = nameOfBox;
        this.listOfUsers = listOfUsers;
    }

    public Box() {
    }

    public String getIdOfCreator() {
        return idOfCreator;
    }

    public void setIdOfCreator(String idOfCreator) {
        this.idOfCreator = idOfCreator;
    }

    public String getNameOfBox() {
        return NameOfBox;
    }

    public void setNameOfBox(String nameOfBox) {
        NameOfBox = nameOfBox;
    }

    public List<String> getListOfUsers() {
        return listOfUsers;
    }

    public void setListOfUsers(List<String> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }

    public String getIdOfBox() {
        return idOfBox;
    }

    public void setIdOfBox(String idOfBox) {
        this.idOfBox = idOfBox;
    }
}
