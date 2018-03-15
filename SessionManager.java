package com.lms.admin.lms;

/**
 * Created by Admin on 27-02-2018.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import java.util.HashMap;

public class SessionManager {
    // User name (make variable public to access from outside)
    public static final String KEY_EMPID = "emp_id";
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    // SharedPref file name
    private static final String PREF_NAME = "HRMSAppPref";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // Shared Preferences
    private SharedPreferences pref;
    // Editor for Shared preferences
    private Editor editor;
    // Context
    private Context _context;

    // Constructor
    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this._context = context;
        int PRIVATE_MODE = 0;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String emp_id, String email) {
//    public void createLoginSession(String name, String email){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_EMPID, emp_id);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, SignInActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Toast.makeText(_context, "Sign in to Continue...", Toast.LENGTH_SHORT).show();

            // Staring Login Activity
            _context.startActivity(i);
        }
//        else {
//            user has already logged in
//            Toast.makeText(_context,"User is Logged In ...",Toast.LENGTH_SHORT).show();
//        }

    }


    /**
     * Get stored session data
     */
    HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_EMPID, pref.getString(KEY_EMPID, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to SignIn Activity
        Intent i = new Intent(_context, SignInActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        _context.startActivity(i);

    }

    /**
     * Clear session details
     */
    void clearPreferences() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    String getUserEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    String getEmpId() {
        return pref.getString(KEY_EMPID, null);
    }

}