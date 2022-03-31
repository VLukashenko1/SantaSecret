package com.vital.santasecret.Model;

public class Box {
    String idOfCreator, NameOfBox;
    String[] listOfUsers;

    public Box(String idOfCreator, String nameOfBox, String[] listOfUsers) {
        this.idOfCreator = idOfCreator;
        NameOfBox = nameOfBox;
        this.listOfUsers = listOfUsers;
    }
    public Box(){}

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

    public String[] getListOfUsers() {
        return listOfUsers;
    }

    public void setListOfUsers(String[] listOfUsers) {
        this.listOfUsers = listOfUsers;
    }
}
