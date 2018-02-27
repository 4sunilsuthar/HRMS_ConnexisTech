package com.lms.admin.lms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class ViewPayrollSlipActivity extends FragmentActivity {

    FragmentManager fragment_manager;
    FragmentTransaction fragmentTrans;
    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payroll_slip);
    }

    public void viewPayrollFragment(View view) {
        fragment_manager = this.getSupportFragmentManager();
        fragmentTrans = fragment_manager.beginTransaction();

        if (view.getId() == R.id.btnViewPayrollSlip) {
            fragment = new ViewPayslipFragment();
            fragmentTrans.add(R.id.fragment_container, fragment).commit();
        }
        if (view.getId() == R.id.btnEditPayrollSlip) {
            fragment = new EditPaySlipFragment();
            fragmentTrans.add(R.id.fragment_container, fragment).commit();

            //change button and its events

        }


    }
}
