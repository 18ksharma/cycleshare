package com.example.cycleshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.cycleshare.models.Post;

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

        Post post = (Post) getIntent().getExtras().get("post");

        etAvailability.setText(post.getAvailability());
        etDescription.setText(post.getDescription());
        etPrice.setText(post.getPrice());
        Glide.with(this).load(post.getImage()).placeholder(R.drawable.ic_baseline_person_24).into(ivPostImage);


    }
}