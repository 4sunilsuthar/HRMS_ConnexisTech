package com.lms.admin.lms;

import android.app.ProgressDialog;
import android.content.Context;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class InboxStructureFragment extends Fragment {
    private static final String TAG = "InboxStructureFragment";
    RecyclerView mRecyclerView;
    List<PendingLeaveRequest> leaveRequestsList;
    LeaveRequestsAdapter leaveRequestsAdapter;
    private RequestQueue requestQueueLeaveRequests;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox_structure, container, false);
        leaveRequestsList = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.leave_requests_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
        //load all the leave requests that has status as pending and reporting_to as emp's emp_id
        //load them in a recycler_view
        //        call volley background request here
        //background volley request here
        requestQueueLeaveRequests = Volley.newRequestQueue(getContext());
        Log.e(TAG, "empId is:> " + new SessionManager(getContext()).getEmpId());
        parseJSONLeaveRequests();
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
//            Log.e(TAG, "Current date timestamp is : " + cDate.getTime());
//            Log.e(TAG, "Post date timestamp is : " + pDate.getTime());
            //find difference between postTime and currentTime
            long ms2 = 43200000; //adding 12 hours (with milliseconds value ) to solve the problem of AM/PM
            long ms = Math.abs((cDate.getTime() + ms2) - pDate.getTime());
//            Log.e(TAG, "difference bw timestamps : " + ms);


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
//            Log.e(TAG, "time is " + text);
            return String.valueOf(text);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //volley background code to fetch post details in the recyclerView
    private void parseJSONLeaveRequests() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Getting you on board", "Please Wait...", false, false);
        final StringRequest request = new StringRequest(Request.Method.POST, API_URLs.getPendingLeaveRequestsAPIUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(TAG, "jsonArray is: " + response);
                    if (response.trim().equals("NoRecordsFound")) {
                        Log.e(TAG, "No Records Found Show some Image here");
                        //hide the recycler view and display the empty screen text and icon
                        mRecyclerView.setVisibility(View.GONE);
                        getView().findViewById(R.id.tv_empty_view).setVisibility(View.VISIBLE);
                        getView().findViewById(R.id.img_empty_view).setVisibility(View.VISIBLE);
                    } else {
                        JSONObject jsonObjectResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonObjectResponse.getJSONArray("server_response");
                        String request_id, emp_name, leaveType, fromDate, toDate, totalDays, subject, description, profile_image_url, requestDate;

                        Log.e(TAG, "Pending Leave Details are: " + jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            request_id = jsonObject.getString("leave_detail_id");
                            emp_name = jsonObject.getString("user_name");
                            leaveType = jsonObject.getString("leave_type");
                            fromDate = jsonObject.getString("date_from");
                            toDate = jsonObject.getString("date_to");
                            totalDays = jsonObject.getString("total_days");
                            subject = jsonObject.getString("subject");
                            description = jsonObject.getString("description");
                            profile_image_url = jsonObject.getString("profile_img_url");
                            requestDate = jsonObject.getString("request_date");

                            //use DateHelper Class to convert date formats call the convertDateFormat() function to convert the dates from db format to the ui format
                            Log.e(TAG, "old Date is: " + fromDate);
                            fromDate = new DateHelper().convertDateToUI(fromDate);
                            Log.e(TAG, "new Date is: " + fromDate);

                            toDate = new DateHelper().convertDateToUI(toDate);
                            requestDate = new DateHelper().convertDateToUI(requestDate);

//                        String diff = getDateDiff(date, time);
//                    Log.e(TAG, "dateDiff msg is : " + diff);
//                    Log.e(TAG, "dateDiff msg is : " + diff);
                            //set this to the Adapter
                            PendingLeaveRequest pendingLeaveRequest = new PendingLeaveRequest(request_id, emp_name, leaveType, fromDate, toDate, totalDays, subject, description, profile_image_url, requestDate);
                            leaveRequestsList.add(pendingLeaveRequest);
                            leaveRequestsAdapter = new LeaveRequestsAdapter(getContext(), leaveRequestsList);
                            mRecyclerView.setAdapter(leaveRequestsAdapter);
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
        requestQueueLeaveRequests.add(request);
    }
}

