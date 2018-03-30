package com.lms.admin.lms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EditEmpDetailsFragment extends Fragment {
    private static final String TAG = "EditEmpDetailsFrag";
    ImageView imgCoverArt, imgEmpProfile;
    Spinner spDesignation, spReportingTo;
    ProgressBar progressBar;
    TextView tvEmpName;
    EditText edEmail;
    Button btnUpdate;
    List<SpinnerEmpDetails> myEmpList;
    List<SpinnerEmpDetails> list;
    private RequestQueue requestQueue;
    private String empId, name, title, email, reportingTo, profileImgUrl, coverArtImgUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_emp_details, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //volley code to fetch designations here
        //background volley request here
        requestQueue = Volley.newRequestQueue(getContext());
        parseJSONDesignations();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //fetching the default values of the user profile
        requestQueue = Volley.newRequestQueue(getContext());
        //Call the function to upload the post to the server
        Bundle arguments = getArguments();
        assert arguments != null;
        empId = arguments.getString("myEmpID"); //getting the empId of the selected employee
        Log.e(TAG, "desired Emp Id in Edit is : " + empId);
        Log.e(TAG, "Calling volley request");
        getUserProfileDetails(empId);//passing empId to fetch user details//call the background task here to fetch initial data here if available

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgEmpProfile = getView().findViewById(R.id.img_edit_profile_image);
        imgCoverArt = getView().findViewById(R.id.img_edit_cover_art);
        spDesignation = getView().findViewById(R.id.spEmpDesignation_admin_edit);
        spReportingTo = getView().findViewById(R.id.sp_emp_names_admin_edit);
        tvEmpName = getView().findViewById(R.id.tvEmpName_admin_edit);
        progressBar = getView().findViewById(R.id.progress_bar_fetch_designation);
        edEmail = getView().findViewById(R.id.ed_edit_emp_email);
        btnUpdate = getView().findViewById(R.id.btn_save_profile_admin);
        final SpinnerHelper spinnerHelper = new SpinnerHelper(getContext(), spReportingTo);
        spinnerHelper.setSpinnerLayout(1000);
        spinnerHelper.fetchJSONEmpNames();


        //new code....

        ManageEmpDetailsActivity activity = (ManageEmpDetailsActivity) getActivity();
        list = activity.sHelper.empNamesList;
        Log.e(TAG, "myList: " + list);


        //till here....


//        List<SpinnerEmpDetails> items = ((SpinnerCustomAdapter)spReportingTo.getAdapter()).emp();
//        myEmpList = spinnerHelper.empNamesList;
//        String repo = spinnerHelper.dataAdapter.empGlobalId;
//        Log.e(TAG,"I am Reporting to: "+repo);

        spReportingTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //on selecting an item from spinner
                spReportingTo.setSelection(position);
                reportingTo = spinnerHelper.dataAdapter.empGlobalId;
                Log.e(TAG, "reporting to: " + reportingTo);
                //fetch the record of employee whose name is selected in the spinner
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //when no item is selected
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code here to update user Profile details
                //get all the values
                //validate the values
                //if validated then show alertDialog to confirm profile update
                //#1. validations
                final String empTitle, empEmail, empReportingToId;
                Log.e(TAG, "empId is: " + empId);
                empTitle = spDesignation.getSelectedItem().toString();
                Log.e(TAG, "empTitle is:" + empTitle);
                empEmail = edEmail.getText().toString().trim();
                Log.e(TAG, "empEmail is:" + empEmail);
//                empReportingToId = spReportingTo.getSelectedItem().toString();//get the Id of the selected User
                //empReportingToId = spReportingTo.getSelectedItem().toString();//get the Id of the selected User
                Log.e(TAG, "empReportingToId is:" + reportingTo);
                empReportingToId = reportingTo;//setting the reporting to of the emp

                //Log.e(TAG,"empReportingToId is:"+empReportingToId);


                if (!TextUtils.isEmpty(empTitle) && TextUtils.isEmpty(empEmail) && TextUtils.isEmpty(empReportingToId)) {
                    //Toast.makeText(getContext(), "Request Values are: leaveType: " + leaveType + " | leaveSubject: " + leaveSubject + " | leaveFrom: " + leaveFrom + " | leaveTill: " + leaveTill + " | totalDays: " + leaveTotalDays + " | leaveDesc: " + leaveDesc, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "fill all the values to continue", Toast.LENGTH_SHORT).show();
                    return;
                }
                //all looks good continue
                //#2. save the details in the database tbl_employees and set values as
                // :> emp_id, leave_type_id,subject, date_from, date_to, total_days, description, status, request_date, response_date, response_from,total_leaves_remaining <:
                // get specified values here
                //get emp_id from the session_manager,
                //get leaveType_id from ref_leave_type
                //set status as pending
                //set request date as today's date
                //set response date and response_from as null
                //don't change total_leaves_remaining as it will be changed at the time of leave approval

                // #1.call the alertDialog for confirmation of registration
                new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme)
                        .setIcon(R.drawable.ic_warning_white)
                        .setTitle("Confirm Profile Update")
                        .setMessage("Are You Sure You Want to Update Employee Profile?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Request for the Leave
                                //get emp_id of the employee
                                //we have that value in the empId

                                //call the API to save these details into the database
                                //invoke the function with the API call with volley
                                updateEmpDetailsAdmin(empId, empTitle, empEmail, empReportingToId);

                                //startActivity(new Intent(RegisterNewEmpActivity.this, AdminDashboardActivity.class));
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
    }

    private void updateEmpDetailsAdmin(final String empId, final String empTitle, final String empEmail, final String empReportingToId) {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Saving Employee Details", "Please Wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URLs.updateEmpDetailsAdminAPIUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "ServerResponse is: " + response);
                //move to main fragment showing posts
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Network Error... Please Try again Later...", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error is: " + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("empId", empId);
                params.put("empTitle", empTitle);
                params.put("empEmail", empEmail);
                params.put("empReportingToId", empReportingToId);
                Log.e(TAG, "paramas sent: " + params);

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }


    //volley background code to fetch available designations in the spinner
    private void parseJSONDesignations() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Getting you on board", "Please Wait...", false, false);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_URLs.getDesignationsAPIUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<String> designationList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("server_response");
                    Log.e(TAG, "Result is: " + jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String designationName = jsonObject.getString("name");
                        designationList.add(designationName);
                    }
                    ArrayAdapter<String> dataAdapter;
                    dataAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, designationList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spDesignation.setAdapter(dataAdapter);
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error in fetching details...Please Try again Later...", Toast.LENGTH_SHORT).show();
            }
        });
        //for default retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
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
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "No Records Found...", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "No Profile Found Show Dummy here");
                        //showing dummy image when no image available
//                        Picasso.with(getContext()).load(R.drawable.person).into(actionBarUserImg);
//                        Picasso.with(UserHomeActivity.this).load(R.drawable.person).into(navHeaderUserImg);
                    } else {
                        JSONObject jsonObjectResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonObjectResponse.getJSONArray("server_response");
                        Log.e(TAG, "profile<><><><> details are: " + jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            empId = jsonObject.getString("emp_id");
                            name = jsonObject.getString("name");
                            email = jsonObject.getString("email");//get if want to print email address
                            title = jsonObject.getString("title");
                            reportingTo = jsonObject.getString("reporting_to_emp_id");
                            coverArtImgUrl = jsonObject.getString("coverArtImgUrl");
                            profileImgUrl = jsonObject.getString("profileImgUrl");

//                    checking if any value is null then hide that layout from the main screen
                            if (name.equals("null")) {
                                Log.e(TAG, "name is Null");
                                tvEmpName.setText("Mr. Employee");
                            } else {
                                tvEmpName.setText(name);
                            }
                            if (email.equals("null")) {
                                Log.e(TAG, "email is Null");
                                edEmail.setText("");
                            } else {
                                edEmail.setText(email);
                            }
                            if (title.equals("null")) {
                                Log.e(TAG, "title is Null");
                                //in this case default will be set as the value
//                                spDesignation.setText(""); spinner designation selection code here
                            } else {
                                Log.e(TAG, "my title is: " + title);
                                spDesignation.setSelection(getIndex(spDesignation, title));
                            }
                            if (reportingTo.equals("null")) {
                                Log.e(TAG, "reportingTo is Null");
//                                spReportingTo.setText("");// spinner reportingTo selection code here
                            } else {
                                Log.e(TAG, "my reporting to is: " + reportingTo);
                                Log.e(TAG, "spReportingTo Size is: " + spReportingTo.getCount());
//                                spReportingTo.setSelection(setReportingTo(myEmpList,reportingTo));
                                spReportingTo.setSelection(setReportingTo(list, reportingTo));

//                                spReportingTo.setSelection(Integer.parseInt(reportingTo));
//                                spReportingTo.setSelection(getIndexFromObject(spReportingTo, reportingTo));

//                                tvEmpName.setText(reportingTo);
                            }
                            if (coverArtImgUrl.equals("null")) {
                                Picasso.with(getContext()).load(R.drawable.picture_post3).into(imgCoverArt);//showing dummy image when no image available
                            } else {
                                Picasso.with(getContext()).load(coverArtImgUrl).into(imgCoverArt);//assign user customized image
                            }

                            Log.e(TAG, "profileImgUrl: " + profileImgUrl);
                            if (profileImgUrl.equals("null")) {
                                Log.e(TAG, "image profile is null so show dummy person");
                                Picasso.with(getContext()).load(R.drawable.person).into(imgEmpProfile);//showing dummy image when no image available
                            } else {
                                Picasso.with(getContext()).load(profileImgUrl).into(imgEmpProfile);//assign user customized image
                            }
                        }
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Json Exception: " + e);
                    Toast.makeText(getContext(), "Something went wrong...try again...", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
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

    //private method of your class
    private int getIndex(Spinner spinner, String myString) {
        Log.e(TAG, "my title is: " + myString);
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        //returning value is
        Log.e(TAG, "my title is: " + myString + " 's index is: " + index);
        return index;
    }


    //private method of your class
    private int setReportingTo(List<SpinnerEmpDetails> myEmpList, String reportingTo) {
        Log.e(TAG, "my Reporting To is: " + reportingTo);
//        int index = 0;
//            Log.e(TAG,"selected item is: "+);

        for (int i = 0; i < myEmpList.size(); i++) {
            if (myEmpList.get(i).getEmpId().equals(reportingTo)) {
                Log.e(TAG, "returning index: " + i);
                return i;
            }
        }
        return -1;
/*//            Log.e(TAG, "spinner item at position " + i + " is " + spinner.getItemAtPosition(i).toString());
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        //returning value is
        Log.e(TAG, "my title is: " + myString + " 's index is: " + index);
        return index;*/
    }


}
