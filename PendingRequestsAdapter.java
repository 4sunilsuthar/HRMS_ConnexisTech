package com.lms.admin.lms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.util.List;

/**
 * Created by Admin on 22-03-2018.
 */

public class PendingRequestsAdapter extends RecyclerView.Adapter<PendingRequestsAdapter.PendingRequestViewHolder> {
    private static final String TAG = "LeaveRequestsAdapter";
    RequestQueue requestQueue;
    private List<PendingLeaveRequest> leaveRequestsList;
    private Context context;

    PendingRequestsAdapter(Context context, List<PendingLeaveRequest> leaveRequestsList) {
        this.context = context;
//        Log.e(TAG,"pending Request Adapter Called>>>");
        this.leaveRequestsList = leaveRequestsList;
    }

    @NonNull
    @Override
    public PendingRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.pending_request_user_custom, null);//change layout view here
        return new PendingRequestViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PendingRequestViewHolder holder, int position) {

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

       /* Log.e(TAG, "postStory.getProfileImg() is :" + pendingLeaveRequest.getProfileImg());
        if (pendingLeaveRequest.getProfileImg().equals("null")) {
            Log.e(TAG, "profile path is null so Lets have default image >>>>><<<<<<");
            Picasso.with(context).load(R.drawable.person).into(holder.leaveRequestUserProfile);//showing demo image till load or img not available
        } else {
            Picasso.with(context).load(pendingLeaveRequest.getProfileImg()).into(holder.leaveRequestUserProfile);//otherwise showing the actual image
        }*/
    }

    @Override
    public int getItemCount() {
        return leaveRequestsList.size();
    }

    /*
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
    */

    class PendingRequestViewHolder extends RecyclerView.ViewHolder {
        //        ImageView leaveRequestUserProfile;
        TextView leaveRequestId, leaveRequestEmpName, leaveRequestLeaveType, leaveRequestFromDate, leaveRequestToDate, leaveRequestTotalDays, leaveRequestSubject, leaveRequestDescription, leaveRequestedOn;
//        Button btnApprove, btnReject;

        PendingRequestViewHolder(View itemView) {
            super(itemView);
//            leaveRequestUserProfile = itemView.findViewById(R.id.leave_request_profile_image);
            leaveRequestId = itemView.findViewById(R.id.tv_user_pending_request_leave_detail_id);
            leaveRequestEmpName = itemView.findViewById(R.id.tv_user_pending_request_reporting_to);
            leaveRequestLeaveType = itemView.findViewById(R.id.tv_user_pending_request_leave_type);
            leaveRequestFromDate = itemView.findViewById(R.id.tv_user_pending_request_leave_from);
            leaveRequestToDate = itemView.findViewById(R.id.tv_user_pending_request_leave_to);
            leaveRequestedOn = itemView.findViewById(R.id.tv_user_pending_request_leave_request_date);
            leaveRequestTotalDays = itemView.findViewById(R.id.tv_user_pending_request_total_days);
            leaveRequestSubject = itemView.findViewById(R.id.tv_user_pending_request_leave_subject);
            leaveRequestDescription = itemView.findViewById(R.id.tv_user_pending_request_leave_description);
//            btnApprove = itemView.findViewById(R.id.btnInboxApproveLeave);
//            btnReject = itemView.findViewById(R.id.btnInboxRejectLeave);

        }
    }

}
