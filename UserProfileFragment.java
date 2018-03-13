package com.lms.admin.lms;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
    private TextView tvEmpName, tvEmpGender, tvEmpTitle, tvEmpEmail, tvEmpPhone, tvEmpAddress, tvEmpReportingTo, tvEmpDateOfJoining, tvEmpTopSkills, tvEmpQualification;
    private ImageView imgCoverArt, imgEmpProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        tvEmpQualification = getView().findViewById(R.id.tv_profile_emp_qualification);
        imgCoverArt = getView().findViewById(R.id.img_cover_art);
        imgEmpProfile = getView().findViewById(R.id.img_profile_image);

        //when view is created then fetch values from db and display it here
        //also make sure only values existing are to be shown
        //hide other layouts completely
        //execute the background task and perform desired operations
        BackgroundTask backgroundTaskFetchValues = new BackgroundTask(getContext());
        backgroundTaskFetchValues.execute();//executing the background task

        //listener for btnEditProfile
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show editable views to the user
                FragmentManager fragment_manager;
                FragmentTransaction fragmentTrans;
                Fragment fragment;

                fragment_manager = getActivity().getSupportFragmentManager();
                fragmentTrans = fragment_manager.beginTransaction();
                fragment = new EditUserProfileFragment();
                fragment_manager.popBackStack();
                fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("user_profile_fragment").commit();
                //showing the edit profile fragment


            }
        });


    }


    //background task fetching the data and displaying it here
    @SuppressLint("StaticFieldLeak")
    public class BackgroundTask extends AsyncTask<Void, PostStory, String> {

        JSONObject jsonObject;
        JSONArray jsonArray;
        //        String get_user_profile_details_url = "http://192.168.0.128/hrms_app/get_user_profile_details.php";//old URL
//        String get_user_profile_details_url = "http://192.168.0.129/hrms_app/get_user_profile_details.php";//new URL changed IP

        ProgressDialog progressDialog;

        BackgroundTask(Context context) {
            //            this.context = context;
//            this.activity = (Activity) context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show progress dialog here
            progressDialog = ProgressDialog.show(getContext(), "Getting you on board", "Please Wait...", false, false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
//                URL url = new URL(API_URLs.getUserDetailsAPIUrl);
                URL url = new URL(API_URLs.getUserProfileDetailsAPIUrl);
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
            progressDialog.dismiss();
            //parse the result with the function
            parseResult(result);
        }

        private void parseResult(String result) {

            try {
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;
                String name, title, email, phone, qualification, gender, address, dateOfJoining, reportingTo, skills, coverArtImgUrl, profileImgUrl;
                Log.e(TAG, "Array Length is : " + jsonArray.length());
                while (count < jsonArray.length()) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(count);
                    name = jsonObject2.getString("name");
                    title = jsonObject2.getString("title");
                    email = jsonObject2.getString("email");
                    phone = jsonObject2.getString("phone");
                    address = jsonObject2.getString("address");
                    qualification = jsonObject2.getString("qualification");
                    gender = jsonObject2.getString("gender");
                    dateOfJoining = jsonObject2.getString("dateOfJoining");
                    reportingTo = jsonObject2.getString("reportingTo");
//                    skills = jsonObject2.getString("skills");
                    coverArtImgUrl = jsonObject2.getString("coverArtImgUrl");
                    profileImgUrl = jsonObject2.getString("profileImgUrl");

//                    checking if any value is null then hide that layout from the main screen
                    if (name.equals("null")) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_name);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpName.setText(name);
                    }

                    Log.e(TAG, "Name: " + name + " gender " + gender + " address " + address);
                    if (address.equals("null")) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_title);
                        layout.setVisibility(View.GONE);

                    } else {
                        //assign the value to respective field
                        tvEmpAddress.setText(title);
                    }

                    if (title.equals("null")) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_title);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpTitle.setText(title);
                    }
                    if (gender.equals("null")) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_gender);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpGender.setText(gender);
                    }

                    if (email.equals("null")) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_email);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpEmail.setText(email);
                    }

                    if (qualification.equals("null")) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_qualification);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpQualification.setText(qualification);
                    }

                    if (phone.equals("null")) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_phone);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpPhone.setText(phone);
                    }

                    if (dateOfJoining.equals("null")) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_date_of_joining);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpDateOfJoining.setText(dateOfJoining);
                    }

                 /*   if (skills.equals(null)) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_top_skills);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpName.setText(skills);
                    }*/

                    if (reportingTo.equals("null")) {
                        LinearLayout layout = getView().findViewById(R.id.layout_emp_reporting_to);
                        layout.setVisibility(View.GONE);
                    } else {
                        //assign the value to respective field
                        tvEmpReportingTo.setText(reportingTo);
                    }
                    Log.e(TAG, "CoverImgUrl is : " + coverArtImgUrl);
                    if (coverArtImgUrl.equals("null")) {
                        //set default cover art if not provided
                        imgCoverArt.setBackgroundResource(R.drawable.picture_post3);//showing demo image till load

                    } else {
                        //assign the value to respective field
                        Picasso.with(getContext()).load(Uri.parse(coverArtImgUrl)).into(imgCoverArt);//assign user customized image
                    }
                    Log.e(TAG, "imgUrl is : " + profileImgUrl);
                    if (profileImgUrl.equals("null")) {
                        //set default profile image if not provided
                        imgEmpProfile.setBackgroundResource(R.drawable.person);//showing demo image till load
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