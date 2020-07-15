package com.example.cycleshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private TextView tvTimestamp;

    private String description;
    private ParseFile image;
    private Date createdAt;
    private String username;
    private ParseFile profilePic;
    private String condition;
    private String price;
    private String availability;

    private String relativeDate;



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
        tvTimestamp=findViewById(R.id.tvTimestamp);
        //TODO: figure out how to link the map

        description = getIntent().getStringExtra("description");
        image = (ParseFile) getIntent().getExtras().get("image");
        createdAt = (Date) getIntent().getExtras().get("timestamp");
        username = getIntent().getStringExtra("username");
        profilePic = (ParseFile) getIntent().getExtras().get("profilePic");
        condition = getIntent().getStringExtra("condition");
        price=getIntent().getStringExtra("price");
        availability=getIntent().getStringExtra("availability");

        relativeDate=getRelativeTimeAgo(createdAt.toString());

        tvUser.setText(username);
        tvDescription.setText(description);
        tvTimestamp.setText(relativeDate);
        tvCondition.setText(condition);
        tvPrice.setText(price);
        tvAvailability.setText(availability);
        Glide.with(this).load(image.getUrl()).placeholder(R.drawable.ic_baseline_person_24).into(ivPostImage);

        Glide.with(this).load(profilePic).transform(new CircleCrop())
                .placeholder(R.drawable.ic_baseline_person_24).into(ivProfilePicture);
    }

    //Gets Relative time
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}