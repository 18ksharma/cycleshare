package com.example.cycleshare.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cycleshare.R;
import com.example.cycleshare.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class EditActivity extends AppCompatActivity {
    private EditText etDescription;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnSubmit;
    private EditText etPrice;
    private EditText etAvailability;
    private Button btnChoose;
    private Spinner sConditions;

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    //Variable for image
    private File photoFile;
    public String photoFileName = "photo.jpg";

    public final static int PICK_PHOTO_CODE = 1046;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_compose);
        etDescription = findViewById(R.id.etDescription);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        ivPostImage = findViewById(R.id.ivPostImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        etPrice = findViewById(R.id.etPrice);
        etAvailability = findViewById(R.id.etAvailability);
        btnChoose = findViewById(R.id.btnChoose);
        sConditions = findViewById(R.id.sConditions);

        btnSubmit.setText("Update");

        final Post post = (Post) getIntent().getExtras().get("post");

        etAvailability.setText(post.getAvailability());
        etDescription.setText(post.getDescription());
        etPrice.setText(post.getPrice().toString());
        Glide.with(this).load(post.getImage().getUrl()).placeholder(R.drawable.ic_baseline_person_24).into(ivPostImage);

        String[] items = new String[]{"New", "Excellent", "Good", "Fair", "Poor"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                items);
        sConditions.setAdapter(adapter);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchcamera();
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchgallery();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = etDescription.getText().toString();
                String price = etPrice.getText().toString();
                String availability = etAvailability.getText().toString();
                String condition = sConditions.getSelectedItem().toString();
                Drawable d = ivPostImage.getDrawable();
                Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();
                final ParseFile img = new ParseFile(bitmapdata);

                //Description cannot be empty
                if (description.isEmpty()) {
                    Toast.makeText(EditActivity.this, "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (price.isEmpty()) {
                    Toast.makeText(EditActivity.this, "Price cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (availability.isEmpty()) {
                    Toast.makeText(EditActivity.this, "Availability cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (ivPostImage.getDrawable() == null) {
                    Toast.makeText(EditActivity.this, "There is no image!", Toast.LENGTH_SHORT).show();
                    return;
                }


                //If description is valid
                ParseUser currentUser = ParseUser.getCurrentUser();

                savePost(post, Double.valueOf(price), availability, description, currentUser, img);
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

    private void savePost(Post post, Double price, String availability, String description, ParseUser currentUser, ParseFile img) {
        post.setDescription(description);
        post.setAvailability(availability);
        post.setPrice(price);
        post.setImage(img);
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(EditActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                    return;
                }
                etDescription.setText("");
                etPrice.setText("");
                etAvailability.setText("");
                ivPostImage.setImageResource(0);
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
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
                ivPostImage.setImageBitmap(takenImage);
            }
        }
        if ((data != null) && requestCode == PICK_PHOTO_CODE) {
            if (resultCode == RESULT_OK) {
                Uri photoUri = data.getData();

                // Load the image located at photoUri into selectedImage
                Bitmap selectedImage = loadFromUri(photoUri);

                // Load the selected image into a preview
                ImageView ivPreview = (ImageView) findViewById(R.id.ivPostImage);
                ivPreview.setImageBitmap(selectedImage);

            }
        }
    }

    private File getPhotoFileUri(String fileName) {

        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "EditActivity");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("EditActivity", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    public Bitmap loadFromUri(Uri photoUri) {
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
}