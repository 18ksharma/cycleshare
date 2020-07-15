package com.example.cycleshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.cycleshare.models.Post;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView ivProfilePic;
        private TextView tvUser;
        private ImageView ivPicture;
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPicture=itemView.findViewById(R.id.ivPicture);
            tvUser=itemView.findViewById(R.id.tvUser);
            ivProfilePic=itemView.findViewById(R.id.ivProfilePic);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //TODO: Implement if a post is clicked on with timestamp

        }

        public void bind(Post post) {
            tvUser.setText(post.getUser().getUsername());
            tvDescription.setText(post.getDescription());
            ParseFile pic = post.getImage();
            ParseFile profilePic = post.getUser().getParseFile("profilePic");

            Glide.with(context).load(pic.getUrl()).placeholder(R.drawable.ic_baseline_person_24).into(ivPicture);
            //Circle crops profile pic
            Glide.with(context).load(profilePic.getUrl()).placeholder(R.drawable.ic_baseline_person_24)
                    .transform(new CircleCrop()).into(ivProfilePic);

        }
    }
}
