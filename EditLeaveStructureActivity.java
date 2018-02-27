package com.lms.admin.lms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class EditLeaveStructureActivity extends AppCompatActivity {
    FragmentManager fragment_manager;
    Fragment fragment;
    FragmentTransaction fragmentTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_leave_structure);
    }

    public void viewFragment(View view) {
        int id = view.getId();
        if (id == R.id.btnViewMyLeaveDeatils) {
            fragment_manager = this.getSupportFragmentManager();
            fragmentTrans = fragment_manager.beginTransaction();
            fragment = new ViewMyLeaveDetailsFragment();
            fragmentTrans.replace(R.id.fragment_container, fragment).addToBackStack("view_my_leave_details_fragment").commit();

        }
        if (id == R.id.btnEditLeaveStructure) {
            fragment_manager = this.getSupportFragmentManager();
            fragmentTrans = fragment_manager.beginTransaction();
            fragment = new EditLeaveStructureFragment();
            fragmentTrans.replace(R.id.fragment_container, fragment).addToBackStack("edit_leave_structure_fragment").commit();
        }

        //summary button

        if (id == R.id.btnLeaveSummary) {
            Intent intent = new Intent(EditLeaveStructureActivity.this, LeaveDetailsActivity.class);
            startActivity(intent);
        }
    }

}
