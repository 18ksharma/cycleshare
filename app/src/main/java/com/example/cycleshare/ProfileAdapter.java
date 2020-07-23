package com.example.cycleshare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cycleshare.models.Post;
import com.parse.ParseFile;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    public ProfileAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }
    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {
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
        private ImageView ivPostImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPostImg=itemView.findViewById(R.id.ivPostImg);
            itemView.setOnClickListener(this);
        }

        public void bind(Post post) {
            String pic = post.getImage().getUrl();
            Glide.with(context).load(pic).placeholder(R.drawable.ic_baseline_person_24).into(ivPostImg);

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

                intent.putExtra("parent", "profile");

                // shows activity
                context.startActivity(intent);
            }
        }
    }
}
