package com.lms.admin.lms;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 20-03-2018.
 */

public class LeaveRequestsAdapter extends RecyclerView.Adapter<LeaveRequestsAdapter.LeaveRequestViewHolder> {
    private static final String TAG = "LeaveRequestsAdapter";
    private RequestQueue requestQueue;
    private List<PendingLeaveRequest> leaveRequestsList;
    private Context context;

    LeaveRequestsAdapter(Context context, List<PendingLeaveRequest> leaveRequestsList) {
        this.context = context;
        this.leaveRequestsList = leaveRequestsList;
    }

    @NonNull
    @Override
    public LeaveRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.pending_request_custom, null);//change layout view here
        return new LeaveRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveRequestViewHolder holder, int position) {

        PendingLeaveRequest pendingLeaveRequest = leaveRequestsList.get(position);
        holder.leaveRequestId.setText(pendingLeaveRequest.getRequest_id());
        holder.leaveRequestEmpName.setText(pendingLeaveRequest.getEmp_name());
        holder.leaveRequestLeaveType.setText(pendingLeaveRequest.getLeaveType());
        holder.leaveRequestFromDate.setText(pendingLeaveRequest.getFromDate());
        holder.leaveRequestToDate.setText(pendingLeaveRequest.getToDate());
        holder.leaveRequestTotalDays.setText(pendingLeaveRequest.getTotalDays());
        holder.leaveRequestSubject.setText(pendingLeaveRequest.getSubject());
        holder.leaveRequestDescription.setText(pendingLeaveRequest.getDescription());
        holder.leaveRequestedOn.setText(pendingLeaveRequest.getRequestDate());

        Log.e(TAG, "postStory.getProfileImg() is :" + pendingLeaveRequest.getProfileImg());
        if (pendingLeaveRequest.getProfileImg().equals("null")) {
            Log.e(TAG, "profile path is null so Lets have default image >>>>><<<<<<");
            Picasso.with(context).load(R.drawable.person).into(holder.leaveRequestUserProfile);//showing demo image till load or img not available
        } else {
            Picasso.with(context).load(pendingLeaveRequest.getProfileImg()).into(holder.leaveRequestUserProfile);//otherwise showing the actual image
        }
    }

    @Override
    public int getItemCount() {
        return leaveRequestsList.size();
    }

    //function to saveLeaveDetails into the db
    private void approveLeaveRequest(final String leaveDetailId, final String responseDate, final String responseFrom, final String responseRemark) {
        final ProgressDialog progressDialog = ProgressDialog.show(context, "Saving Leave Details", "Please Wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URLs.approveLeaveRequestAPIUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "ServerResponse is: " + response);
                //move to main fragment showing posts
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, "Network Error... Please Try again Later...", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error is: " + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("leave_detail_id", leaveDetailId);
                params.put("response_date", responseDate);
                params.put("response_from", responseFrom);
                params.put("response_remark", responseRemark);
//                Log.e(TAG,"params are: "+params.toString());
                return params;
            }
        };
        //for default retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    //function to send response notification
    private void sendResponseNotification(final String leaveDetailId) {
        final ProgressDialog progressDialog = ProgressDialog.show(context, "Getting you on board", "Please Wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URLs.pushResponseNotificationsAPIUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e(TAG, "response notification result is: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "Error : " + error);
                progressDialog.dismiss();
                Toast.makeText(context, "Volley Network Error... Please Try again Later...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //demo title is : New Leave Request
                //demo message is : Mr.Emp is requesting for Leave for 5 days
                //
                String title = "Hurray Leave Approved!!!";
//                String message = empName+" has Approved your leave";
                String message = "Senior has Approved your leave";
                params.put("leave_detail_id", leaveDetailId);
                params.put("title", title);
                params.put("message", message);
                return params;
            }
        };
        //for default retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    class LeaveRequestViewHolder extends RecyclerView.ViewHolder {
        ImageView leaveRequestUserProfile;
        TextView leaveRequestId, leaveRequestEmpName, leaveRequestLeaveType, leaveRequestFromDate, leaveRequestToDate, leaveRequestTotalDays, leaveRequestSubject, leaveRequestDescription, leaveRequestedOn;
        Button btnApprove, btnReject;

        LeaveRequestViewHolder(View itemView) {
            super(itemView);
            leaveRequestUserProfile = itemView.findViewById(R.id.leave_request_profile_image);
            leaveRequestId = itemView.findViewById(R.id.tv_leave_details_id);
            leaveRequestEmpName = itemView.findViewById(R.id.tvInboxEmpName);
            leaveRequestLeaveType = itemView.findViewById(R.id.tvInboxLeaveType);
            leaveRequestFromDate = itemView.findViewById(R.id.tvInboxLeaveFrom);
            leaveRequestToDate = itemView.findViewById(R.id.tvInboxLeaveTo);
            leaveRequestedOn = itemView.findViewById(R.id.tvInboxLeaveRequestDate);
            leaveRequestTotalDays = itemView.findViewById(R.id.tvInboxLeaveDays);
            leaveRequestSubject = itemView.findViewById(R.id.tvInboxLeaveSubject);
            leaveRequestDescription = itemView.findViewById(R.id.tvInboxLeaveDescription);
            btnApprove = itemView.findViewById(R.id.btnInboxApproveLeave);
            btnReject = itemView.findViewById(R.id.btnInboxRejectLeave);

            //set action to clicks on buttons

            btnApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    @SuppressLint("InflateParams") View subView = inflater.inflate(R.layout.custom_alert_dialog, null);
                    final EditText subEditText = subView.findViewById(R.id.ed_dialog_remark);

                    new AlertDialog.Builder(context, R.style.CustomDialogTheme)
                            .setIcon(R.drawable.ic_warning_white)
                            .setTitle("Confirm Leave Response")
                            .setMessage("Are you sure you want to Approve Leave Request")
                            .setView(subView)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // get position of the item and find specified leave record
                                    int pos = getAdapterPosition();
                                    // check if item still exists
                                    if (pos != RecyclerView.NO_POSITION) {
                                        PendingLeaveRequest clickedDataItem = leaveRequestsList.get(pos);
                                        Toast.makeText(context, "You clicked approved button of  " + clickedDataItem.getRequest_id() + " requestID...", Toast.LENGTH_SHORT).show();
                                        //call the background task to approve leave request
                                        //collect all the data that to be sent to the method for background request call
                                        //get leave detail id, current user id , current date in dbFormat
                                        String leaveDetailId, responseDate, responseFrom, responseRemark;
                                        leaveDetailId = clickedDataItem.getRequest_id(); //get id of clicked leave request
                                        responseDate = new DateHelper().convertDateToDB(Calendar.getInstance().getTime());//convert current date into dbDateFormat
                                        responseFrom = new SessionManager(context).getEmpId();//get current user's emp_id
//                                        responseRemark = "null"; //initialize remark
                                        responseRemark = "null"; //initialize remark

                                        if (TextUtils.isEmpty(subEditText.getText().toString())) {
                                            Log.e(TAG, "response remark is null");
                                        } else {
                                            responseRemark = subEditText.getText().toString();
                                        }
//                                        Log.e(TAG,"leave id: "+leaveDetailId+" | response Date is: "+responseDate+"response from: "+responseFrom);

                                        requestQueue = Volley.newRequestQueue(context);//creating new network request
                                        approveLeaveRequest(leaveDetailId, responseDate, responseFrom, responseRemark); //volley background task in this method
                                        //leave is Approved so now inform the user about the response with the help of the notification
                                        // send notification to the requester
                                        sendResponseNotification(leaveDetailId);


                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show().setCanceledOnTouchOutside(false);
                }


            });

            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Reject button Clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
