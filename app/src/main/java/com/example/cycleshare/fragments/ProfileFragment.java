package com.example.cycleshare.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cycleshare.InitialActivity;
import com.example.cycleshare.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private Button btnLogout;
    private Button btnChangeProfilePicture;
    private ImageView ivProfilePicture;
    private ParseFile img;
    private TextView tvUsername;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //sets everything in view to variable
        btnLogout=view.findViewById(R.id.btnLogout);
        btnChangeProfilePicture=view.findViewById(R.id.btnChangeProfilePicture);
        ivProfilePicture=view.findViewById(R.id.ivProfilePicture);
        tvUsername=view.findViewById(R.id.tvUsername);

        ParseUser user = ParseUser.getCurrentUser();
        tvUsername.setText(user.getUsername());

        //TODO: Set profile picture

        //TODO: open camera/photo gallery to change profile picture
        btnChangeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //Set onclick on logout to log out the current user
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                if(currentUser == null){
                    Intent i = new Intent(getContext(), InitialActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}