package com.lms.admin.lms;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyLeaveSummaryFragment extends Fragment {
    private static final String TAG = "MyLeaveSummaryFragment";
    RecyclerView mRecyclerView, mSummaryRecyclerView;
    List<PendingLeaveRequest> pendingRequestsList, leaveRequestSummary;
    PendingRequestsAdapter pendingRequestsAdapter;
    RequestSummaryAdapter leaveRequestsSummaryAdapter;
    private RequestQueue requestQueuePendingRequests;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_leave_summary, container, false);

        //for pending leave requests
        Log.e(TAG, "pendingLeaveRequest Created");
        pendingRequestsList = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.pending_requests_user_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); //for Vertical recycler view
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false)); //for horizontal recycler view

        //for leave summary
        leaveRequestSummary = new ArrayList<>();
        mSummaryRecyclerView = view.findViewById(R.id.leave_summary_user_list_recycler_view);
        mSummaryRecyclerView.setHasFixedSize(true);
        mSummaryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //load all the leave requests that has status as pending emp_id = emp_id
        //load them in a recycler_view
        //call volley background request here
        //for pending leave requests
        requestQueuePendingRequests = Volley.newRequestQueue(getContext());
        Log.e(TAG, "empId is:> " + new SessionManager(getContext()).getEmpId());
        parseJSONPendingRequests();

        //call volley background request here
        //for leave summary
//        requestQueuePendingRequests = Volley.newRequestQueue(getContext());
        Log.e(TAG, "Calling Summary Request here:> ");
        parseJSONRequestsSummary();

    }

    //volley background code to fetch pending leave requests in the recyclerView
    private void parseJSONPendingRequests() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Getting you on board", "Please Wait...", false, false);
        final StringRequest request = new StringRequest(Request.Method.POST, API_URLs.getUserPendingLeaveRequestsAPIUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(TAG, "jsonArray is: " + response);
                    if (response.trim().equals("NoRecordsFound")) {
                        Log.e(TAG, "No Records Found Show some Image here");
                        //hide the recycler view and display the empty screen text and icon
                        mRecyclerView.setVisibility(View.GONE);
//                        getView().findViewById(R.id.tv_empty_leave_summary_view).setVisibility(View.VISIBLE);
//                        getView().findViewById(R.id.img_empty_view).setVisibility(View.VISIBLE);
                    } else {
                        JSONObject jsonObjectResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonObjectResponse.getJSONArray("server_response");
                        String request_id, reportingTo, leaveType, fromDate, toDate, totalDays, subject, description, profile_image_url = "null", requestDate;

                        Log.e(TAG, "Pending Requests are: " + jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            request_id = jsonObject.getString("leave_detail_id");
                            reportingTo = jsonObject.getString("reporting_to");
                            leaveType = jsonObject.getString("leave_type");
                            fromDate = jsonObject.getString("date_from");
                            toDate = jsonObject.getString("date_to");
                            totalDays = jsonObject.getString("total_days");
                            subject = jsonObject.getString("subject");
                            description = jsonObject.getString("description");
                            requestDate = jsonObject.getString("request_date");

                            //use DateHelper Class to convert date formats call the convertDateFormat() function to convert the dates from db format to the ui format
//                            Log.e(TAG, "old Date is: " + fromDate);
                            fromDate = new DateHelper().convertDateToUI(fromDate);
//                            Log.e(TAG, "new Date is: " + fromDate);

                            toDate = new DateHelper().convertDateToUI(toDate);
                            requestDate = new DateHelper().convertDateToUI(requestDate);

                            //set this to the Adapter
                            Log.e(TAG, "creating new pendingLeaveRequest");
                            PendingLeaveRequest pendingLeaveRequest = new PendingLeaveRequest(request_id, reportingTo, leaveType, fromDate, toDate, totalDays, subject, description, profile_image_url, requestDate);
                            pendingRequestsList.add(pendingLeaveRequest);
//                            Log.e(TAG,"pendingRequestsList is: "+pendingRequestsList.toString());
                            pendingRequestsAdapter = new PendingRequestsAdapter(getContext(), pendingRequestsList);
                            mRecyclerView.setAdapter(pendingRequestsAdapter);
                        }
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Network Error... Please Try again Later...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                Log.e(TAG, "Setting Params here : ");
//                Log.e(TAG, "emp_id is: " + params.toString());
                params.put("emp_id", new SessionManager(getContext()).getEmpId());//get myEmpId and Send it for further operations
                return params;
            }
        };
        //for default retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueuePendingRequests.add(request);
    }


    //volley background code to fetch leave requests summary in the recyclerView
    private void parseJSONRequestsSummary() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Getting you on board", "Please Wait...", false, false);
        final StringRequest request = new StringRequest(Request.Method.POST, API_URLs.getLeaveRequestsSummaryAPIUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(TAG, "jsonArray is: " + response);
                    if (response.trim().equals("NoRecordsFound")) {
                        Log.e(TAG, "No Records Found Show some Image here");
                        //hide the recycler view and display the empty screen text and icon
                        mSummaryRecyclerView.setVisibility(View.GONE);
//                        getView().findViewById(R.id.tv_empty_leave_summary_view).setVisibility(View.VISIBLE);
//                        getView().findViewById(R.id.img_empty_view).setVisibility(View.VISIBLE);
                    } else {
                        JSONObject jsonObjectResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonObjectResponse.getJSONArray("server_response");
                        String request_id, reportingTo, leaveType, fromDate, toDate, totalDays, subject, description, responseDate, requestDate;

                        Log.e(TAG, "Leave Summary Details are: " + jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            request_id = jsonObject.getString("leave_detail_id");
                            reportingTo = jsonObject.getString("reporting_to");
                            leaveType = jsonObject.getString("leave_type");
                            fromDate = jsonObject.getString("date_from");
                            toDate = jsonObject.getString("date_to");
                            totalDays = jsonObject.getString("total_days");
                            subject = jsonObject.getString("subject");
                            description = jsonObject.getString("description");
                            requestDate = jsonObject.getString("request_date");
                            responseDate = jsonObject.getString("response_date");

                            //use DateHelper Class to convert date formats call the convertDateFormat() function to convert the dates from db format to the ui format
//                            Log.e(TAG, "old Date is: " + fromDate);
                            fromDate = new DateHelper().convertDateToUI(fromDate);
//                            Log.e(TAG, "new Date is: " + fromDate);

                            toDate = new DateHelper().convertDateToUI(toDate);
                            requestDate = new DateHelper().convertDateToUI(requestDate);
                            responseDate = new DateHelper().convertDateToUI(responseDate);

                            //set this to the Adapter
                            Log.e(TAG, "creating Summary!@!#@");
                            PendingLeaveRequest pendingLeaveRequest = new PendingLeaveRequest(request_id, reportingTo, leaveType, fromDate, toDate, totalDays, subject, description, responseDate, requestDate);
                            leaveRequestSummary.add(pendingLeaveRequest);
//                            Log.e(TAG,"pendingRequestsList is: "+leaveRequestSummary.toString());
                            leaveRequestsSummaryAdapter = new RequestSummaryAdapter(getContext(), leaveRequestSummary);
                            mSummaryRecyclerView.setAdapter(leaveRequestsSummaryAdapter);
                        }
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Network Error... Please Try again Later...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                Log.e(TAG, "Setting Params here : ");
//                Log.e(TAG, "emp_id is: " + params.toString());
                params.put("emp_id", new SessionManager(getContext()).getEmpId());//get myEmpId and Send it for further operations
                return params;
            }
        };
        //for default retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueuePendingRequests.add(request);
    }
}
