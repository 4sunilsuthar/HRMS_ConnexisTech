package com.lms.admin.lms;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

public class UserProfileFragment extends Fragment {
    private static final String TAG = "UserProfileFragment";
    private Button btnEditProfile;
    private TextView tvEmpName, tvEmpGender, tvEmpTitle, tvEmpEmail, tvEmpPhone, tvEmpAddress, tvEmpReportingTo, tvEmpDateOfJoining, tvEmpTopSkills;
    private ImageView imgCoverArt, imgEmpProfile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView() called...");
        return inflater.inflate(R.layout.fragment_user_profile, container, false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate() called...");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach() called...");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated() called...");

        // get all the values here
        btnEditProfile = getView().findViewById(R.id.btn_edit_profile);
        tvEmpName = getView().findViewById(R.id.tv_profile_emp_name);
        tvEmpGender = getView().findViewById(R.id.tv_profile_gender);
        tvEmpTitle = getView().findViewById(R.id.tv_profile_emp_designation);
        tvEmpEmail = getView().findViewById(R.id.tv_profile_emp_email);
        tvEmpPhone = getView().findViewById(R.id.tv_profile_emp_phone);
        tvEmpAddress = getView().findViewById(R.id.tv_profile_address);
        tvEmpReportingTo = getView().findViewById(R.id.tv_profile_reporting_to);
        tvEmpDateOfJoining = getView().findViewById(R.id.tv_profile_date_of_joining);
        imgCoverArt = getView().findViewById(R.id.img_cover_art);
        imgEmpProfile = getView().findViewById(R.id.img_profile_image);

        //when view is created then fetch values from db and display it here
        //also make sure only values existing are to be shown
        //hide other layouts completely
        //execute the background task and perform desired operations
        BackgroundTask backgroundTaskFetchValues = new BackgroundTask(getContext());
        backgroundTaskFetchValues.execute();//executing the background task


    }

    //background task fetching the data and displaying it here
    @SuppressLint("StaticFieldLeak")
    public class BackgroundTask extends AsyncTask<Void, PostStory, String> {

        JSONObject jsonObject;
        JSONArray jsonArray;
        String get_user_profile_details_url = "http://192.168.0.128/hrms_app/get_user_profile_details.php";

        BackgroundTask(Context context) {
            Log.e(TAG, "onBackgroundTask called...");

            //            this.context = context;
//            this.activity = (Activity) context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(get_user_profile_details_url);
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
                Log.e(TAG, "result is : " + result);
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
            Log.e(TAG, "Latest JSON received is : " + result);
            //parse the result with the function
            parseResult(result);
        }

        private void parseResult(String result) {

            try {
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;
                String name, title, email, phone, qualification, gender, dateOfJoining, reportingTo, skills, coverArtImgUrl, profileImgUrl;

                while (count < jsonArray.length()) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(count);
                    name = jsonObject2.getString("name");
                    title = jsonObject2.getString("title");
                    email = jsonObject2.getString("email");
                    phone = jsonObject2.getString("phone");
                    qualification = jsonObject2.getString("qualification");
                    gender = jsonObject2.getString("gender");
                    dateOfJoining = jsonObject2.getString("dateOfJoining");
                    reportingTo = jsonObject2.getString("reportingTo");
                    skills = jsonObject2.getString("skills");
                    coverArtImgUrl = jsonObject2.getString("coverArtImgUrl");
                    profileImgUrl = jsonObject2.getString("profileImgUrl");

//                    checking if any value is null then hide that layout from the main screen
                    if (name.equals(null)) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_name);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpName.setText(name);
                    }

                    if (title.equals(null)) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_title);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpName.setText(title);
                    }


                    if (gender.equals(null)) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_gender);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpName.setText(gender);
                    }

                    if (email.equals(null)) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_email);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpName.setText(email);
                    }

                    if (qualification.equals(null)) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_qualification);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpName.setText(qualification);
                    }

                    if (phone.equals(null)) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_phone);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpName.setText(phone);
                    }

                    if (dateOfJoining.equals(null)) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_date_of_joining);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpName.setText(dateOfJoining);
                    }

                    if (skills.equals(null)) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_top_skills);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpName.setText(skills);
                    }

                    if (reportingTo.equals(null)) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_reporting_to);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpName.setText(reportingTo);
                    }

                    if (coverArtImgUrl.equals(null)) {
                        //set default cover art if not provided
//                        Uri.parse("android.resource://"+getPackageName()+"/drawable/picture_post1.jpeg");
                        Uri defaultImgUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + getContext().getResources().getDrawable(R.drawable.picture_post1));
//                        C:\Users\Admin\AndroidStudioProjects\LMS\app\src\main\res\drawable\picture_post1.jpeg
                        Picasso.with(getContext()).load(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + getContext().getResources().getDrawable(R.drawable.picture_post1))).into(imgCoverArt);


                    } else {
                        //assign the value to respective field
                        Picasso.with(getContext()).load(Uri.parse(coverArtImgUrl)).into(imgCoverArt);//assign user customized image
                    }

                    if (profileImgUrl.equals(null)) {
                        //set default profile image if not provided
                        Picasso.with(getContext()).load(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + getContext().getResources().getDrawable(R.drawable.person))).into(imgCoverArt);
//                        Picasso.with(getContext()).load(Uri.parse(profileImgUrl)).into(imgEmpProfile);//assign user customized image
                    } else {
                        //assign the value to respective field
                        Picasso.with(getContext()).load(Uri.parse(profileImgUrl)).into(imgEmpProfile);//assign user customized image
                    }
                    count++;
                    // values fetched now assign them to respective fields
                }//end of while loop
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}