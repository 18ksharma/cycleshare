package com.example.cycleshare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cycleshare.activities.PostDetailsActivity;
import com.example.cycleshare.models.Post;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import static android.app.ProgressDialog.show;

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private ImageView ivPostImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPostImg=itemView.findViewById(R.id.ivPostImg);
            itemView.setOnClickListener(this);
        }

        public void bind(Post post) {
            String pic = post.getImage().getUrl();
            Glide.with(context).load(pic)
                    .centerCrop()
                    .fitCenter()
                    .thumbnail(0.3f).placeholder(R.drawable.ic_baseline_person_24).into(ivPostImg);

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

        @Override
        public boolean onLongClick(View view) {
            Toast.makeText(context, "Post selected", Toast.LENGTH_SHORT).show();
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){

                MaterialAlertDialogBuilder mDialog = new MaterialAlertDialogBuilder(context)
                        .setTitle("Delete Post").setMessage("Are you sure you want to delete this post");

                // Show Dialog
                mDialog.show();
            }
            return false;
        }
    }



}
