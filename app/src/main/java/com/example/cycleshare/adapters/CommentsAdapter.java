package com.example.cycleshare.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.cycleshare.R;
import com.example.cycleshare.Utils;
import com.example.cycleshare.activities.MainActivity;
import com.example.cycleshare.models.Comment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>  {
    private Context context;
    private List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void clear() {
        comments.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Comment> listofcomments) {
        comments.addAll(listofcomments);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivprofilePic;
        private TextView tvUsername;
        private TextView tvContents;
        private TextView tvTimestamp;
        private ImageView ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivprofilePic=itemView.findViewById(R.id.ivProfilePicture);
            tvUsername=itemView.findViewById(R.id.tvUsername);
            tvContents=itemView.findViewById(R.id.tvContent);
            tvTimestamp=itemView.findViewById(R.id.tvTimestamp);
            ivDelete=itemView.findViewById(R.id.ivDelete);
        }

        public void bind(final Comment comment) {
            ivDelete.setVisibility(View.GONE);
            Log.i("Comments", "comment author"+comment.getUser());
            Log.i("Comments", "cur user"+ParseUser.getCurrentUser());
            if(comment.getUser().getUsername().equals(ParseUser.getCurrentUser().getUsername()) ){
                ivDelete.setVisibility(View.VISIBLE);
            }
            tvUsername.setText(comment.getUser().getUsername());
            tvContents.setText(comment.getContents());
            tvTimestamp.setText(Utils.getRelativeTimeAgo(comment.getCreatedAt().toString()));
            ParseFile profilePic = comment.getUser().getParseFile("profilePic");
            if(profilePic!=null) {
                //Circle crops profile pic
                Glide.with(context).load(profilePic.getUrl()).placeholder(R.drawable.ic_baseline_person_24)
                        .transform(new CircleCrop()).into(ivprofilePic);
            }
            else{
                Glide.with(context).load("http://img.freepik.com/free-vector/abstract-geometric-lines-seamless-pattern_144290-8.jpg?size=626&ext=jpg")
                        .placeholder(R.drawable.ic_baseline_person_24).transform(new CircleCrop()).into(ivprofilePic);

            }

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        comment.delete();
                        comment.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                                Toast.makeText(context, "Comment deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            });


        }
    }
}
