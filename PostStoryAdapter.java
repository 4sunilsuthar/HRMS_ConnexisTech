package com.lms.admin.lms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Admin on 01-03-2018.
 */
//adapter class
public class PostStoryAdapter extends RecyclerView.Adapter<PostStoryAdapter.PostStoryViewHolder> {

    private static final String TAG = "PostStoryAdapter";
    List<PostStory> postStoriesList;
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
        holder.postTxtMsg.setText(postStory.getTextMsg());
//        Log.e(TAG,"Setting Image for Post");
        Picasso.with(context).load(postStory.getImage()).into(holder.postImg);
//        Log.e(TAG, "Post image url is : " + postStory.getImage());
        holder.postImg.setImageURI(Uri.parse(postStory.getImage()));

//        Log.e(TAG, "Profile image url is : " + postStory.getProfileImg());
        Picasso.with(context).load(postStory.getProfileImg()).into(holder.postUserProfile);

    }

    @Override
    public int getItemCount() {
        return postStoriesList.size();
    }

    class PostStoryViewHolder extends RecyclerView.ViewHolder {
        ImageView postImg, postUserProfile;
        TextView postTxtMsg, postUsername, postDate;

        PostStoryViewHolder(View itemView) {
            super(itemView);

            postImg = itemView.findViewById(R.id.post_image);
            postUserProfile = itemView.findViewById(R.id.post_profile_image);
            postUsername = itemView.findViewById(R.id.post_by_user);
            postDate = itemView.findViewById(R.id.post_date_time);
            postTxtMsg = itemView.findViewById(R.id.post_text_msg);
        }
    }
}

/*

    ArrayList<PostStory> arrayList = new ArrayList<>();

    public PostStoryAdapter(ArrayList<PostStory> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row_card,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        PostStory postStory = arrayList.get(position);
        holder.userName.setText(postStory.getUsername());
        holder.postTitle.setText(postStory.getTime());
        holder.postTextMsg.setText(postStory.getTextMsg());
        holder.postDate.setText(postStory.getDate());
        //image Task here
        //        holder.postImage.

//        Picasso.with().load(postStory.getProfileImg()).into(holder.postImage);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


//viewHolder class
   */
/* public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView userName,postDate,postTitle,postTextMsg;
        ImageView profileImage,postImage;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.post_by_user);
            postDate = itemView.findViewById(R.id.post_date_time);
            postTitle = itemView.findViewById(R.id.post_title);
            postTitle = itemView.findViewById(R.id.post_text_msg);

            profileImage = itemView.findViewById(R.id.post_profile_image);
            postImage = itemView.findViewById(R.id.post_image);

        }
    }*/

