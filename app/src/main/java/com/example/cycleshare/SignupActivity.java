package com.example.cycleshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cycleshare.fragments.ComposeFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class SignupActivity extends AppCompatActivity {

    private EditText tvUsername;
    private EditText tvPassword;
    private EditText tvConfirm;
    private EditText tvEmail;
    private Button btnCreateAccount;
    private Button btnPicture;
    private Button btnGallery;
    private ImageView ivPreview;

    private ParseFile img;

    //Variable for image
    private static File photoFile;
    public static final String TAG = "ComposeFragment";
    public static String photoFileName = "photo.jpg";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    public final static int PICK_PHOTO_CODE = 1046;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        tvUsername = findViewById(R.id.tvUsername);
        tvPassword = findViewById(R.id.tvPassword);
        tvConfirm = findViewById(R.id.tvConfirm);
        tvEmail = findViewById(R.id.tvEmail);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnPicture = findViewById(R.id.btnPicture);
        btnGallery = findViewById(R.id.btnGallery);
        ivPreview = findViewById(R.id.ivPreview);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchgallery();
            }
        });


        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchcamera();
            }
        });

        //Sets onclicklistener for button
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String username=tvUsername.getText().toString();
                String password=tvPassword.getText().toString();
                String confirm=tvConfirm.getText().toString();
                String email=tvEmail.getText().toString();

                final ParseUser user = new ParseUser();

                //Checks if anything is invalid

                if(username.isEmpty()){
                    Toast.makeText(SignupActivity.this, "You must enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.isEmpty()){
                    Toast.makeText(SignupActivity.this, "You must have a password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(confirm)){
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Set core properties
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                if(ivPreview.getDrawable()!=null){
                    Drawable d = ivPreview.getDrawable();
                    Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] bitmapdata = stream.toByteArray();
                    final ParseFile img = new ParseFile(bitmapdata);
                    user.put("profilePic", img);
                    img.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            // Invoke signUpInBackground
                            signup(user);
                        }
                    });
                }
                else {
                    signup(user);
                }

            }
        });
    }

    private void signup(ParseUser user) {
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    Intent i = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(SignupActivity.this, "Failure to signup", Toast.LENGTH_SHORT).show();
                    Log.e("Signup", "error:"+e);
                }
            }
        });
    }

    private void launchgallery() {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(this.getPackageManager()) != null) {
                // Bring up gallery to select a photo
                startActivityForResult(intent, PICK_PHOTO_CODE);
            }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPreview.setImageBitmap(takenImage);
            }
        }
            if ((data != null) && requestCode == PICK_PHOTO_CODE) {
                if (resultCode == RESULT_OK) {
                    Uri photoUri = data.getData();

                    // Load the image located at photoUri into selectedImage
                    Bitmap selectedImage = loadFromUri(photoUri);

                    // Load the selected image into a preview
                    ImageView ivPrev = (ImageView) findViewById(R.id.ivPreview);
                    ivPreview.setImageBitmap(selectedImage);

                }
            }
            else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
    }

    private Bitmap loadFromUri(Uri photoUri) {

        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;

    }

    public void launchcamera() {

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

    public  File getPhotoFileUri(String photoFileName) {


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