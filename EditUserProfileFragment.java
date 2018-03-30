package com.lms.admin.lms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserProfileFragment extends Fragment {
    private static final String TAG = "EditUserProfileFragment";
    private static final int PICK_COVER_IMAGE = 9;
    String name, phone, qualification, gender, address, skills, coverArtImgUrl, profileImgUrl;
    Bitmap bitmapProfile, bitmapCoverArt;
    private EditText edEmpName, edEmpGender, edEmpPhone, edEmpAddress, edEmpTopSkills, edEmpQualification;
    private ImageView imgCoverArt;
    private CircleImageView imgEmpProfile;
    private RequestQueue requestQueue;


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

        //when view is created then fetch values from db and display it here
        //also make sure only values existing are to be shown
        //hide other layouts completely
        //execute the background task and perform desired operations
        //generate new volley background request here
        requestQueue = Volley.newRequestQueue(getContext());
        //Call the function to upload the post to the server
        Log.e(TAG, "My Emp_id :> " + new SessionManager(getContext()).getEmpId());
        Log.e(TAG, "Calling volley request");
        getUserProfileDetails(new SessionManager(getContext()).getEmpId());//passing empId to fetch user details//call the background task here to fetch initial data here if available

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
//        setting the pencil buttons
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


                                if (!TextUtils.isEmpty(edEmpName.getText().toString().trim())) {

                                    name = edEmpName.getText().toString().trim();
                                } else {
                                    //set qualification to null
                                    name = "null";
                                }

                                if (!TextUtils.isEmpty(edEmpGender.getText().toString().trim())) {
                                    gender = edEmpGender.getText().toString().trim();
                                } else {
                                    //set qualification to null
                                    gender = "null";
                                }

                                if (!TextUtils.isEmpty(edEmpQualification.getText().toString().trim())) {
                                    Log.e(TAG, "edEmpQualification is NOT Empty");
                                    qualification = edEmpQualification.getText().toString().trim();
                                } else {
                                    //set qualification to null
                                    qualification = "null";
                                }

                                if (!TextUtils.isEmpty(edEmpPhone.getText().toString().trim())) {
                                    phone = edEmpPhone.getText().toString().trim();
                                } else {
                                    //set qualification to null
                                    phone = "null";
                                }

                                if (!TextUtils.isEmpty(edEmpTopSkills.getText().toString().trim())) {
                                    Log.e(TAG, "edEmpTopSkills is NOT Empty");
                                    skills = edEmpTopSkills.getText().toString().trim();
                                } else {
                                    //set qualification to null
                                    skills = "null";
                                }

                                if (!TextUtils.isEmpty(edEmpAddress.getText().toString().trim())) {
                                    address = edEmpAddress.getText().toString().trim();
                                } else {
                                    //set qualification to null
                                    address = "null";
                                }

                                //before creating URLConnection Compress and Convert ProfileImg and CoverImg into Strings
                                if (bitmapProfile != null) {
                                    ByteArrayOutputStream byteArrayOutputStreamObject;
                                    byteArrayOutputStreamObject = new ByteArrayOutputStream();
                                    bitmapProfile.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
                                    byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
                                    profileImgUrl = Base64.encodeToString(byteArrayVar, Base64.DEFAULT); //when profile image is updated
                                }
                                if (bitmapCoverArt != null) {
                                    ByteArrayOutputStream byteArrayOutputStreamObject;
                                    byteArrayOutputStreamObject = new ByteArrayOutputStream();
                                    bitmapCoverArt.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
                                    byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
                                    coverArtImgUrl = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);//when profile image is updated
                                }
//                              //generate new volley background request here
                                requestQueue = Volley.newRequestQueue(getContext());
                                //Call the function to upload the post to the server
                                Log.e(TAG, "My Emp_id :> " + new SessionManager(getContext()).getEmpId());
                                Log.e(TAG, "Calling volley request");
                                updateUserProfileDetails(new SessionManager(getContext()).getEmpId(), name, phone, qualification, gender, address, skills, profileImgUrl, coverArtImgUrl);//passing empId to update user details

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

    //volley background code to fetch emp profile details
    private void getUserProfileDetails(final String empId) {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Getting you on board", "Please Wait...", false, false);
        StringRequest request = new StringRequest(Request.Method.POST, API_URLs.getUserProfileDetailsAPIUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "ServerResponse is: " + response);
                try {
                    Log.e(TAG, "ServerResponse is: " + response);
                    if (response.trim().equals("NoRecordFound")) {
                        Log.e(TAG, "No Profile Found Show Dummy here");
                        //showing dummy image when no image available
//                        Picasso.with(getContext()).load(R.drawable.person).into(actionBarUserImg);
//                        Picasso.with(UserHomeActivity.this).load(R.drawable.person).into(navHeaderUserImg);
                    } else {
                        JSONObject jsonObjectResponse = new JSONObject(response);
                        String email;
                        JSONArray jsonArray = jsonObjectResponse.getJSONArray("server_response");
                        Log.e(TAG, "profile<><><><> details are: " + jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            name = jsonObject.getString("name");
//                            email = jsonObject.getString("email");//get if want to print email address
                            phone = jsonObject.getString("phone");
                            address = jsonObject.getString("address");
                            qualification = jsonObject.getString("qualification");
                            gender = jsonObject.getString("gender");
                            skills = jsonObject.getString("skills");
                            coverArtImgUrl = jsonObject.getString("coverArtImgUrl");
                            profileImgUrl = jsonObject.getString("profileImgUrl");

//                    checking if any value is null then show empty EditText on the screen
                            if (name.equals("null")) {
                                Log.e(TAG, "name is Null");
                                edEmpName.setText("");
                            } else {
                                edEmpName.setText(name);
                            }

                            if (gender.equals("null")) {
                                Log.e(TAG, "gender is Null");
                                edEmpGender.setText("");
                            } else {
                                edEmpGender.setText(gender);
                            }

                            if (phone.equals("null")) {
                                Log.e(TAG, "phone is Null");
                                edEmpPhone.setText("");
                            } else {
                                edEmpPhone.setText(phone);
                            }

                            if (address.equals("null")) {
                                edEmpAddress.setText("");

                            } else {
                                edEmpAddress.setText(address);
                            }

                            if (skills.equals("null")) {
                                edEmpTopSkills.setText("");//set empty value here
                            } else {
                                edEmpTopSkills.setText(skills);
                            }

                            if (qualification.equals("null")) {
                                edEmpQualification.setText("");//   set empty value here
                            } else {
                                edEmpQualification.setText(qualification);
                            }

                            if (coverArtImgUrl.equals("null")) {
                                Picasso.with(getContext()).load(R.drawable.picture_post3).into(imgCoverArt);//showing dummy image when no image available
                            } else {
                                Picasso.with(getContext()).load(coverArtImgUrl).into(imgCoverArt);//assign user customized image
                            }

                            if (profileImgUrl.equals("null")) {
                                Picasso.with(getContext()).load(R.drawable.person).into(imgEmpProfile);//showing dummy image when no image available
                            } else {
                                Picasso.with(getContext()).load(profileImgUrl).into(imgEmpProfile);//assign user customized image
                            }
                        }
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Json Exception: " + e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "Server returns error response");
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Network Error... Please Try again Later...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("emp_id", empId);
                Log.e(TAG, "params : " + params.toString());
                return params;
            }
        };
        //for default retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);//add the request to requestQueue queue
    }

    //volley background code to update emp profile details
    private void updateUserProfileDetails(final String empId, final String name, final String phone, final String qualification, final String gender, final String address, final String skills, final String profileImgUrl, final String coverArtImgUrl) {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Updating User Profile", "Please Wait...", false, false);
        StringRequest request = new StringRequest(Request.Method.POST, API_URLs.updateUserProfileDetailsAPIUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e(TAG, "UpdateResponse is: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "Server returns error response");
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Network Error... Please Try again Later...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                /*
                //check if all values are set or any null value exist
                Log.e(TAG, "emp_id is: " + empId);
                if (empId != null)
                    params.put("emp_id", empId);

                Log.e(TAG, "name is: " + name);
                if (name != null)
                    params.put("name", name);

                Log.e(TAG, "phone is: " + phone);
                if (phone != null)
                    params.put("phone", phone);

                Log.e(TAG, "qualification is: " + qualification);
                if (qualification != null)
                    params.put("qualification", qualification);

                Log.e(TAG, "gender is: " + gender);
                if (gender != null)
                    params.put("gender", gender);

                Log.e(TAG, "address is: " + address);
                if (address != null)
                    params.put("address", address);

                Log.e(TAG, "coverArtImgUrl is: " + coverArtImgUrl);
                if (coverArtImgUrl != null)
                    params.put("img_cover_art_url", coverArtImgUrl);

                Log.e(TAG, "skills is: " + skills);
                if (skills != null)
                    params.put("top_skills", skills);

                Log.e(TAG, "profileImgUrl is: " + profileImgUrl);
                if (profileImgUrl != null)
                    params.put("profile_img_url", profileImgUrl);
                Log.e(TAG, "||params :> " + params.toString());
                */
                params.put("emp_id", empId);
                params.put("name", name);
                params.put("gender", gender);
                params.put("qualification", qualification);
                params.put("phone", phone);
                params.put("address", address);
                params.put("top_skills", skills);
                params.put("profile_img_url", profileImgUrl);
                params.put("img_cover_art_url", coverArtImgUrl);
                Log.e(TAG, "||params :> " + params.toString());
                return params;
            }
        };
        //for default retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);//add the request to requestQueue queue
    }

    /*
    private void uploadNewPost() {

        final ProgressDialog progressDialog = ProgressDialog.show(this, "Uploading new Post...", "Please Wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URLs.imgUploadToServerAPIUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(StoriesUploadActivity.this, response, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "ServerResponse is: " + response);
                startActivity(new Intent(StoriesUploadActivity.this, ManagePostActivity.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //HashMap for values
                HashMap<String, String> params = new HashMap<>();

                //finding values to be stored in the database
                Calendar cal = Calendar.getInstance();
                //creating Locale object for date formats
                Locale myLocale = new Locale("en", "in");
//                Locale standardLocale = Locale.US;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", myLocale);
                String mCurrentDate = sdf.format(cal.getTime());
                sdf = new SimpleDateFormat("HH:mm:ss", myLocale); //using the same dateFormatter to get the time as well
                String mCurrentTime = sdf.format(cal.getTime());

                //get emp_id from the shared preference obj
                HashMap<String, String> userDetails = sessionManager.getUserDetails();

//              Log.e(TAG, "current Date is : " + mCurrentDate);
//              Log.e(TAG, "current Time is : " + mCurrentTime);
                String txtMsg = edPostTextMsg.getText().toString().trim();
                String titleMsg = edPostTitle.getText().toString().trim();
                Log.e(TAG, "txtTitle is : " + titleMsg + "| txtMsg is: " + txtMsg);
                if (txtMsg.isEmpty()) {
                    txtMsg = "null";
                }
                if (titleMsg.isEmpty()) {
                    titleMsg = "null";
                }
//              Log.e(TAG, "emp_id is : " + userDetails.get("emp_id"));
                //add more values here to be uploaded to the server
                params.put("date", mCurrentDate);
                params.put("times", mCurrentTime);
                params.put("text_message", txtMsg);
                params.put("title_message", titleMsg);
                params.put("image_url", imageByteString);
                params.put("added_by", userDetails.get("emp_id"));
                Log.e(TAG, "params are : " + params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    */

/*
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
*/

}
