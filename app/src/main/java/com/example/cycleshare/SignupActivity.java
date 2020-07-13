package com.example.cycleshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Set;

public class SignupActivity extends AppCompatActivity {

    EditText tvUsername;
    EditText tvPassword;
    EditText tvConfirm;
    EditText tvEmail;
    Button btnCreateAccount;
    Button btnPicture;

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


        //TODO: implement function so that user can upload/select their own profile picture
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                ParseUser user = new ParseUser();

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




                // Invoke signUpInBackground
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
        });
    }
}