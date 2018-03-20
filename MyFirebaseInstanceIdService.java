package com.lms.admin.lms;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Admin on 19-03-2018.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "InstanceIdService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();//the magical line to be used whenever we want the device token...
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //don't send it for the first time
        //send it when the user log in
        sendRegistrationToServer(refreshedToken);//needs to be changed when the user logs in @?#><
    }

    private void sendRegistrationToServer(String refreshedToken) {
        //you have got the token send it to the database and register the device for the user

        //save this token to the user with whom these phone (device) is registered
        //so get user_id and store the token into the users table by matching the user_id
        //we can get the user_id from the saved shared preference and if it is null then we can't just yet send the token to the server
        //so wait till the user log in and then at the time of the login we will send the token to the server also
        Log.e(TAG, "User Logged in : " + new SessionManager(this).isLoggedIn());
        Log.e(TAG, "User Token is : " + refreshedToken);
//        if(getSharedPreferences(new SessionManager(this).PREF_NAME))


    }
}
