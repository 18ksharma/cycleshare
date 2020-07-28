package com.example.cycleshare.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String KEY_CONTENTS = "contents";
    public static final String KEY_USER = "author";
    public static final String KEY_POST = "post";
    public static final String KEY_CREATEDAT = "createdAt";

    public String getContents(){
        return getString(KEY_CONTENTS);
    }

    public void setContents(String description){
        put(KEY_CONTENTS, description);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public Post getPost(){
        return (Post) get(KEY_POST);
    }
    public void setPost(Post post){
        put(KEY_POST, post);
    }


}
