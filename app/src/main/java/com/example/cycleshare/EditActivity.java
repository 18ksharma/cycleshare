package com.example.cycleshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cycleshare.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class EditActivity extends AppCompatActivity {
    private EditText etDescription;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnSubmit;
    private EditText etPrice;
    private EditText etAvailability;
    private Button btnChoose;
    private Spinner sConditions;

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
        etPrice.setText(post.getPrice());
        Glide.with(this).load(post.getImage().getUrl()).placeholder(R.drawable.ic_baseline_person_24).into(ivPostImage);

        String[] items = new String[]{"New", "Excellent", "Good", "Fair", "Poor"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                items);
        sConditions.setAdapter(adapter);

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

                savePost(post, price, availability, description, currentUser, img);
            }
        });


    }

    private void savePost(Post post, String price, String availability, String description, ParseUser currentUser, ParseFile img) {
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
}