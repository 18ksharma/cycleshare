package com.example.cycleshare.models;

import android.webkit.GeolocationPermissions;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "bikePic";
    public static final String KEY_USER = "author";
    public static final String KEY_CREATEDAT = "createdAt";
    public static final String KEY_CONDITION = "condition";
    public static final String KEY_PRICE = "cost";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_POINT = "point";

    public String getAvailability() {
        return getString(KEY_AVAILABILITY);
    }

    public void setAvailability(String availability){
        put(KEY_AVAILABILITY, availability);
    }


    public Double getPrice() {
        return getDouble(KEY_PRICE);
    }

    public void setPrice(Double price){
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

    public void setLatitude(double lat){ put(KEY_LATITUDE, lat);}

    public double getLatitude(){ return getDouble(KEY_LATITUDE);}

    public void setLongitude(double lon){ put(KEY_LONGITUDE, lon);}

    public double getLongitude(){ return getDouble(KEY_LONGITUDE);}

    public ParseGeoPoint getPoint(){ return getParseGeoPoint(KEY_POINT);}

    public void setPoint(ParseGeoPoint point){ put(KEY_POINT, point);}
}
