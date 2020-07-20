package com.example.cycleshare.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cycleshare.InitialActivity;
import com.example.cycleshare.PostsAdapter;
import com.example.cycleshare.R;
import com.example.cycleshare.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    private Button btnLogout;
    private Button btnChangeProfilePicture;
    private ImageView ivProfilePicture;
    private ParseFile img;
    private TextView tvUsername;
    private RecyclerView rvUserPosts;
    private PostsAdapter adapter;
    private List<Post> allposts;

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
        rvUserPosts=view.findViewById(R.id.rvUserPoses);

        ParseUser user = ParseUser.getCurrentUser();
        tvUsername.setText(user.getUsername());
        if(user.getParseFile("profilePic")!=null) {
            Glide.with(view.getContext()).load(user.getParseFile("profilePic").getUrl())
                    .placeholder(R.drawable.ic_baseline_person_24).into(ivProfilePicture);
        }
        else{
            Glide.with(view.getContext()).load("http://img.freepik.com/free-vector/abstract-geometric-lines-seamless-pattern_144290-8.jpg?size=626&ext=jpg").into(ivProfilePicture);
        }

        allposts= new ArrayList<>();
        adapter=new PostsAdapter(view.getContext(), allposts);

        rvUserPosts.setAdapter(adapter);
        rvUserPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        queryPostsbyUser();

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

    private void queryPostsbyUser() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATEDAT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return ;
                }
                allposts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}