package com.lms.admin.lms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class ManageEmpDetailsActivity extends AppCompatActivity {

    FragmentManager fragment_manager;
    Fragment fragment;
    FragmentTransaction fragmentTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_emp_details);
    }

    public void viewFragment(View view) {
        fragment_manager = this.getSupportFragmentManager();
        fragmentTrans = fragment_manager.beginTransaction();

        int id = view.getId();
        if (id == R.id.btnViewEmpDetails) {
            fragment = new UserProfileFragment();
            fragmentTrans.replace(R.id.fragment_container, fragment).addToBackStack("view_emp_details_fragment").commit();

        }
        if (id == R.id.btnEditEmpDetails) {
            fragment = new EditEmpDetailsFragment();
            fragmentTrans.replace(R.id.fragment_container, fragment).addToBackStack("edit_emp_details_fragment").commit();
        }
        if (id == R.id.btnSetHierarchy) {
            Log.i("ABC", "In the Fragment Function");
            fragment = new SetHierarchyFragment();
            fragmentTrans.replace(R.id.fragment_container, fragment).addToBackStack("set_hierarchy_fragment").commit();
            Log.i("ABC", "Fragment Function Completed");
        }
    }
}
