package com.lms.admin.lms;

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
import android.view.MenuItem;

public class UserHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fragment_manager;
    FragmentTransaction fragmentTrans;
    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //to check the default fragment at startup
        navigationView.getMenu().getItem(0).setChecked(true);

        //calling the method for the default fragment loading

        fragment_manager = this.getSupportFragmentManager();
        fragmentTrans = fragment_manager.beginTransaction();
        fragment = new ContentUserHomeFragment();
        fragmentTrans.add(R.id.fragment_container1, fragment).commit();


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
        fragment_manager = this.getSupportFragmentManager();
        fragmentTrans = fragment_manager.beginTransaction();

        if (id == R.id.nav_home_feed) {

            fragment = new ContentUserHomeFragment();
            fragment_manager.popBackStack();
            fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("content_user_home_fragment").commit();


        } else if (id == R.id.nav_inbox) {

            fragment = new InboxStructureFragment();
            fragment_manager.popBackStack();
            fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("inbox_structure_fragment").commit();

        } else if (id == R.id.nav_leave_request) {

            fragment = new LeaveRequestFragment();
            fragment_manager.popBackStack();
            fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("leave_request_fragment").commit();


        } else if (id == R.id.nav_my_leaves_summary) {

            fragment = new MyLeaveSummaryFragment();
            fragment_manager.popBackStack();
            fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("my_leave_summary_fragment").commit();

        } else if (id == R.id.nav_payslip_details) {

            fragment = new MyPayslipDetailsFragment();
            fragment_manager.popBackStack();
            fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("my_payslip_details_fragment").commit();


        } else if (id == R.id.nav_profile) {

            fragment = new UserProfileFragment();
            fragment_manager.popBackStack();
            fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("user_profile_fragment").commit();

        } else if (id == R.id.nav_share) {

            startActivity(new Intent(this, AdminDashboardActivity.class));


        } else if (id == R.id.nav_about) {

            fragment = new ContactUsFragment();
            fragment_manager.popBackStack();
            fragmentTrans.replace(R.id.fragment_container1, fragment).addToBackStack("contact_us_fragment").commit();

        } else if (id == R.id.nav_log_out) {
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
