package com.example.cycleshare.models;

import android.webkit.GeolocationPermissions;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "bikePic";
    public static final String KEY_USER = "author";
    public static final String KEY_CREATEDAT = "createdAt";
    public static final String KEY_CONDITION = "condition";
    public static final String KEY_PRICE = "price";

    public String getPrice() {
        return getString(KEY_PRICE);
    }

    public void setPrice(String price){
        put(KEY_PRICE, price);
    }

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public String getCondition(){
        return getString(KEY_CONDITION);
    }

    public void setCondition(String condition){
        put(KEY_CONDITION, condition);
    }
}
