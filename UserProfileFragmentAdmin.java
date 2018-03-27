package com.lms.admin.lms;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfileFragmentAdmin extends Fragment {
    private static final String TAG = "UsrProfileFragAdmin";
    private RequestQueue requestQueue;
    private TextView tvEmpName, tvEmpGender, tvEmpTitle, tvEmpEmail, tvEmpPhone, tvEmpAddress, tvEmpReportingTo, tvEmpDateOfJoining, tvEmpTopSkills, tvEmpQualification;
    private ImageView imgCoverArt;
    private CircleImageView imgEmpProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile_fragment_admin, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get all the values here
        tvEmpName = getView().findViewById(R.id.tv_profile_emp_name_admin);
        tvEmpGender = getView().findViewById(R.id.tv_profile_gender_admin);
        tvEmpTitle = getView().findViewById(R.id.tv_profile_emp_designation_admin);
        tvEmpEmail = getView().findViewById(R.id.tv_profile_emp_email_admin);
        tvEmpPhone = getView().findViewById(R.id.tv_profile_emp_phone_admin);
        tvEmpAddress = getView().findViewById(R.id.tv_profile_address_admin);
        tvEmpReportingTo = getView().findViewById(R.id.tv_profile_reporting_to_admin);
        tvEmpDateOfJoining = getView().findViewById(R.id.tv_profile_date_of_joining_admin);
        tvEmpQualification = getView().findViewById(R.id.tv_profile_emp_qualification_admin);
        tvEmpTopSkills = getView().findViewById(R.id.tv_profile_top_skils_admin);
        imgCoverArt = getView().findViewById(R.id.img_cover_art_admin);
        imgEmpProfile = getView().findViewById(R.id.img_profile_image_admin);

        Bundle arguments = getArguments();
        assert arguments != null;
        String empId = arguments.getString("myEmpID"); //getting the empId of the selected employee
        Log.e(TAG, "desired Emp Id: " + empId);

        //when view is created then fetch values from db and display it here
        //also make sure only values existing are to be shown
        //hide other layouts completely
        //execute the background task and perform desired operations
        //generate new volley background request here
        requestQueue = Volley.newRequestQueue(getContext());
        //Call the function to upload the post to the server
        Log.e(TAG, "Selected Emp_id is :> " + empId); //pass ID of the selected emp here
        Log.e(TAG, "Calling volley request");
        getUserProfileDetails(empId);//passing empId to fetch profile details


    }

    //volley background code to fetch emp details in the spinner
    private void getUserProfileDetails(final String empId) {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Getting you on board", "Please Wait...", false, false);
        StringRequest request = new StringRequest(Request.Method.POST, API_URLs.getUserProfileDetailsAPIUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "ServerResponse is: " + response);
                try {
                    Log.e(TAG, "ServerResponse is: " + response);
                    if (response.trim().equals("NoRecordFound")) {
                        Log.e(TAG, "No Profile Found Show Dummy profile here");
                        progressDialog.dismiss();
                        //showing dummy image when no image available
//                        Picasso.with(getContext()).load(R.drawable.person).into(actionBarUserImg);
//                        Picasso.with(UserHomeActivity.this).load(R.drawable.person).into(navHeaderUserImg);
                    } else {
                        JSONObject jsonObjectResponse = new JSONObject(response);
                        String name, title, email, phone, qualification, gender, address, dateOfJoining, reportingTo, skills, coverArtImgUrl, profileImgUrl;
                        JSONArray jsonArray = jsonObjectResponse.getJSONArray("server_response");
                        Log.e(TAG, "profile<><><><> details are: " + jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            name = jsonObject.getString("name");
                            title = jsonObject.getString("title");
                            email = jsonObject.getString("email");
                            phone = jsonObject.getString("phone");
                            address = jsonObject.getString("address");
                            qualification = jsonObject.getString("qualification");
                            gender = jsonObject.getString("gender");
                            dateOfJoining = jsonObject.getString("dateOfJoining");
                            reportingTo = jsonObject.getString("reportingTo");
                            skills = jsonObject.getString("skills");
                            coverArtImgUrl = jsonObject.getString("coverArtImgUrl");
                            profileImgUrl = jsonObject.getString("profileImgUrl");

//                    checking if any value is null then hide that layout from the main screen
                            if (name.equals("null")) {
                                Log.e(TAG, "name is Null");
                                getView().findViewById(R.id.layout_emp_name).setVisibility(View.GONE);
                            } else {
                                tvEmpName.setText(name);
                            }

                            if (address.equals("null")) {
                                getView().findViewById(R.id.layout_emp_address).setVisibility(View.GONE);
                            } else {
                                tvEmpAddress.setText(address);
                            }

                            if (title.equals("null")) {
                                Log.e(TAG, "title is Null");
                                getView().findViewById(R.id.layout_emp_title).setVisibility(View.GONE);
                            } else {
                                tvEmpTitle.setText(title);
                            }
                            if (gender.equals("null")) {
                                getView().findViewById(R.id.layout_emp_gender).setVisibility(View.GONE);
                            } else {
                                tvEmpGender.setText(gender);
                            }

                            if (email.equals("null")) {
                                getView().findViewById(R.id.layout_emp_email).setVisibility(View.GONE);
                            } else {
                                tvEmpEmail.setText(email);
                            }

                            if (qualification.equals("null")) {
                                getView().findViewById(R.id.layout_emp_qualification).setVisibility(View.GONE);
                            } else {
                                tvEmpQualification.setText(qualification);
                            }

                            if (phone.equals("null")) {
                                getView().findViewById(R.id.layout_emp_phone).setVisibility(View.GONE);
                            } else {
                                tvEmpPhone.setText(phone);
                            }

                            if (dateOfJoining.equals("null")) {
                                getView().findViewById(R.id.layout_emp_date_of_joining).setVisibility(View.GONE);
                            } else {
                                tvEmpDateOfJoining.setText(dateOfJoining);
                            }

                            if (skills.equals("null")) {
                                getView().findViewById(R.id.layout_emp_top_skills).setVisibility(View.GONE); //hiding the unavailable layout fields
                            } else {
                                tvEmpTopSkills.setText(skills);
                            }
                            if (reportingTo.equals("null")) {
                                getView().findViewById(R.id.layout_emp_reporting_to).setVisibility(View.GONE);
                            } else {
                                tvEmpReportingTo.setText(reportingTo);
                            }
                            if (coverArtImgUrl.equals("null")) {
                                Picasso.with(getContext()).load(R.drawable.picture_post2).into(imgCoverArt);//showing dummy image when no image available
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
}
