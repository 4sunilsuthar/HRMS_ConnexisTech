package com.lms.admin.lms;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TableLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class ViewPayslipFragment extends Fragment {
    private Spinner spinLeaveType;
    private RequestQueue requestQueue;
    private TableLayout tableLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_payslip, container, false);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinLeaveType = getView().findViewById(R.id.leave_type_spinner);
        //volley code to fetch payroll details here
        //background volley request here
        requestQueue = Volley.newRequestQueue(getContext());
//        getLeaveTypesInSpinner();


        //get the values from the database based on the emp_id
        //pass emp_id of the selected item in the spinner


        tableLayout = view.findViewById(R.id.table_payroll_details);
        tableLayout.removeAllViewsInLayout();

        //setting the data in here...


    }
}
