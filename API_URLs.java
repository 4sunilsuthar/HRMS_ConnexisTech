package com.lms.admin.lms;

/**
 * Created by Admin on 07-03-2018.
 */

public class API_URLs {

    //all API URLs here
    static final String getUserProfileDetailsAPIUrl = "http://192.168.0.116/hrms_app/get_user_profile_details.php";
    static final String getPostStoriesAPIUrl = "http://192.168.0.116/hrms_app/get_posts_stories.php";
    static final String registerNewEmpAPIUrl = "http://192.168.0.116/hrms_app/register_new_emp.php";
    static final String getLeaveTypesAPIUrl = "http://192.168.0.116/hrms_app/get_leave_types.php";
    static final String getDesignationsAPIUrl = "http://192.168.0.116/hrms_app/get_designations.php";
    static final String getEmpNamesAPIUrl = "http://192.168.0.116/hrms_app/get_emp_names.php";
    static final String imgUploadToServerAPIUrl = "http://192.168.0.116/hrms_app/img_upload_to_server.php";
    static final String updateUserProfileDetailsAPIUrl = "http://192.168.0.116/hrms_app/update_user_profile_details.php";
    static final String verifyEmailAPIUrl = "http://192.168.0.116/hrms_app/verify_email.php";
    static final String saveLeaveDetailsAPIUrl = "http://192.168.0.116/hrms_app/save_leave_request_details.php";
    static final String sendRegistrationTokenToServerAPIUrl = "http://192.168.0.116/hrms_app/send_registration_token_to_server.php";
    static final String pushNotificationsAPIUrl = "http://192.168.0.116/hrms_app/push_notifications.php";
    static final String pushResponseNotificationsAPIUrl = "http://192.168.0.116/hrms_app/push_response_notifications.php";
    static final String getPendingLeaveRequestsAPIUrl = "http://192.168.0.116/hrms_app/get_pending_leave_requests.php";
    static final String getUserPendingLeaveRequestsAPIUrl = "http://192.168.0.116/hrms_app/get_pending_leave_requests_user.php";
    static final String getLeaveRequestsSummaryAPIUrl = "http://192.168.0.116/hrms_app/get_leave_requests_summary.php";
    static final String approveLeaveRequestAPIUrl = "http://192.168.0.116/hrms_app/approve_leave_request.php";
    static final String showUserProfileAPIUrl = "http://192.168.0.116/hrms_app/show_user_profile.php";
    static final String getEmpPayrollStructureAPIUrl = "http://192.168.0.116/hrms_app/get_emp_payroll_structure.php";

/*    public static void getMyIP(){
        Log.e("MyIPTag","my IP Called...");

        try {
            InetAddress ipAddr = InetAddress.getLocalHost();
            Log.e("MyIPTag","my IP is :"+ipAddr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.e("MyIPTag","error is: "+e);
        }
    }*/
}
