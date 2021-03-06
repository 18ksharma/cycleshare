package com.example.cycleshare.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.cycleshare.R;
import com.example.cycleshare.Utils;
import com.example.cycleshare.models.Comment;
import com.example.cycleshare.models.Post;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private Button btnCamera;
    private Button btnGallery;
    private Button btnLogout;
    private Button btnChangeUsername;
    private EditText etnewUser;
    private TextView tvUsername;
    private ImageView ivProfilePicture;
    ParseUser user;
    private Button btnUpdate;
    private Button btnDelete;

    private ParseFile img;

    private EditText etcurPass;
    private EditText etcurPassConfirm;
    private EditText etnewPass;
    private EditText etnewPassConfirm;

    //Variable for image
    private File photoFile;
    public static final String TAG = "ComposeFragment";
    public String photoFileName = "photo.jpg";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    public final static int PICK_PHOTO_CODE = 1046;

    private ToggleButton darkmode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_logo_round);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        user=ParseUser.getCurrentUser();
        btnCamera=findViewById(R.id.btncamera);
        btnGallery=findViewById(R.id.btngallery);
        btnLogout=findViewById(R.id.btnLogout);
        etnewUser=findViewById(R.id.etnewuser);
        tvUsername=findViewById(R.id.tvUsername);
        ivProfilePicture=findViewById(R.id.ivProfilePicture);
        btnUpdate=findViewById(R.id.btnupdate);
        btnDelete=findViewById(R.id.btnDelete);
        etcurPass=findViewById(R.id.etcurPass);
        etcurPassConfirm=findViewById(R.id.etconfirmcurPass);
        etnewPass=findViewById(R.id.etnewPass);
        etnewPassConfirm=findViewById(R.id.etconfirmnewPass);

        tvUsername.setText(user.getUsername().toString());

        if(user.getParseFile("profilePic")!=null) {
            Glide.with(this).load(user.getParseFile("profilePic").getUrl())
                    .transform(new CircleCrop()).placeholder(R.drawable.ic_baseline_person_24).into(ivProfilePicture);
        }
        else{
            Glide.with(this)
                    .load("http://img.freepik.com/free-vector/abstract-geometric-lines-seamless-pattern_144290-8.jpg?size=626&ext=jpg")
                    .transform(new CircleCrop()).placeholder(R.drawable.ic_baseline_person_24).into(ivProfilePicture);
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
                launchgallery();
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


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUser = etnewUser.getText().toString();
                if(!newUser.isEmpty()){
                    user.setUsername(etnewUser.getText().toString());
                    tvUsername.setText(etnewUser.getText().toString());
                    etnewUser.setText("");
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(SettingsActivity.this, "Your username has been changed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if(etcurPass.getText().toString().equals(etcurPassConfirm.getText().toString())){
                    Log.i(TAG, "Passwords match and matches user password");

                    if(!etnewPass.getText().toString().equals("")
                            && etnewPass.getText().toString().equals(etnewPassConfirm.getText().toString())){
                        user.setPassword(etnewPass.getText().toString());
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.i(TAG, "password changed");
                                ParseUser.logOut();
                                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                                if(currentUser == null){
                                    Intent i = new Intent(SettingsActivity.this, InitialActivity.class);
                                    startActivity(i);
                                }

                            }
                        });
                    }
                }
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog diaBox = AskOption();
                diaBox.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_darkmode, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.darkmode:

                darkmode = (ToggleButton) findViewById(R.id.darkmode);
                darkmode.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (((ToggleButton) v).isChecked()) {
                            Log.i("Settings", "darkmode on");
                            AppCompatDelegate
                                    .setDefaultNightMode(
                                            AppCompatDelegate
                                                    .MODE_NIGHT_YES);
                        }
                        else {
                            AppCompatDelegate
                                    .setDefaultNightMode(
                                            AppCompatDelegate
                                                    .MODE_NIGHT_NO);
                        }

                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }

    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this account? This action cannot be undone.")
                .setIcon(R.drawable.ic_baseline_delete_24)

                .setPositiveButton("Delete Account", new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int whichButton) {
                        querycommentsbyuser();

                        querypostsbyuser();
                        try {
                            user.delete();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(SettingsActivity.this, "Error deleting account", Toast.LENGTH_SHORT).show();
                        }
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                ParseUser.logOut();
                                Toast.makeText(SettingsActivity.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SettingsActivity.this, InitialActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });

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

    private void querycommentsbyuser() {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_AUTHOR);
        //gets posts
        query.whereEqualTo(Comment.KEY_AUTHOR, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addAscendingOrder(Comment.KEY_CREATEDAT);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> postcomments, com.parse.ParseException e) {
                if(e!=null){
                    Log.e("PostDetailsActivity", "Issue with getting posts", e);
                    return ;
                }
                for(Comment comment: postcomments){
                    try {
                        comment.delete();
                        comment.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(SettingsActivity.this, "Deleted comments", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch (ParseException ex){
                        ex.printStackTrace();
                        Toast.makeText(SettingsActivity.this, "Error deleting comments", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, String.valueOf(ex));
                    }
                }
            }
        });
    }

    private void querypostsbyuser() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return ;
                }
                for(Post post: posts){
                    Log.i(TAG, post.getDescription());
                    try {
                        post.delete();
                        post.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(SettingsActivity.this, "Posts deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        Toast.makeText(SettingsActivity.this, "Error deleting post", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, String.valueOf(ex));
                    }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivProfilePicture.setImageBitmap(takenImage);
            }
        }
        if ((data != null) && requestCode == PICK_PHOTO_CODE) {
            if (resultCode == RESULT_OK) {
                Uri photoUri = data.getData();

                // Load the image located at photoUri into selectedImage
                Bitmap selectedImage = loadFromUri(photoUri);

                // Load the selected image into a preview
                ImageView ivPrev = (ImageView) findViewById(R.id.ivProfilePicture);
                ivPrev.setImageBitmap(selectedImage);

            }
        }
        Drawable d = ivProfilePicture.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        final ParseFile img = new ParseFile(bitmapdata);
        user.put("profilePic", img);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(SettingsActivity.this, "Profile Picture has been changed", Toast.LENGTH_SHORT).show();
            }
        });
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