package com.example.cycleshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.cycleshare.fragments.ComposeFragment;
import com.example.cycleshare.models.Post;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageView ivProfilePicture;
    private TextView tvUser;
    private TextView tvCondition;
    private TextView tvPrice;
    private TextView tvAvailability;
    private TextView tvDescription;
    private PhotoView ivPostImage;
    private MapView map;
    private GoogleMap gMap;
    private Button btnContact;
    private TextView tvTimestamp;
    private ImageView ivDelete;
    private ImageView ivEdit;

    private String description;
    private ParseFile image;
    private Date createdAt;
    private String username;
    private ParseFile profilePic;
    private String condition;
    private String price;
    private String availability;
    private String email;
    private ParseUser user;
    public Post post;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private String relativeDate;

    private double lat;
    private double lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        ivProfilePicture = findViewById(R.id.ivProfilePic);
        tvUser = findViewById(R.id.tvUser);
        tvCondition = findViewById(R.id.tvCondition);
        tvPrice = findViewById(R.id.tvPrice);
        tvAvailability = findViewById(R.id.tvAvailability);
        tvDescription = findViewById(R.id.tvDescription);
        ivPostImage = (PhotoView) findViewById(R.id.ivPicture);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        btnContact = findViewById(R.id.btnContact);
        ivEdit = findViewById(R.id.ivEdit);
        ivDelete = findViewById(R.id.ivDelete);

        Bundle mapViewBundle=null;

        if (savedInstanceState!=null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        map = findViewById(R.id.mapView);
        map.onCreate(mapViewBundle);
        map.getMapAsync(this);
        //TODO: figure out how to link the map

        description = getIntent().getStringExtra("description");
        image = (ParseFile) getIntent().getExtras().get("image");
        createdAt = (Date) getIntent().getExtras().get("timestamp");
        username = getIntent().getStringExtra("username");
        profilePic = (ParseFile) getIntent().getExtras().get("profilePic");
        condition = getIntent().getStringExtra("condition");
        price = getIntent().getStringExtra("price");
        availability = getIntent().getStringExtra("availability");
        email = getIntent().getStringExtra("email");
        lat = getIntent().getExtras().getDouble("latitude");
        lon = getIntent().getExtras().getDouble("longitude");
        user = (ParseUser) getIntent().getExtras().get("user");
        post = (Post) getIntent().getExtras().get("post");

        if(username.equals(ParseUser.getCurrentUser().getUsername())){
            ivDelete.setVisibility(View.VISIBLE);
            ivEdit.setVisibility(View.VISIBLE);

        }
        else{
            ivDelete.setVisibility(View.GONE);
            ivEdit.setVisibility(View.GONE);
        }



        relativeDate = getRelativeTimeAgo(createdAt.toString());

        tvUser.setText("@"+username);
        tvDescription.setText(description);
        tvTimestamp.setText(relativeDate);
        tvCondition.setText("Condition: " + condition);
        tvPrice.setText("Price: " + price);
        tvAvailability.setText("Availability: " + availability);
        Glide.with(this).load(image.getUrl()).placeholder(R.drawable.ic_baseline_person_24).into(ivPostImage);


        Drawable d = ivPostImage.getDrawable();
        ivPostImage.setImageDrawable(d);
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(ivPostImage);

        if(profilePic!=null){
            Glide.with(this).load(profilePic.getUrl()).transform(new CircleCrop())
                    .placeholder(R.drawable.ic_baseline_person_24).into(ivProfilePicture);
        }
        else{
            Glide.with(this).load("http://img.freepik.com/free-vector/abstract-geometric-lines-seamless-pattern_144290-8.jpg?size=626&ext=jpg")
                    .transform(new CircleCrop()).into(ivProfilePicture);
        }

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent to open mail app
                //Intent intent = new Intent(Intent.ACTION_MAIN);
                //intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + email));
                intent.putExtra(Intent.EXTRA_SUBJECT, "CycleShare Bike Inquiry");
                //intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                startActivity(intent);
                finish();
            }
        });

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(PostDetailsActivity.this, ComposeFragment.class);
                intent.putExtra("post", post);
                startActivity(intent);*/

            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle=outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if(mapViewBundle==null){
            mapViewBundle= new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        map.onSaveInstanceState(mapViewBundle);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title("marker"));
        dropPinEffect(marker);
        LatLng latLng = new LatLng(lat, lon);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
    }

    private void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 15ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 15);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        map.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }


    @Override
    public void onPause() {
        map.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        map.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }
}