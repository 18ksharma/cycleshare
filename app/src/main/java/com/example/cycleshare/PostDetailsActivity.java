package com.example.cycleshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

public class PostDetailsActivity extends AppCompatActivity {
    private ImageView ivProfilePicture;
    private TextView tvUser;
    private TextView tvCondition;
    private TextView tvPrice;
    private TextView tvAvailability;
    private TextView tvDescription;
    private ImageView ivPostImage;
    private GoogleMap map;
    private Button btnContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        ivProfilePicture=findViewById(R.id.ivProfilePic);
        tvUser=findViewById(R.id.tvUser);
        tvCondition=findViewById(R.id.tvCondition);
        tvPrice=findViewById(R.id.tvPrice);
        tvAvailability=findViewById(R.id.tvAvailability);
        tvDescription=findViewById(R.id.tvDescription);
        ivPostImage=findViewById(R.id.ivPicture);
        //TODO: figure out how to link the map
    }
}