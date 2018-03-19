package com.lms.admin.lms;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.DatePicker;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class LeaveRequestFragment extends Fragment {
    private static final String TAG = "LeaveRequestFragment";
    EditText edSubject, edDescription, edDateFrom, edDateTo;
    Button btnSendRequest, btnReset;
    Calendar mDateFrom, mDateTo;
    String formattedDateTo, formattedDateFrom;
    private Spinner spinLeaveType;
    private RequestQueue requestQueue;
    private TextView totalLeaveDays;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning the view --> return view
        return inflater.inflate(R.layout.fragment_leave_request, container, false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSendRequest = getView().findViewById(R.id.btnSendRequest);
        btnReset = getView().findViewById(R.id.btnResetRequest);
        edSubject = getView().findViewById(R.id.ed_leave_subject);
        edDescription = getView().findViewById(R.id.ed_leave_description);
        edDateFrom = getView().findViewById(R.id.edDateFrom);
        edDateTo = getView().findViewById(R.id.edDateTo);
        totalLeaveDays = getView().findViewById(R.id.tvTotalLeaveDays);
        spinLeaveType = getView().findViewById(R.id.leave_type_spinner);
        //volley code to fetch designations here
        //background volley request here
        requestQueue = Volley.newRequestQueue(getContext());
        getLeaveTypesInSpinner();
//        totalLeaveDays = getView().findViewById(R.id.tvTotalLeaveDays);
        //disable the edDateTo to get minDate from the edDateFrom datePicker
        edDateTo.setEnabled(false);
//        final Spin spinLeaveType = getView().findViewById(R.id.leave_type_spinner);
        /*List<String> leaveTypes = new ArrayList<>();

        leaveTypes.add("Medical Leave");
        leaveTypes.add("Casual Leave");
        leaveTypes.add("Other Leave");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, leaveTypes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLeaveType.setAdapter(dataAdapter);
        */
        spinLeaveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //on selecting an item from spinner
                spinLeaveType.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //when no item is selected

            }
        });


        //select leave start date
        edDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateFrom = Calendar.getInstance();
                int year = mDateFrom.get(Calendar.YEAR);
                int month = mDateFrom.get(Calendar.MONTH);
                int day = mDateFrom.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialogFrom = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        mDateFrom.set(selectedYear, selectedMonth, selectedDayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM", Locale.US);
                        formattedDateFrom = dateFormat.format(mDateFrom.getTime());
//                        Log.e(TAG, "Date is " + mDateFrom.getTime());
//                        Log.e(TAG, "Formatted Date is " + formattedDateFrom);
                        edDateFrom.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        edDateFrom.setText(formattedDateFrom);
                        //enable dateTo editText for toDate
                        edDateTo.setEnabled(true);

                    }
                }, year, month, day);
                //setting calender to have future dates only restricting the past dates
                datePickerDialogFrom.getDatePicker().setMinDate(System.currentTimeMillis());
                Long dur = 5184000000L; //for two months duration @@@@@@
                datePickerDialogFrom.getDatePicker().setMaxDate(System.currentTimeMillis() + dur);
                datePickerDialogFrom.show(); //show the datePicker
            }
        });

        //select leave end date
        edDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateTo = Calendar.getInstance();
                int year = mDateTo.get(Calendar.YEAR);
                int month = mDateTo.get(Calendar.MONTH); // months are from 0 to 11
                int day = mDateTo.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialogTo = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        mDateTo.set(selectedYear, selectedMonth, selectedDayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM", Locale.US);
                        formattedDateTo = dateFormat.format(mDateTo.getTime());
                        edDateTo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        edDateTo.setText(formattedDateTo);
                        //calculate the date difference and show it as total days
                        int days = daysBetween(mDateFrom, mDateTo);
                        totalLeaveDays.setText(String.valueOf(days));

                    }
                }, year, month, day);
                datePickerDialogTo.getDatePicker().setMinDate(mDateFrom.getTimeInMillis());
                Long dur = 5184000000L; //for two months duration @@@@@@
                datePickerDialogTo.getDatePicker().setMaxDate(mDateFrom.getTimeInMillis() + dur);
                datePickerDialogTo.show();
            }
        });

        //btnLeaveRequest code here
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code here to
                //...get values from all the fields and store them in the variables
                //#1.first validate the request with fields
                //#2.then save this request into the database table leave details and set status as pending
                //...and show toast to the user that leave request is sent successfully
                //...return the user to the homeFeeds activity
                //#3.then send notification to the senior about the leave request
                //#4.show in my leave details in the recent section with status as pending (from the db)

                //#1. validations
                final String leaveType, leaveSubject, leaveTotalDays, leaveDesc;
                leaveType = spinLeaveType.getSelectedItem().toString();
                leaveSubject = edSubject.getText().toString().trim();
//                leaveFrom = edDateFrom.getText().toString().trim();//just to check if date entered or not
//                leaveTill = edDateTo.getText().toString().trim();//just to check if date entered or not
                leaveTotalDays = totalLeaveDays.getText().toString().trim();
                leaveDesc = edDescription.getText().toString().trim();

                if (TextUtils.isEmpty(leaveType) && TextUtils.isEmpty(leaveSubject) && TextUtils.isEmpty(edDateFrom.getText().toString().trim()) && TextUtils.isEmpty(edDateTo.getText().toString().trim()) && TextUtils.isEmpty(leaveTotalDays)) {
                    //Toast.makeText(getContext(), "Request Values are: leaveType: " + leaveType + " | leaveSubject: " + leaveSubject + " | leaveFrom: " + leaveFrom + " | leaveTill: " + leaveTill + " | totalDays: " + leaveTotalDays + " | leaveDesc: " + leaveDesc, Toast.LENGTH_SHORT).show();
//                    Log.e(TAG,""+"Request Values are: empId: 8 | leaveType: " + leaveType + " | leaveSubject: " + leaveSubject + " | leaveFrom: " + leaveFrom + " | leaveTill: " + leaveTill + " | totalDays: " + leaveTotalDays + " | leaveDesc: " + leaveDesc);
                    Toast.makeText(getContext(), "fill all the values to continue", Toast.LENGTH_SHORT).show();
                    return;
                }
                //all looks good continue
                //#2. save the details in the database leave_details and set values as
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
                        .setTitle("Confirm Leave Request")
                        .setMessage("Are You Sure You Want A Leave?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Request for the Leave


                                //get emp_id because only signed in users can request for leave
                                String empId = new SessionManager(getContext()).getEmpId();
                                Log.e(TAG, "empId is: " + empId);


                                //get request date and fromDate and toDate in the database date format
                                String leaveFrom = changeDateFormat(mDateFrom.getTime());
                                String leaveTill = changeDateFormat(mDateTo.getTime());
                                String requestDate = changeDateFormat(new Date());
                                //Log.e(TAG,"new leaveFrom is:"+leaveFrom);
                                //Log.e(TAG,"new leaveTill is:"+leaveTill);
                                //Log.e(TAG,"requestDate is: "+requestDate);

                                //call the API to save these details into the database
                                //invoke the function with the API call with volley
                                saveLeaveDetails(empId, leaveType, leaveSubject, leaveFrom, leaveTill, leaveTotalDays, leaveDesc, requestDate);
                                //show home posts fragment after background request
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

        //btnReset code here
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code to reset the form
                http:
//localhost/hrms_app/push_notifications.php
                //CALL THE METHOD HERE
                sendNotification();
            }
        });

    }


    private void sendNotification() {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Getting you on board", "Please Wait...", false, false);
//        final StringRequest request = new StringRequest(Request.Method.POST, API_URLs.verifyEmailAPIUrl,null, new Response.Listener<JSONObject>() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URLs.pushNotificationsAPIUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e(TAG, "response is: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "Error : " + error);
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Volley Network Error... Please Try again Later...", Toast.LENGTH_SHORT).show();
            }
        });
        //for default retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


    private void getLeaveTypesInSpinner() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Getting you on board", "Please Wait...", false, false);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_URLs.getLeaveTypesAPIUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<String> leaveTypesList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("server_response");
                    Log.e(TAG, "Result is: " + jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String leaveTypeName = jsonObject.getString("type_name");
                        leaveTypesList.add(leaveTypeName);
                    }
                    ArrayAdapter<String> dataAdapter;
                    dataAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, leaveTypesList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinLeaveType.setAdapter(dataAdapter);
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Error in fetching details...Please Try again Later...", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "JSON Exception Here :" + e);
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

    //function to saveLeaveDetails into the db
    private void saveLeaveDetails(final String empId, final String leaveType, final String leaveSubject, final String leaveFrom, final String leaveTill, final String leaveTotalDays, final String leaveDesc, final String requestDate) {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Saving Leave Details", "Please Wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URLs.saveLeaveDetailsAPIUrl, new Response.Listener<String>() {
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
                params.put("leaveType", leaveType);
                params.put("leaveSubject", leaveSubject);
                params.put("leaveFrom", leaveFrom);
                params.put("leaveTill", leaveTill);
                params.put("leaveTotalDays", leaveTotalDays);
                params.put("leaveDesc", leaveDesc);
                params.put("requestDate", requestDate);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    //function to calculate total leave days from two given dates
    public int daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        int totalLeaveDuration = (int) TimeUnit.MILLISECONDS.toDays(Math.abs(end - start) + 1);
//        return (int) TimeUnit.MILLISECONDS.toDays(Math.abs(end - start)+1);

        //logic to exclude weekends from total leave days
        int day1 = startDate.get(Calendar.DAY_OF_WEEK);
//        Log.e(TAG, "startDate Day is : " + day1);
        int totalWorkingDays = 0;
        for (int i = 0; i <= totalLeaveDuration; i++, day1++) {
            if (day1 != Calendar.SATURDAY && day1 != Calendar.SUNDAY) {
                totalWorkingDays += 1;//=> ++
            }
            if (day1 == 7) {
                day1 = 0;
            }
        }
        return totalWorkingDays;

    }

    private String changeDateFormat(Date date) {
        //format date to database format
        SimpleDateFormat dateFormatDB = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return dateFormatDB.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
/*
    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        int workDays = 0;

        Calendar c = Calendar.getInstance();
        c.setTime(d1);
        int day1 = c.get(Calendar.DAY_OF_WEEK);
        //logic to exclude weekends from total leave days
        Log.e(TAG,"d1 time is : "+day1);


        Log.e(TAG,"d2 time is : "+d2.getTime());

//        Log.e(TAG,"working days are : "+diff);
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static int daysBetween(Calendar startCal, Calendar endCal) {

        int workDays = 0;
//        Log.e(TAG,"start date is: "+startCal.getTime());
//        Log.e(TAG,"end date is : "+endCal.getTime());

        //Return 0 if start and end are the same
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 1;
        }

        *//*if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }*//*
        Calendar tempCal = startCal;

        do {
            //excluding start date
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
//            Log.e(TAG,"day of month is : "+startCal.get(Calendar.DAY_OF_WEEK));
            if (tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
        } while (tempCal.getTimeInMillis() <= endCal.getTimeInMillis()); //excluding end date
//        Log.e(TAG,"working days are : "+workDays);
        return workDays;
    }
   */


}