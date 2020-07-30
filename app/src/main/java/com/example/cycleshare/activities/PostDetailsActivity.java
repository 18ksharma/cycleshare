package com.example.cycleshare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.cycleshare.CommentsAdapter;
import com.example.cycleshare.PostsAdapter;
import com.example.cycleshare.R;
import com.example.cycleshare.Utils;
import com.example.cycleshare.fragments.ComposeFragment;
import com.example.cycleshare.models.Comment;
import com.example.cycleshare.models.Post;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private EditText etComment;
    private Button btnComment;
    private ImageView ivExpand;
    private ImageView ivCollapse;

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

    protected List<Comment> comments;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private String relativeDate;

    private double lat;
    private double lon;

    private RecyclerView rvComments;
    protected CommentsAdapter adapter;

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
        ivPostImage = (PhotoView)findViewById(R.id.ivPicture);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        btnContact = findViewById(R.id.btnContact);
        ivEdit = findViewById(R.id.ivEdit);
        ivDelete = findViewById(R.id.ivDelete);
        rvComments=findViewById(R.id.rvComments);
        ivExpand=findViewById(R.id.ivenlarge);
        btnComment=findViewById(R.id.btnpost);
        etComment=findViewById(R.id.etComment);
        ivCollapse=findViewById(R.id.ivCollapse);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        comments= new ArrayList<>();
        adapter=new CommentsAdapter(this, comments);

        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(linearLayoutManager);


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
        String parent = getIntent().getStringExtra("parent");

        querycommentbypost(post);

        if(username.equals(ParseUser.getCurrentUser().getUsername()) && (parent != "post")){
            ivDelete.setVisibility(View.VISIBLE);
            ivEdit.setVisibility(View.VISIBLE);
        }
        else{
            ivDelete.setVisibility(View.GONE);
            ivEdit.setVisibility(View.GONE);
        }
        ivCollapse.setVisibility(View.INVISIBLE);
        ivExpand.setVisibility(View.VISIBLE);



        relativeDate = Utils.getRelativeTimeAgo(createdAt.toString());

        tvUser.setText("@"+username);
        tvDescription.setText(description);
        tvTimestamp.setText(relativeDate);
        tvCondition.setText("Condition: " + condition);
        tvPrice.setText("Price: " + price);
        tvAvailability.setText("Availability: " + availability);
        Glide.with(this).load(image.getUrl()).placeholder(R.drawable.ic_baseline_person_24).into(ivPostImage);


        Drawable d = ivPostImage.getDrawable();
        ivPostImage.setImageDrawable(d);

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
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + email));
                intent.putExtra(Intent.EXTRA_SUBJECT, "CycleShare Bike Inquiry");
                startActivity(intent);
                finish();
            }
        });

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment =new ComposeFragment();
                Intent intent = new Intent(PostDetailsActivity.this, EditActivity.class);
                intent.putExtra("edit", "edit");
                intent.putExtra("post", post);
                startActivity(intent);

            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog diaBox = AskOption();
                diaBox.show();
            }
        });

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Comment comment = new Comment();
                comment.setContents(etComment.getText().toString());
                comment.setUser(ParseUser.getCurrentUser());
                comment.setPost(post);
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        Log.i("dets", "Comment saved");
                        etComment.setText("");
                        adapter.clear();
                        comments.clear();
                        adapter.addAll(comments);
                        querycommentbypost(post);
                        hideKeyboardFrom(PostDetailsActivity.this, view);
                    }
                });
            }
        });

        ivExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator moveRV = ObjectAnimator.ofFloat(rvComments, "translationY", 400f);
                moveRV.setDuration(1000);
                moveRV.start();

                RelativeLayout.LayoutParams lp =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 1400);
                rvComments.setLayoutParams(lp);

                ivPostImage.setVisibility(View.INVISIBLE);
                map.setVisibility(View.INVISIBLE);
                btnContact.setVisibility(View.INVISIBLE);
                ivExpand.setVisibility(View.INVISIBLE);
                ivCollapse.setVisibility(View.VISIBLE);
            }
        });
        ivCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ObjectAnimator moveRV = ObjectAnimator.ofFloat(rvComments, "translationY", 1300f);
                moveRV.setDuration(1000);
                moveRV.start();
                RelativeLayout.LayoutParams lp =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 300);
                rvComments.setLayoutParams(lp);

                ivPostImage.setVisibility(View.VISIBLE);
                map.setVisibility(View.VISIBLE);
                btnContact.setVisibility(View.VISIBLE);
                ivExpand.setVisibility(View.VISIBLE);
                ivCollapse.setVisibility(View.INVISIBLE);

            }
        });
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void querycommentbypost(Post post) {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_AUTHOR);
        //gets posts
        query.whereEqualTo(Comment.KEY_POST, post);
        query.setLimit(20);
        query.addDescendingOrder(Comment.KEY_CREATEDAT);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> postcomments, com.parse.ParseException e) {
                if(e!=null){
                    Log.e("PostDetailsActivity", "Issue with getting posts", e);
                    return ;
                }
                for(Comment comment: postcomments){
                    Log.i("dets", comment.getContents());
                }
                comments.addAll(postcomments);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this post?")
                .setIcon(R.drawable.ic_baseline_delete_24)

                .setPositiveButton("Delete this Post", new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int whichButton) {

                        try {
                            post.delete();
                            post.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    Toast.makeText(PostDetailsActivity.this, "Post deleted.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(PostDetailsActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            });
                        } catch (com.parse.ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(PostDetailsActivity.this, "Post could not be deleted", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
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