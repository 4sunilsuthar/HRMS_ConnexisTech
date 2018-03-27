package com.lms.admin.lms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "UserHomeActivity";
    CircleImageView actionBarUserImg;
    CircleImageView navHeaderUserImg;
    FragmentManager fragment_manager;
    FragmentTransaction fragmentTrans;
    Fragment fragment;
    String empProfileImgUrl, empCoverImgUrl;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        actionBarUserImg = findViewById(R.id.action_bar_profile_image);

        //calling the method for the default fragment loading

        fragment_manager = this.getSupportFragmentManager();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                //this code runs when the drawer is opened...
                navHeaderUserImg = findViewById(R.id.nav_header_profile_image);
                Log.e(TAG, "profile URL is: " + empProfileImgUrl);
                if (empProfileImgUrl != null)
                    Picasso.with(UserHomeActivity.this).load(empProfileImgUrl).into(navHeaderUserImg);//assign user customized image
                if (new SessionManager(UserHomeActivity.this).getUserName() != null) {   //show name here
                    TextView tvUserName = findViewById(R.id.tvNavigationBarUserName);
                    tvUserName.setText(new SessionManager(UserHomeActivity.this).getUserName());
                }
                if (new SessionManager(UserHomeActivity.this).getUserEmail() != null) {   //show email here
                    TextView tvUserEmail = findViewById(R.id.tvNavigationBarUserEmail);
                    tvUserEmail.setText(new SessionManager(UserHomeActivity.this).getUserEmail());
                }
                navHeaderUserImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //display view profile fragment
                        fragmentTrans = fragment_manager.beginTransaction();
                        fragment = new UserProfileFragment();
                        fragment_manager.popBackStack();
                        fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("user_profile_fragment").commit();
                        drawer.closeDrawer(GravityCompat.START);

                    }
                });
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //fetch current user's profile image and show it here

        //call volley to show profile img in the action bar function
        requestQueue = Volley.newRequestQueue(this);
        showUserProfile(new SessionManager(this).getEmpId());

        //to check the default fragment at startup
        navigationView.getMenu().getItem(0).setChecked(true);


        //<<<check if activity is called from a notification or not>>>
        String menuFragment = getIntent().getStringExtra("menuFragment");
        // If menuFragment is defined, then this activity was launched with a fragment selection
        if (menuFragment != null) {
            // Here we can decide what do to -- perhaps load other parameters from the intent extras such as IDs, etc
            if (menuFragment.equals("leaveRequestInboxFragment")) {
                fragmentTrans = fragment_manager.beginTransaction();
                fragment = new InboxStructureFragment();
                fragmentTrans.add(R.id.fragment_container1, fragment).commit();
            }
        } else {
            // Activity was not launched with a menuFragment selected -- continue as if this activity was opened from a launcher (for example)
            fragmentTrans = fragment_manager.beginTransaction();
            fragment = new ContentUserHomeFragment();
            fragmentTrans.add(R.id.fragment_container1, fragment).commit();
        }

        //code for action bar profile image onClickListener
        actionBarUserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserHomeActivity.this, "Action Bar Button Called", Toast.LENGTH_SHORT).show();
                //display view profile fragment
                fragmentTrans = fragment_manager.beginTransaction();
                fragment = new UserProfileFragment();
                fragment_manager.popBackStack();
                fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("user_profile_fragment").commit();


            }
        });
    }

    //function to saveLeaveDetails into the db
    private void showUserProfile(final String empId) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Getting you on Board", "Please Wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URLs.showUserProfileAPIUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
//                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "ServerResponse is: " + response);
                try {
                    if (response.trim().equals("NoImageFound")) {
                        Log.e(TAG, "No Image Found Show Dummy Image here");
                        //showing dummy image when no image available
                        Picasso.with(UserHomeActivity.this).load(R.drawable.person).into(actionBarUserImg);
                        Picasso.with(UserHomeActivity.this).load(R.drawable.person).into(navHeaderUserImg);
                    } else {
                        JSONObject jsonObjectResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonObjectResponse.getJSONArray("server_response");

                        Log.e(TAG, "User Image URL Details are: " + jsonArray.toString());
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        empProfileImgUrl = jsonObject.getString("profile_img_url");
                        empCoverImgUrl = jsonObject.getString("img_cover_art_url");
                        Picasso.with(UserHomeActivity.this).load(empProfileImgUrl).into(actionBarUserImg);//assign user customized image
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(UserHomeActivity.this, "Network Error... Please Try again Later...", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error is: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("emp_id", empId);
                Log.e(TAG, "params : " + params.toString());
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home_feed) {
            fragmentTrans = fragment_manager.beginTransaction();
            fragment = new ContentUserHomeFragment();
            fragment_manager.popBackStack();
            fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("content_user_home_fragment").commit();

        } else if (id == R.id.nav_inbox) {

            fragmentTrans = fragment_manager.beginTransaction();
            fragment = new InboxStructureFragment();
            fragment_manager.popBackStack();
            fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("inbox_structure_fragment").commit();

        } else if (id == R.id.nav_leave_request) {

            fragmentTrans = fragment_manager.beginTransaction();
            fragment = new LeaveRequestFragment();
            fragment_manager.popBackStack();
            fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("leave_request_fragment").commit();

        } else if (id == R.id.nav_my_leaves_summary) {

            fragmentTrans = fragment_manager.beginTransaction();
            fragment = new MyLeaveSummaryFragment();
            fragment_manager.popBackStack();
            fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("my_leave_summary_fragment").commit();

        } else if (id == R.id.nav_payslip_details) {

            fragmentTrans = fragment_manager.beginTransaction();
            fragment = new MyPayslipDetailsFragment();
            fragment_manager.popBackStack();
            fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("my_payslip_details_fragment").commit();

        } else if (id == R.id.nav_profile) {

            fragmentTrans = fragment_manager.beginTransaction();
            fragment = new UserProfileFragment();
            fragment_manager.popBackStack();
            fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("user_profile_fragment").commit();

        } else if (id == R.id.nav_share) {

            /*
            //code for share using intent
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "https://drive.google.com/drive/folders/1PBsaJ78jmSgi1KRF8mwxTFATSBZioUAx?usp=sharing";//link of the application (google drive link here)
            String shareSub = "FlexiHR Download Link";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
            */

            startActivity(new Intent(this, AdminDashboardActivity.class)); // gateway to the admin dashboard

        } else if (id == R.id.nav_about) {

            fragmentTrans = fragment_manager.beginTransaction();
            fragment = new ContactUsFragment();
            fragment_manager.popBackStack();
            fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("contact_us_fragment").commit();

        } else if (id == R.id.nav_log_out) {
            //SignOut the User and show signIn Activity
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
