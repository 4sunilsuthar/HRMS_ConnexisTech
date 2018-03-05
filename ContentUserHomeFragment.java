package com.lms.admin.lms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ContentUserHomeFragment extends Fragment {

    private static final String TAG = "ContentUserHomeFragment";
    RecyclerView mRecyclerView;
    List<PostStory> postStoryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_user_home, container, false);

        postStoryList = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.posts_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //        get values in the Adapter from the background task
        BackgroundTask backgroundTask = new BackgroundTask(getContext());
        backgroundTask.execute();

    }

    // Background Task Class just fetching json data here
    @SuppressLint("StaticFieldLeak")
    public class BackgroundTask extends AsyncTask<Void, PostStory, String> {

        @SuppressLint("StaticFieldLeak")
        Context context;
        @SuppressLint("StaticFieldLeak")
        Activity activity;
        PostStoryAdapter postStoryAdapter;
        JSONObject jsonObject;
        JSONArray jsonArray;
        String get_posts_url = "http://192.168.0.128/hrms_app/get_posts_stories.php";

        BackgroundTask(Context context) {
            this.context = context;
            this.activity = (Activity) context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(get_posts_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                InputStream inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
//                Log.e(TAG, "result is : " + result);
                return result.toString().trim();
                /*
                // got our result in JSON format now parse it
                JSONObject jsonObject = new JSONObject(result.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    count++;
                    PostStory postStory = new PostStory(jo.getString("date"), jo.getString("time"), jo.getString("text_message"), jo.getString("image_url"), jo.getString("added_by"));
//                    publishProgress(postStory);
                }
                return result.toString().trim(); //returning result from the web service
                */

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            return "Something Went Wrong... Please Try Again!!!!";
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //result is ready now parse it and sent it to the adapter
//        Log.e("ABCErr","Latest JSON received is : "+json_string);
            try {
                Log.e(TAG, "result is : " + result);
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;
                String date, time, text_message, image_url, added_by;

                while (count < jsonArray.length()) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(count);
                    date = jsonObject2.getString("date");
                    time = jsonObject2.getString("time");
                    text_message = jsonObject2.getString("text_message");
                    image_url = jsonObject2.getString("image_url");
                    added_by = jsonObject2.getString("added_by");
                    //set this to the Adapter
                    /*
                    Contacts contacts = new Contacts(name, email, mobile);
                    contactsAdapter.add(contacts);
                    */
                    count++;
                    PostStory postStory = new PostStory(date, time, text_message, image_url, added_by);
                    postStoryList.add(postStory);
                    Log.e(TAG, "in onPostExecute() method ...setting the adapter.. ");
                    postStoryAdapter = new PostStoryAdapter(context, postStoryList);
                    mRecyclerView.setAdapter(postStoryAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
