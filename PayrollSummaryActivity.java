package com.lms.admin.lms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class PayrollSummaryActivity extends AppCompatActivity {
    FragmentManager fragment_manager;
    FragmentTransaction fragmentTrans;
    Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payroll_summary);
        fragment_manager = this.getSupportFragmentManager();
        fragmentTrans = fragment_manager.beginTransaction();
        fragment = new PayrollSummaryTableFragment();
        fragmentTrans.replace(R.id.frg_container_pay_summary, fragment).addToBackStack("payroll_summary_fragment").commit();

    }
}

