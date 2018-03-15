package com.lms.admin.lms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Admin on 01-03-2018.
 */
//adapter class
public class PostStoryAdapter extends RecyclerView.Adapter<PostStoryAdapter.PostStoryViewHolder> {

    private static final String TAG = "PostStoryAdapter";
    private List<PostStory> postStoriesList;
    private Context context;

    PostStoryAdapter(Context context, List<PostStory> postStoriesList) {
        this.context = context;
        this.postStoriesList = postStoriesList;
    }

    @NonNull
    @Override
    public PostStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.post_row_card, null);
        return new PostStoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostStoryViewHolder holder, int position) {

        PostStory postStory = postStoriesList.get(position);
        holder.postUsername.setText(postStory.getUsername());
        holder.postDate.setText(postStory.getDate());
        holder.postTitleMsg.setText(postStory.getTitleMsg());
        holder.postTxtMsg.setText(postStory.getTextMsg());
        //checking if post image is not available then show a default picture in that post
        Log.e(TAG, "postStory.getImage() is " + postStory.getImage());
        if (postStory.getImage().equals("null")) {
            Picasso.with(context).load(R.drawable.picture_post1).into(holder.postImg);//showing demo image till load
        } else {
            Picasso.with(context).load(postStory.getImage()).into(holder.postImg);//otherwise showing the actual image
        }
        Log.e(TAG, "postStory.getProfileImg() is :" + postStory.getProfileImg());
        if (postStory.getProfileImg().equals("null")) {
//            Log.e(TAG, "profile path is null so Lets have default image >>>>><<<<<<");
            Picasso.with(context).load(R.drawable.person).into(holder.postUserProfile);//showing demo image till load or img not available
        } else {
            Picasso.with(context).load(postStory.getProfileImg()).into(holder.postUserProfile);//otherwise showing the actual image
        }
    }

    @Override
    public int getItemCount() {
        return postStoriesList.size();
    }

    class PostStoryViewHolder extends RecyclerView.ViewHolder {
        ImageView postImg, postUserProfile;
        TextView postTitleMsg, postTxtMsg, postUsername, postDate;

        PostStoryViewHolder(View itemView) {
            super(itemView);
            postImg = itemView.findViewById(R.id.post_image);
            postUserProfile = itemView.findViewById(R.id.post_profile_image);
            postUsername = itemView.findViewById(R.id.post_by_user);
            postDate = itemView.findViewById(R.id.post_date_time);
            postTxtMsg = itemView.findViewById(R.id.post_text_msg);
            postTitleMsg = itemView.findViewById(R.id.post_title_msg);

            //setting the onclick listener to the Post to display post
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Post clicked", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, ShowPostDetailsActivity.class));
                }
            });
        }
    }
}
