package com.example.cycleshare.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.cycleshare.EndlessRecyclerViewScrollListener;
import com.example.cycleshare.PostsAdapter;
import com.example.cycleshare.R;
import com.example.cycleshare.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allposts;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    private int limit;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        /*adapter.clear();
        // 2. Notify the adapter of the update
        adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
        // 3. Reset endless scroll listener when performing a new search
        scrollListener.resetState();*/
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //might need to check this
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599

                queryPosts(query, 0);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryPosts(newText, 0);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                onOptionsMenuClosed();
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void onOptionsMenuClosed() {
        queryPosts(null, 0);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts=view.findViewById(R.id.rvPosts);
        swipeContainer=view.findViewById(R.id.swipeContainer);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        allposts= new ArrayList<>();
        adapter=new PostsAdapter(getContext(), allposts);


        rvPosts.setAdapter(adapter);

        rvPosts.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextData(page);
            }
        };


        queryPosts(null, 0);

        //Updates adapter for when posts are deleted
        allposts.clear();
        adapter.clear();
        adapter.addAll(allposts);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allposts.clear();
                adapter.clear();
                adapter.addAll(allposts);
                queryPosts(null, 0);
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_light);


        rvPosts.addOnScrollListener(scrollListener);
    }

    private void loadNextData(int page) {
        limit = (page)*20;
        queryPosts(null, limit);
    }

    protected void queryPosts(String search, int skip){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        if(search!=null){
            adapter.clear();
            adapter.notifyDataSetChanged();
            query.include(Post.KEY_USER);
            query.whereContains(Post.KEY_DESCRIPTION, search);
        }
        query.setLimit(20);
        if(skip!=0){
            query.setSkip(limit);
        }
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