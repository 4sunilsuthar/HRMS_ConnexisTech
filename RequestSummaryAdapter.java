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

public class RequestSummaryAdapter extends RecyclerView.Adapter<RequestSummaryAdapter.LeaveSummaryViewHolder> {
    private static final String TAG = "LeaveRequestsAdapter";
    RequestQueue requestQueue;
    private List<PendingLeaveRequest> leaveRequestsList;
    private Context context;

    RequestSummaryAdapter(Context context, List<PendingLeaveRequest> leaveRequestsList) {
        this.context = context;
//        Log.e(TAG,"pending Request Adapter Called>>>");
        this.leaveRequestsList = leaveRequestsList;
    }

    @NonNull
    @Override
    public LeaveSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.leave_summary_custom, null);//change layout view here
        return new LeaveSummaryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull LeaveSummaryViewHolder holder, int position) {

        PendingLeaveRequest pendingLeaveRequest = leaveRequestsList.get(position);
        holder.leaveRequestId.setText(pendingLeaveRequest.getRequest_id());
        //preparing final message here
        String statusMessage = "Your Leave Request was approved by " + pendingLeaveRequest.getEmp_name() + " on " + pendingLeaveRequest.getProfileImg();
        holder.leaveRequestStatusMessage.setText(statusMessage);
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

    class LeaveSummaryViewHolder extends RecyclerView.ViewHolder {
        //        ImageView leaveRequestUserProfile;
        TextView leaveRequestId, leaveRequestStatusMessage, leaveRequestLeaveType, leaveRequestFromDate, leaveRequestToDate, leaveRequestTotalDays, leaveRequestSubject, leaveRequestDescription, leaveRequestedOn;
//        Button btnApprove, btnReject;

        LeaveSummaryViewHolder(View itemView) {
            super(itemView);
//            leaveRequestUserProfile = itemView.findViewById(R.id.leave_request_profile_image);
            leaveRequestId = itemView.findViewById(R.id.tv_user_leave_summary_detail_id);
            leaveRequestStatusMessage = itemView.findViewById(R.id.tvMyLeaveSummaryStatusMessage);
            leaveRequestLeaveType = itemView.findViewById(R.id.tv_user_summary_leave_type);
            leaveRequestFromDate = itemView.findViewById(R.id.tv_user_summary_leave_from);
            leaveRequestToDate = itemView.findViewById(R.id.tv_user_summary_leave_to);
            leaveRequestedOn = itemView.findViewById(R.id.tv_user_summary_leave_request_date);
            leaveRequestTotalDays = itemView.findViewById(R.id.tv_user_summary_leave_total_days);
            leaveRequestSubject = itemView.findViewById(R.id.tv_user_summary_leave_subject);
            leaveRequestDescription = itemView.findViewById(R.id.tv_user_summary_leave_description);
//            btnApprove = itemView.findViewById(R.id.btnInboxApproveLeave);
//            btnReject = itemView.findViewById(R.id.btnInboxRejectLeave);

        }
    }


}
