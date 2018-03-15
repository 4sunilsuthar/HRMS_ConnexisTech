package com.lms.admin.lms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class EditLeaveStructureActivity extends AppCompatActivity {
    private static final String TAG = "EditLeaveStructureActi";
    public String empId = null;//for global reference
    FragmentManager fragment_manager;
    Fragment fragment;
    FragmentTransaction fragmentTrans;
    Spinner spEmpNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_leave_structure);
        spEmpNames = findViewById(R.id.sp_emp_names);
        //creating SpinnerHelper class object to fetch values in the spinner
        final SpinnerHelper spinnerHelper = new SpinnerHelper(EditLeaveStructureActivity.this, spEmpNames);
        //setting the height of the spinner dropdown (parameter in px)
        spinnerHelper.setSpinnerLayout(2000);
        //fetching emp names in the spinner with background volley request
        spinnerHelper.fetchJSONEmpNames();

        spEmpNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //on selecting an item from spinner
//                Log.e(TAG, "Listener called : ");
                spEmpNames.setSelection(position);
                empId = spinnerHelper.dataAdapter.empGlobalId;
                Log.e(TAG, "empId is: " + empId);
                viewFragment(spEmpNames);
                Log.e(TAG, "Item Selected...");
                //fetch the record of employee whose name is selected in the spinner
//                getEmpName
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //when no item is selected
            }
        });
    }

    public void viewFragment(View view) {
        int id = view.getId();
        fragment_manager = this.getSupportFragmentManager();
        fragmentTrans = fragment_manager.beginTransaction();
        if (id == R.id.sp_emp_names) {
            fragment = new ViewMyLeaveDetailsFragment();
            fragment_manager.popBackStack();//popping all from backStack
            fragmentTrans.replace(R.id.fragment_container, fragment).addToBackStack("view_my_leave_details_fragment").commit();

        }
        if (id == R.id.btnEditLeaveStructure) {
            fragment = new EditLeaveStructureFragment();
            fragment_manager.popBackStack();//popping all from backStack
            fragmentTrans.replace(R.id.fragment_container, fragment).addToBackStack("edit_leave_structure_fragment").commit();
        }

        //summary button
        if (id == R.id.btnLeaveSummary) {
            Intent intent = new Intent(EditLeaveStructureActivity.this, LeaveDetailsActivity.class);
            startActivity(intent);
        }
    }

}
