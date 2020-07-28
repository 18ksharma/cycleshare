package com.example.cycleshare.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cycleshare.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnForgot;
    private EditText etEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Assigned attributes from activity_login to variables
        etUsername=findViewById(R.id.etUsername);
        etPassword=findViewById(R.id.etPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnForgot=findViewById(R.id.btnForgot);
        etEmail=findViewById(R.id.etEmail);

        etEmail.setVisibility(View.GONE);

        //Sets an onclicklistener on the login button
        btnLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                //Assigns username and password
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etEmail.setVisibility(View.VISIBLE);

                if(etEmail.getText().equals("")){
                    Toast.makeText(LoginActivity.this, "Email cannot be blank", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser.requestPasswordResetInBackground(etEmail.getText().toString(),
                        new RequestPasswordResetCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    // An email was successfully sent with reset instructions.
                                    Toast.makeText(LoginActivity.this, "An email was sent with reset instructions", Toast.LENGTH_SHORT).show();
                                    etEmail.setText("");
                                } else {
                                    Toast.makeText(LoginActivity.this, "Error sending email", Toast.LENGTH_SHORT).show();
                                    // Something went wrong. Look at the ParseException to see what's up.
                                }
                            }
                        });
            }
        });
    }

    private void loginUser(String username, String password) {

        Log.i(TAG, "Attempting to login user "+ username);
        //Uses parse to login user
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT);
            }
        });
    }
}