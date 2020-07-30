package com.example.cycleshare.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.cycleshare.R;
import com.example.cycleshare.Utils;
import com.example.cycleshare.activities.PostDetailsActivity;
import com.example.cycleshare.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

import static java.lang.Math.round;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> allposts) {
        posts.addAll(allposts);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView ivProfilePic;
        private TextView tvUser;
        private ImageView ivPicture;
        private TextView tvDescription;
        private TextView tvTimestamp;
        private TextView tvRelativeDistance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPicture=itemView.findViewById(R.id.ivPicture);
            tvUser=itemView.findViewById(R.id.tvUser);
            ivProfilePic=itemView.findViewById(R.id.ivProfilePic);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            tvTimestamp=itemView.findViewById(R.id.tvTimestamp);
            tvRelativeDistance=itemView.findViewById(R.id.tvRelativeDistance);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //gets of post clicked on
            int position = getAdapterPosition();

            //checks if position is valid
            if (position != RecyclerView.NO_POSITION) {
                // gets movie at position
                Post post = posts.get(position);

                // creates intent for  new activity
                Intent intent = new Intent(context, PostDetailsActivity.class);

                intent.putExtra("description", post.getDescription());
                intent.putExtra("image", post.getImage());
                intent.putExtra("timestamp", post.getCreatedAt());
                intent.putExtra("username", post.getUser().getUsername());
                intent.putExtra("condition", post.getCondition());
                intent.putExtra("price", post.getPrice());
                intent.putExtra("availability", post.getAvailability());
                intent.putExtra("profilePic", post.getUser().getParseFile("profilePic"));
                intent.putExtra("email", post.getUser().getEmail());
                intent.putExtra("latitude", post.getLatitude());
                intent.putExtra("longitude", post.getLongitude());
                intent.putExtra("user", post.getUser());
                intent.putExtra("post", post);

                intent.putExtra("parent", "home");

                // shows activity
                context.startActivity(intent);
            }

        }

        public void bind(Post post) {
            tvUser.setText("@"+post.getUser().getUsername());
            tvDescription.setText(post.getDescription());
            ParseFile pic = post.getImage();
            ParseFile profilePic = post.getUser().getParseFile("profilePic");
            tvTimestamp.setText(Utils.getRelativeTimeAgo(post.getCreatedAt().toString()));
            if(post.getPoint()!=null){
                double distance = post.getPoint().distanceInMilesTo(ParseUser.getCurrentUser().getParseGeoPoint("location"));
                tvRelativeDistance.setText(String.valueOf(round(distance))+" miles");
            }
            if(post.getPoint()==null){
                tvRelativeDistance.setVisibility(View.GONE);
            }
            Glide.with(context).load(pic.getUrl()).placeholder(R.drawable.ic_baseline_person_24).into(ivPicture);
            if(profilePic!=null) {
                //Circle crops profile pic
                Glide.with(context).load(profilePic.getUrl()).placeholder(R.drawable.ic_baseline_person_24)
                        .transform(new CircleCrop()).into(ivProfilePic);
            }
            else{
                Glide.with(context).load("http://img.freepik.com/free-vector/abstract-geometric-lines-seamless-pattern_144290-8.jpg?size=626&ext=jpg")
                        .placeholder(R.drawable.ic_baseline_person_24).transform(new CircleCrop()).into(ivProfilePic);

            }
        }


    }
}
