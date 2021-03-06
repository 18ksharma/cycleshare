package com.example.cycleshare;

import android.app.Application;

import com.example.cycleshare.models.Comment;
import com.example.cycleshare.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        //Register parse models
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("kanika-myapp") // should correspond to APP_ID env variable
                .clientKey("CodePathMoveFast")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://kanika-myapp.herokuapp.com/parse/").build());

    }
}