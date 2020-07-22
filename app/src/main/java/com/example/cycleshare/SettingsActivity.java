package com.example.cycleshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;

public class SettingsActivity extends AppCompatActivity {
    private Button btnCamera;
    private Button btnGallery;
    private Button btnLogout;
    private Button btnChangeUsername;
    private EditText etnewUser;
    private TextView tvUsername;
    private ImageView ivProfilePicture;
    ParseUser user;

    private ParseFile img;

    //Variable for image
    private File photoFile;
    public static final String TAG = "ComposeFragment";
    public String photoFileName = "photo.jpg";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    public final static int PICK_PHOTO_CODE = 1046;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        user=ParseUser.getCurrentUser();
        btnCamera=findViewById(R.id.btncamera);
        btnGallery=findViewById(R.id.btngallery);
        btnLogout=findViewById(R.id.btnLogout);
        btnChangeUsername=findViewById(R.id.btnChangeUsername);
        etnewUser=findViewById(R.id.etnewuser);
        tvUsername=findViewById(R.id.etUsername);
        ivProfilePicture=findViewById(R.id.ivProfilePicture);

        //tvUsername.setText(user.getUsername());

        if(user.getParseFile("profilePic")!=null) {
            Glide.with(this).load(user.getParseFile("profilePic").getUrl())
                    .placeholder(R.drawable.ic_baseline_person_24).into(ivProfilePicture);
        }
        else{
            Glide.with(this).load("http://img.freepik.com/free-vector/abstract-geometric-lines-seamless-pattern_144290-8.jpg?size=626&ext=jpg").into(ivProfilePicture);
        }

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchcamera();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                if(currentUser == null){
                    Intent i = new Intent(SettingsActivity.this, InitialActivity.class);
                    startActivity(i);
                }

            }
        });

        btnChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUser = etnewUser.getText().toString();
                if(!newUser.isEmpty()){
                    user.setUsername(etnewUser.getText().toString());
                    tvUsername.setText(etnewUser.getText().toString());
                    etnewUser.setText("");
                }
            }
        });

    }

    private void launchcamera() {


        // Intent to open camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for accessing the image
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(this, "com.example.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            // Starts image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String photoFileName) {


        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);

        return file;
    }
}