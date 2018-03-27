package com.lms.admin.lms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class ManageEmpDetailsActivity extends AppCompatActivity {

    private static final String TAG = "ManageEmpActivity";
    public String empId = null;//for global reference
    FragmentManager fragment_manager;
    Fragment fragment;
    FragmentTransaction fragmentTrans;
    Spinner spEmpNames;
    SpinnerHelper sHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//hide the action bar for this activity
        setContentView(R.layout.activity_manage_emp_details);
        spEmpNames = findViewById(R.id.sp_emp_names);
        //final SpinnerHelper spinnerHelper = new SpinnerHelper(ManageEmpDetailsActivity.this, spEmpNames);
        final SpinnerHelper spinnerHelper = new SpinnerHelper(ManageEmpDetailsActivity.this, spEmpNames);

        spinnerHelper.setSpinnerLayout(1000);
        spinnerHelper.fetchJSONEmpNames();
        sHelper = spinnerHelper;
        spEmpNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //on selecting an item from spinner
//                Log.e(TAG, "Listener called : ");
                spEmpNames.setSelection(position);
                empId = spinnerHelper.dataAdapter.empGlobalId;
                Log.e(TAG, "empId is: " + empId);
//                savedInstanceState.putInt("empId", Integer.parseInt(empId));


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
        fragment_manager = this.getSupportFragmentManager();
        fragmentTrans = fragment_manager.beginTransaction();

        int id = view.getId();
        if (id == R.id.sp_emp_names) {
//            Bundle arguments = new Bundle(); arguments.putString( string_key , desired_string);
//            fragment.setArguments(arguments);
//            final FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.replace(R.id.content, fragment , FRAGMENT_TAG);
//            ft.commit();
            fragment = new UserProfileFragmentAdmin();
            Bundle arguments = new Bundle();
            arguments.putString("myEmpID", empId);
            fragment.setArguments(arguments);
            fragment_manager.popBackStack();//popping all from backStack
            fragmentTrans.replace(R.id.fragment_container, fragment).addToBackStack("view_emp_details_fragment").commit();
        }
        if (id == R.id.btnEditEmpDetails) {
            fragment = new EditEmpDetailsFragment();
            Bundle arguments = new Bundle();
            arguments.putString("myEmpID", empId);
            fragment.setArguments(arguments);
            fragment_manager.popBackStack();//popping all from backStack
            fragmentTrans.replace(R.id.fragment_container, fragment).addToBackStack("edit_emp_details_fragment").commit();
        }
        /*if (id == R.id.btnSetHierarchy) {
            Log.i("ABC", "In the Fragment Function");
            fragment = new SetHierarchyFragment();
            fragment_manager.popBackStack();//popping all from backStacks
            fragmentTrans.replace(R.id.fragment_container, fragment).addToBackStack("set_hierarchy_fragment").commit();
            Log.i("ABC", "Fragment Function Completed");
        }*/
    }
}
