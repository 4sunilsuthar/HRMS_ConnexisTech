package com.lms.admin.lms;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class EditUserProfileFragment extends Fragment {
    private static final String TAG = "EditUserProfileFragment";
    private static final int PICK_COVER_IMAGE = 9;
    String name, phone, qualification, gender, address, skills, coverArtImgUrl, profileImgUrl;
    Bitmap bitmapProfile, bitmapCoverArt;
    private EditText edEmpName, edEmpGender, edEmpPhone, edEmpAddress, edEmpTopSkills, edEmpQualification;
    private ImageView imgEmpProfile, imgCoverArt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_user_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //call the background task here to fetch initial data here if available
        new BackgroundTask().execute();//execute the background task to fetch initial data

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get all the values here
        Button btnUpdateProfile = getView().findViewById(R.id.btn_save_profile);
        edEmpName = getView().findViewById(R.id.ed_edit_emp_name);
        edEmpGender = getView().findViewById(R.id.ed_edit_gender);
        edEmpPhone = getView().findViewById(R.id.ed_edit_phone);
        edEmpAddress = getView().findViewById(R.id.ed_edit_address);
        edEmpTopSkills = getView().findViewById(R.id.ed_edit_top_skills);
        edEmpQualification = getView().findViewById(R.id.ed_edit_qualification);
//        tvEmpEmail = getView().findViewById(R.id.ed_edit_email);
        imgCoverArt = getView().findViewById(R.id.img_edit_cover_art);
        imgEmpProfile = getView().findViewById(R.id.img_edit_profile_image);
//        setting the buttons
        ImageView imgBtnEditEmpProfile = getView().findViewById(R.id.btn_update_profile_image);
        ImageView imgBtnEditCoverArt = getView().findViewById(R.id.btn_update_cover_art);

        //when view is created then fetch values from db and display it here
        //also make sure only values existing are to be shown
        //hide other layouts completely
        //execute the background task and perform desired operations

        //profile Update imgBtn Listener Here
        imgBtnEditEmpProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code here to get new Profile pic
//                Log.e(TAG, "Pencil Button Clicked...");

//                android.app.Fragment currentFragment = getActivity().getFragmentManager().findFragmentById(R.id.fragment_container1);
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle("Select New Profile Photo")
                        .setAutoZoomEnabled(true)
                        .setMinCropResultSize(240, 360)
                        .start(getContext(), getFragmentManager().findFragmentById(R.id.fragment_container1));// for fragment (DO NOT use "getActivity()")
//                Log.e(TAG, "getActivity() is..." + getActivity());

            }
        });

        //Cover Art Update imgBtn Listener Here
        imgBtnEditCoverArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code here to get new Cover pic
                /*
                CropImage.activity()
                        .setAutoZoomEnabled(true)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle("Select New Cover Art")
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setMinCropResultSize(640,360)
                        .start(getContext(), getFragmentManager().findFragmentById(R.id.fragment_container1));// for fragment (DO NOT use "getActivity()")
                */
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select New Cover Photo"), PICK_COVER_IMAGE);
            }
        });

        //btnUpdateProfile button's OnClickListener here
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on click of this button perform various operations like
                // #1. check validations of all the fields
                // #2. call the alertDialog for confirmation of registration
                // #3.then call the background task that manages data insertion in the db tables.
                // #4. then show the Toast that employee added successfully ..

                // #1.call the alertDialog for confirmation of registration
                new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme)
                        .setIcon(R.drawable.ic_warning_white)
                        .setTitle("Confirm Profile Update")
                        .setMessage("Do You Want To Continue ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // update employee details
                                //code here to save given details
                                //run background task to save emp details
                                //get all values

                                name = edEmpName.getText().toString().trim();
                                gender = edEmpGender.getText().toString().trim();
                                qualification = edEmpQualification.getText().toString().trim();
                                phone = edEmpPhone.getText().toString().trim();
                                skills = edEmpTopSkills.getText().toString().trim();
                                address = edEmpAddress.getText().toString().trim();

//                                call background task to update data in the database
                                UpdateEmpProfileData updateEmpProfileData = new UpdateEmpProfileData(name, gender, qualification, phone, address, skills, profileImgUrl, coverArtImgUrl);
                                updateEmpProfileData.execute();//calling background task
                                //now go to user home feeds
//                                startActivity(new Intent(RegisterNewEmpActivity.this, AdminDashboardActivity.class));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss the alert dialog
                                dialog.dismiss();
                            }
                        })
                        .show().setCanceledOnTouchOutside(false);
            }
        });

        /*btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code here to update user profile

                //Call background task with the call to API to update user Profile Details





            }
        });*/


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //code for profile photo selection
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            Log.e(TAG, "intent data is : " + data);
//            Log.e(TAG, "resultCode is : " + resultCode);
            if (resultCode == Activity.RESULT_OK) {
//                get Image URL
                Uri resultUri = result.getUri();
//                Log.e(TAG, "URI is : " + resultUri);
                try {
                    bitmapProfile = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
//                    Log.e(TAG, "bitmap is : " + bitmap);
                    imgEmpProfile.setImageBitmap(bitmapProfile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }
        //code for cover art photo selection
        if (requestCode == PICK_COVER_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
//                Log.e(TAG, "URI is : " + data.toString());
                Uri resultUri = data.getData();
//                Log.e(TAG, "URI is : " + resultUri);
                bitmapCoverArt = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
//                Log.e(TAG, "Cover bitmap is : " + bitmap);
                imgCoverArt.setImageBitmap(bitmapCoverArt);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class BackgroundTask extends AsyncTask<Void, PostStory, String> {

        JSONObject jsonObject;
        JSONArray jsonArray;
        //        String get_user_profile_details_url = "http://192.168.0.128/hrms_app/get_user_profile_details.php";//old URL
        ProgressDialog progressDialog;

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
            progressDialog.dismiss();
            //parse the result with the function
            parseResult(result.toString().trim());

        }

        private void parseResult(String result) {

            try {
                jsonObject = new JSONObject(result);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;
//                String name, phone, qualification, gender, address, skills, coverArtImgUrl, profileImgUrl;
                Log.e(TAG, "Array Length is : " + jsonArray.length());
                while (count < jsonArray.length()) {

                    JSONObject jsonObject2 = jsonArray.getJSONObject(count);
                    name = jsonObject2.getString("name");
                    phone = jsonObject2.getString("phone");
                    address = jsonObject2.getString("address");
                    qualification = jsonObject2.getString("qualification");
                    gender = jsonObject2.getString("gender");
                    skills = jsonObject2.getString("skills");
                    coverArtImgUrl = jsonObject2.getString("coverArtImgUrl");
                    profileImgUrl = jsonObject2.getString("profileImgUrl");

                    //set the fetched data into the editTexts for default data purpose
                    Log.e(TAG, "Name: " + name + "\n Gender: " + gender + "\n Address: " + address + "\n Phone: " + phone + "\n Qualification: " + qualification + "\n Top 5 Skills: " + skills);
                    if (name != null)
                        edEmpName.setText(name);
                    if (gender != null)
                        edEmpGender.setText(gender);
                    if (address != null)
                        edEmpAddress.setText(address);
                    if (phone != null)
                        edEmpPhone.setText(phone);
                    if (qualification != null)
                        edEmpQualification.setText(qualification);
                    if (skills != null)
                        edEmpTopSkills.setText(skills);

                    //setting image URLs here
                    if (profileImgUrl.equals("null")) {
                        //set default profile image if not provided
                        imgEmpProfile.setBackgroundResource(R.drawable.person);//showing demo image till load
                    } else {
                        //assign the value to respective field
                        Picasso.with(getContext()).load(Uri.parse(profileImgUrl)).into(imgEmpProfile);//assign user customized image
                    }

                    //for cover art image
                    if (coverArtImgUrl.equals("null")) {
                        //set default cover art if not provided
                        imgCoverArt.setBackgroundResource(R.drawable.picture_post3);//showing demo image till load

                    } else {
                        //assign the value to respective field
                        Picasso.with(getContext()).load(Uri.parse(coverArtImgUrl)).into(imgCoverArt);//assign user customized image
                    }
                    count++;
                }//end of while loop
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    class UpdateEmpProfileData extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;
        String empId, name, phone, qualification, gender, address, skills, coverArtImgUrl, profileImgUrl;

        UpdateEmpProfileData(String name, String phone, String qualification, String gender, String address, String skills, String profileImgUrl, String coverArtImgUrl) {
            // for fetching the user_id
            SessionManager sessionManager = new SessionManager(getContext());
            final HashMap<String, String> userDetails = sessionManager.getUserDetails();
//            Log.e(TAG, "UserDetails are : " + userDetails.get("user_id"));
            this.empId = userDetails.get(SessionManager.KEY_EMP_ID);
            Log.e(TAG, "Fetched UserDetails are : " + this.empId);
            this.name = name;
            this.phone = phone;
            this.qualification = qualification;
            this.gender = gender;
            this.address = address;
            this.skills = skills;
            this.coverArtImgUrl = coverArtImgUrl;
            this.profileImgUrl = profileImgUrl;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(), "Updating Your Profile", "Please Wait...", false, false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                //before creating URLConnection Compress and Convert ProfileImg and CoverImg into Strings
                if (bitmapProfile != null) {

                    ByteArrayOutputStream byteArrayOutputStreamObject;
                    byteArrayOutputStreamObject = new ByteArrayOutputStream();
                    bitmapProfile.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
                    byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
                    profileImgUrl = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
                }
                if (bitmapCoverArt != null) {

                    ByteArrayOutputStream byteArrayOutputStreamObject;
                    byteArrayOutputStreamObject = new ByteArrayOutputStream();
                    bitmapCoverArt.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
                    byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
                    coverArtImgUrl = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
                }


                URL url = new URL(API_URLs.updateUserProfileDetailsAPIUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(empId, "UTF-8") + "&" +
                        URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&" +
                        URLEncoder.encode("qualification", "UTF-8") + "=" + URLEncoder.encode(qualification, "UTF-8") + "&" +
                        URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8") + "&" +
                        URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8") + "&" +
                        URLEncoder.encode("img_cover_art_url", "UTF-8") + "=" + URLEncoder.encode(coverArtImgUrl, "UTF-8") + "&" +
                        URLEncoder.encode("top_skills", "UTF-8") + "=" + URLEncoder.encode(skills, "UTF-8") + "&" +
                        URLEncoder.encode("profile_img_url", "UTF-8") + "=" + URLEncoder.encode(profileImgUrl, "UTF-8");

                Log.e(TAG, "data sent is : " + data_string);
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
//                Log.e(TAG, "result is : " + result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result.toString(); //returning result from the web service
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }
            return "Something Went Wrong... Please Try Again!!!!";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            try {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Result is : " + result);
                Thread.sleep(Toast.LENGTH_SHORT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
