package com.lms.admin.lms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Admin on 20-03-2018.
 */

public class LeaveRequestsAdapter extends RecyclerView.Adapter<LeaveRequestsAdapter.LeaveRequestViewHolder> {

    private static final String TAG = "LeaveRequestsAdapter";
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
        holder.leaveRequestFromDate.setText(pendingLeaveRequest.getFromDate());
        holder.leaveRequestToDate.setText(pendingLeaveRequest.getToDate());
        holder.leaveRequestTotalDays.setText(pendingLeaveRequest.getTotalDays());
        holder.leaveRequestSubject.setText(pendingLeaveRequest.getSubject());
        holder.leaveRequestDescription.setText(pendingLeaveRequest.getDescription());
        holder.leaveRequestDescription.setText(pendingLeaveRequest.getRequestDate());

        Log.e(TAG, "postStory.getProfileImg() is :" + pendingLeaveRequest.getProfileImg());
        if (pendingLeaveRequest.getProfileImg().equals("null")) {
//            Log.e(TAG, "profile path is null so Lets have default image >>>>><<<<<<");
            Picasso.with(context).load(R.drawable.person).into(holder.leaveRequestUserProfile);//showing demo image till load or img not available
        } else {
            Picasso.with(context).load(pendingLeaveRequest.getProfileImg()).into(holder.leaveRequestUserProfile);//otherwise showing the actual image
        }
    }

    @Override
    public int getItemCount() {
        return leaveRequestsList.size();
    }

    class LeaveRequestViewHolder extends RecyclerView.ViewHolder {
        ImageView leaveRequestUserProfile;
        TextView leaveRequestId, leaveRequestEmpName, leaveRequestFromDate, leaveRequestToDate, leaveRequestTotalDays, leaveRequestSubject, leaveRequestDescription, leaveRequestedOn;
        Button btnApprove, btnReject;

        LeaveRequestViewHolder(View itemView) {
            super(itemView);
            leaveRequestUserProfile = itemView.findViewById(R.id.leave_request_profile_image);
            leaveRequestId = itemView.findViewById(R.id.tv_leave_details_id);
            leaveRequestEmpName = itemView.findViewById(R.id.tvInboxEmpName);
            leaveRequestFromDate = itemView.findViewById(R.id.tvInboxLeaveFrom);
            leaveRequestToDate = itemView.findViewById(R.id.tvInboxLeaveTo);
            leaveRequestedOn = itemView.findViewById(R.id.tvInboxLeaveRequestDate);
            leaveRequestTotalDays = itemView.findViewById(R.id.tvInboxLeaveDays);
            leaveRequestSubject = itemView.findViewById(R.id.tvInboxLeaveSubject);
            leaveRequestDescription = itemView.findViewById(R.id.tvInboxLeaveDescription);
            btnApprove = itemView.findViewById(R.id.btnInboxApproveLeave);
            btnReject = itemView.findViewById(R.id.btnInboxRejectLeave);

            //setting the onclick listener to the Post to display post
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Leave Request clicked", Toast.LENGTH_SHORT).show();
//                context.startActivity(new Intent(context, ShowPostDetailsActivity.class));
                }
            });
        }
    }
}