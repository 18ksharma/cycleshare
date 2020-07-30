package com.example.cycleshare.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cycleshare.EndlessRecyclerViewScrollListener;
import com.example.cycleshare.adapters.PostsAdapter;
import com.example.cycleshare.R;
import com.example.cycleshare.models.Post;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

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
    private boolean searching;

    private static LocationRequest mLocationRequest;

    private static long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private static long FASTEST_INTERVAL = 2000; /* 2 sec */
    public final static int REQUEST_FINE_LOCATION = 1;


    private double lon;
    private double lat;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng latLng;
    private ParseGeoPoint point;
    private String filter;

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
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
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
        MenuItem filterDescription = menu.findItem(R.id.filter_description);
        final MenuItem filterCondition = menu.findItem(R.id.filter_condition);
        final MenuItem filterPrice = menu.findItem(R.id.filter_price);
        final MenuItem filterAvailability = menu.findItem(R.id.filter_availability);
        new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.equals(filterCondition)){
                    filter=Post.KEY_CONDITION;
                    Log.i(TAG, "Filter set to condition");
                }
                else if(menuItem.equals(filterPrice)){
                    filter=Post.KEY_PRICE;
                    Log.i(TAG, "Filter set to price");
                }
                else if(menuItem.equals(filterAvailability)){
                    filter=Post.KEY_AVAILABILITY;
                    Log.i(TAG, "Filter set to availability");
                }
                filter=Post.KEY_DESCRIPTION;
                Log.i(TAG, "Filter set to description");
                return false;
            }
        };
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
                allposts.clear();
                // 2. Notify the adapter of the update
                adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                // 3. Reset endless scroll listener when performing a new search
                scrollListener.resetState();

                queryPosts(query, 0);
                searchView.clearFocus();
                searching=true;

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                allposts.clear();
                // 2. Notify the adapter of the update
                adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                // 3. Reset endless scroll listener when performing a new search
                scrollListener.resetState();
                queryPosts(newText, 0);
                searching=true;
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                onOptionsMenuClosed();
                searching=false;
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void onOptionsMenuClosed() {
        queryPosts(null, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.filter_availability:
                filter= Post.KEY_AVAILABILITY;
                Log.i(TAG, "Filter set to availability");
                return true;
            case R.id.filter_condition:
                filter=Post.KEY_CONDITION;
                Log.i(TAG, "Filter set to condition");
                return true;
            case R.id.filter_price:
                filter=Post.KEY_PRICE;
                Log.i(TAG, "Filter set to price");
                return true;
            case R.id.filter_description:
                filter=Post.KEY_DESCRIPTION;
                Log.i(TAG, "Filter set to description");
                return true;
            case R.id.action_search:
                return true;
            default:
                filter=Post.KEY_DESCRIPTION;
                Log.i(TAG, "Filter set to des");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startLocationUpdates();

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

                if(searching!=true) {
                    loadNextData(page);
                }
                Log.i(TAG, "onLoadMore called");
            }
        };
        rvPosts.addOnScrollListener(scrollListener);


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
                //might have to switch with queryposts and adapter.addAll
                queryPosts(null, 0);
                adapter.addAll(allposts);
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_light);

        if(checkPermissions()==false){
            requestPermissions();
        }

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions((Activity) getContext(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_FINE_LOCATION);
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void startLocationUpdates() {
        //Creates location request for receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        SettingsClient settingsClient = LocationServices.getSettingsClient(getContext());
        settingsClient.checkLocationSettings(locationSettingsRequest);


        getFusedLocationProviderClient(getContext()).requestLocationUpdates(mLocationRequest, new com.google.android.gms.location.LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    private void onLocationChanged(Location location) {

        // New location has now been determined
        // You can now create a LatLng Object for use with maps
        lat = location.getLatitude();
        lon=location.getLongitude();

        point= new ParseGeoPoint(lat, lon);

        ParseUser currentuser = ParseUser.getCurrentUser();
        if(currentuser!=null){

            currentuser.put("location", point);
            currentuser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Log.i(TAG, "got user location: "+ String.valueOf(point));
                }
            }
            );
        }
    }

    //Does not load more if it is searching
    private void loadNextData(int page) {
            limit = (page)*20;
            queryPosts(null, limit);
    }

    protected void queryPosts(String search, int skip){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        if(filter==null){
            filter=Post.KEY_DESCRIPTION;
        }
        if(search!=null){
            adapter.clear();
            allposts.clear();
            // 2. Notify the adapter of the update
            adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
            // 3. Reset endless scroll listener when performing a new search
            scrollListener.resetState();
            //query.include(Post.KEY_USER);
            query.whereContains(filter, search);

        }
        else if(skip!=0){
            query.setSkip(limit);
        }
        query.setLimit(20);
        query.whereNear(Post.KEY_POINT, ParseUser.getCurrentUser().getParseGeoPoint("location"));
        Log.i(TAG, String.valueOf(point));

        //query.addDescendingOrder(Post.KEY_CREATEDAT);
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