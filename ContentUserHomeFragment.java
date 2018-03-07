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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


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

    /**
     * Get a diff between two dates
     *
     * @param date the post date
     * @param time the post time
     * @return the diff value, in the minutes, hours, days, or date
     */
    public String getDateDiff(String date, String time) {
        try {
            final int SECOND = 1000;
            final int MINUTE = 60 * SECOND;
            final int HOUR = 60 * MINUTE;
            final int DAY = 24 * HOUR;

            //get date and time string and calculate difference bw now and post date time
            //get now value
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", new Locale("hi", "in"));
            String postDate = date + " " + time;
            Date pDate = dateFormat.parse(postDate);

            String currentDate = dateFormat.format(new Date().getTime());
            Date cDate = dateFormat.parse(currentDate);
//            Log.e(TAG, "Current date is : " + currentDate);
            Log.e(TAG, "Current date timestamp is : " + cDate.getTime());
            Log.e(TAG, "Post date timestamp is : " + pDate.getTime());
            //find difference between postTime and currentTime
            long ms2 = 43200000; //adding 12 hours (with milliseconds value ) to solve the problem of AM/PM
            long ms = Math.abs((cDate.getTime() + ms2) - pDate.getTime());
            Log.e(TAG, "difference bw timestamps : " + ms);



            StringBuffer text = new StringBuffer("");

            // code for getting the locale date format like hindi or japanese
//            Log.e(TAG, " date and month is @@ -> " + new SimpleDateFormat("dd MMMM",new Locale("hi","IN")).format(postDate));

            if (ms > 4 * DAY) {
                text.append(new SimpleDateFormat("dd MMMM | hh:mm a", Locale.ENGLISH).format(pDate));//showing date and time directly
            } else if (ms > DAY) {
                if (ms / DAY == 1)
                    text.append(" Yesterday");
                else
                    text.append(ms / DAY).append(" DAYS AGO");
            } else if (ms > HOUR) {
                if (ms / HOUR == 1)
                    text.append(ms / HOUR).append(" HOUR AGO");
                else
                    text.append(ms / HOUR).append(" HOURS AGO");
            } else if (ms > MINUTE) {
                if (ms / MINUTE == 1)
                    text.append(ms / MINUTE).append(" MINUTE AGO");
                else
                    text.append(ms / MINUTE).append(" MINUTES AGO");
            } else if (ms > SECOND) {
                text.append(" JUST NOW");
            }
            Log.e(TAG, "time is " + text);
            return String.valueOf(text);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        //        String get_posts_url = "http://192.168.0.128/hrms_app/get_posts_stories.php"; //old URL
        String get_posts_url = "http://192.168.0.119/hrms_app/get_posts_stories.php"; // new URL with new IP address

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
            Log.e("ABCErr", "Latest JSON received is : " + result);
            try {
//                Log.e(TAG, "result is : " + result);
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;
                String date, time, title_message, text_message, image_url, added_by, profile_image_url;

                while (count < jsonArray.length()) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(count);
                    date = jsonObject2.getString("date");
                    time = jsonObject2.getString("time");
                    title_message = jsonObject2.getString("title_message");
                    text_message = jsonObject2.getString("text_message");
                    image_url = jsonObject2.getString("image_url");
                    added_by = jsonObject2.getString("user_name");
                    profile_image_url = jsonObject2.getString("profile_img_url");
                    count++;
                    //call the dateDiff function to set value of date differences
                    String diff = getDateDiff(date, time);
//                    Log.e(TAG, "dateDiff msg is : " + diff);
                    //set this to the Adapter
                    PostStory postStory = new PostStory(diff, title_message, text_message, image_url, added_by, profile_image_url);
                    postStoryList.add(postStory);
//                    Log.e(TAG,"Story is : "+postStory.toString());s
//                    Log.e(TAG, "in onPostExecute() method ...setting the adapter.. ");
                    postStoryAdapter = new PostStoryAdapter(context, postStoryList);
                    mRecyclerView.setAdapter(postStoryAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
