package com.lms.admin.lms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ViewPayslipFragment extends Fragment {
    private static final String TAG = "ViewPayslipFragment";
    PayrollStructureAdapter payrollStructureAdapter;
    ListView listViewPayrollStructure;
    Button btnEditSalary, btnUpdateSalary;
    EditText edNetSalary;
    private RequestQueue requestQueue;

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
        btnEditSalary = getView().findViewById(R.id.btnEditSalary);
        btnUpdateSalary = getView().findViewById(R.id.btnUpdateSalaryViewSlip);

        edNetSalary = getView().findViewById(R.id.tv_payslip_structure_net_salary);
        listViewPayrollStructure = getView().findViewById(R.id.payrollStructureList);
        payrollStructureAdapter = new PayrollStructureAdapter(getContext(), R.layout.payslip_format_custom);
        listViewPayrollStructure.setAdapter(payrollStructureAdapter);
        getSalarySum();
        //getting the empId of the selected employee
        Bundle arguments = getArguments();
        assert arguments != null;
        String empId = arguments.getString("myEmpID"); //getting the empId of the selected employee
        Log.e(TAG, "desired Emp Id: " + empId);
        //volley code to fetch payroll details here
        //volley code to fetch payroll structure here
        //background volley request here
        requestQueue = Volley.newRequestQueue(getContext());
        parsePayrollStructure(empId);//passing empId to fetch emp payroll structure details

        //get the values from the database based on the emp_id
        btnEditSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //onclick of this button send bundle of listview details to the next fragment
                //Put the value
                FragmentManager fragment_manager;
                FragmentTransaction fragmentTrans;
                fragment_manager = getActivity().getSupportFragmentManager();
                fragment_manager.popBackStack();
                fragmentTrans = fragment_manager.beginTransaction();

                Fragment fragment = new EditPaySlipFragment();
                Bundle arguments = new Bundle();

//                arguments.putString("myEmpID", empId);
//                fragment.setArguments(arguments);

//                get all the values in the Bundle and then pass it with fragment.setArguments
                ListView listViewPayrollStructure = getView().findViewById(R.id.payrollStructureList);
                View mView;
                TextView tvTitle;
                EditText edValue;
                int listLength = listViewPayrollStructure.getChildCount();
                Log.e(TAG, "Length of list is: " + listLength);
//        String[] valueOfEditText = new String[listLength];
                for (int i = 0; i < listLength; i++) {
                    mView = listViewPayrollStructure.getChildAt(i);
                    tvTitle = mView.findViewById(R.id.tv_payslip_structure_title);
                    edValue = mView.findViewById(R.id.tv_payslip_structure_value);
                    Log.e(TAG, "value of " + tvTitle.getText().toString() + ": " + edValue.getText().toString());
                    arguments.putString(tvTitle.getText().toString(), edValue.getText().toString());

//                    edValue.setFocusable(true);
//                    edValue.setEnabled(true);
//                    edValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
//            valueOfEditText[i] = et.getText().toString();
                }
//
//                arguments.putString("your key", "YourValue");
                fragment.setArguments(arguments);
                fragmentTrans.replace(R.id.fragment_container, fragment).commit();
                //change button and its events


                //Inflate the fragment
//                getFragmentManager().beginTransaction().add(R.id.container, ldf).commit();

                //get salary sum
//                setEditable();//set EditText as Editable and focusable
//                getSalarySum();

                //set all the EditText as Editable and Focusable

                //hide edit btn visibility gone
                btnEditSalary.setVisibility(View.GONE);
                //set visibility of update btn as visible
                btnUpdateSalary.setVisibility(View.VISIBLE);
                //set the focus to the first edit text
                Toast.makeText(getContext(), "Lets Edit Salary Structure", Toast.LENGTH_SHORT).show();
            }
        });

        //update button listener here
        btnUpdateSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code here to update the salary structure
                //#1. check validation here
                //#2. call alertDialog to confirm structure update
                //#1. validations
                //get values from edit texts
                // final String leaveType, leaveSubject, leaveTotalDays, leaveDesc;
                //leaveSubject = edSubject.getText().toString().trim();
                //leaveTotalDays = totalLeaveDays.getText().toString().trim();
                //leaveDesc = edDescription.getText().toString().trim();

                //code validations here
                /*
                if (!TextUtils.isEmpty(leaveType) && TextUtils.isEmpty(leaveSubject) && TextUtils.isEmpty(edDateFrom.getText().toString().trim()) && TextUtils.isEmpty(edDateTo.getText().toString().trim()) && TextUtils.isEmpty(leaveTotalDays)) {
                    //Toast.makeText(getContext(), "Request Values are: leaveType: " + leaveType + " | leaveSubject: " + leaveSubject + " | leaveFrom: " + leaveFrom + " | leaveTill: " + leaveTill + " | totalDays: " + leaveTotalDays + " | leaveDesc: " + leaveDesc, Toast.LENGTH_SHORT).show();
//                    Log.e(TAG,""+"Request Values are: empId: 8 | leaveType: " + leaveType + " | leaveSubject: " + leaveSubject + " | leaveFrom: " + leaveFrom + " | leaveTill: " + leaveTill + " | totalDays: " + leaveTotalDays + " | leaveDesc: " + leaveDesc);
                    Toast.makeText(getContext(), "fill all the values to continue", Toast.LENGTH_SHORT).show();
                    return;
                }
                */
                //all looks good continue
                //#2. save the details in the database tbl_payroll_structure and set values as
                // get specified values here
                //get emp_id from the spinner,
                //showing alert Dialog here
                new AlertDialog.Builder(getContext(), R.style.CustomDialogTheme)
                        .setIcon(R.drawable.ic_warning_white)
                        .setTitle("Confirm Payroll Structure Update!")
                        .setMessage("Do You Want to Update Payroll Structure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Request for the Leave
                                //get emp_id because only signed in users can request for leave
                                String empId = new SessionManager(getContext()).getEmpId();
                                Log.e(TAG, "empId is: " + empId);

                                //call the API to save these details into the database
                                //invoke the function with the API call with volley
                                //saveLeaveDetails(empId, leaveType, leaveSubject, leaveFrom, leaveTill, leaveTotalDays, leaveDesc, requestDate);
                                //find the higher Authority from db (to whom I am Reporting )
                                //sending leave request notification to the higher authority
//                                Log.e(TAG, "Calling sendNotification()...");
                                //sendNotification(new SessionManager(getContext()).getUserName());//pass employee name to the function to display it in the notification
                                //show home posts fragment after background request
                                //startActivity(new Intent(RegisterNewEmpActivity.this, AdminDashboardActivity.class));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss the alert dialog
                                dialog.dismiss();
                            }
                        })
                        .show().setCanceledOnTouchOutside(false);


            }
        });
    }

    private void setEditable() {
        Log.e(TAG, "setEditable function called!!!");
        ListView lvName = getView().findViewById(R.id.payrollStructureList);
        View v;
        EditText et;
        int listLength = lvName.getChildCount();
        Log.e(TAG, "Length of list is: " + listLength);
//        String[] valueOfEditText = new String[listLength];
        for (int i = 0; i < listLength; i++) {
            v = lvName.getChildAt(i);
            et = v.findViewById(R.id.tv_payslip_structure_value);
            Log.e(TAG, "value of et: " + et.getText().toString());
            et.setFocusable(true);
            et.setEnabled(true);
            et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
//            valueOfEditText[i] = et.getText().toString();
        }
//        Log.e(TAG,"valueOfEditText : "+ Arrays.toString(valueOfEditText));
//        edNetSalary.setText(String.valueOf(getTotalSalary(valueOfEditText)));


    }

    private void getSalarySum() {
        Log.e(TAG, "getSumSalary function called!!!");
        ListView lvName = getView().findViewById(R.id.payrollStructureList);
        View v;
        EditText et;
        int listLength = lvName.getChildCount();
        Log.e(TAG, "Length of list is: " + listLength);
        String[] valueOfEditText = new String[listLength];
        for (int i = 0; i < listLength; i++) {
            v = lvName.getChildAt(i);
            et = v.findViewById(R.id.tv_payslip_structure_value);
            valueOfEditText[i] = et.getText().toString();
        }
        Log.e(TAG, "valueOfEditText : " + Arrays.toString(valueOfEditText));
        edNetSalary.setText(String.valueOf(getTotalSalary(valueOfEditText)));


    }

    private float getTotalSalary(String[] valueOfEditText) {
        float sum = 0.f;
        for (String num : valueOfEditText) {
            //convert num into integer
//            then add it to the sum
            sum = sum + Float.parseFloat(num);

        }
        Log.e(TAG, "sum of salary is : " + sum);
        return sum;
    }

    private void parsePayrollStructure(final String empId) {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Getting you on board", "Please Wait...", false, false);
        StringRequest request = new StringRequest(Request.Method.POST, API_URLs.getEmpPayrollStructureAPIUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "ServerResponse is: " + response);

                try {
                    Log.e(TAG, "ServerResponse is: " + response);
                    if (response.trim().equals("NoRecordFound")) {
                        Log.e(TAG, "No Payroll Structure Found Show Dummy Structure here");
                        progressDialog.dismiss();
                    } else {
                        JSONObject jsonObjectResponse = new JSONObject(response);
                        String title, value;
                        JSONArray jsonArray = jsonObjectResponse.getJSONArray("server_response");
                        Log.e(TAG, "Structure<><><><> details are: " + jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            title = jsonObject.getString("title");
                            value = jsonObject.getString("value");

                            //got the values now send them to the POJO (Model) Class
                            PayrollStructure payrollStructure = new PayrollStructure(title, value);
                            payrollStructureAdapter.add(payrollStructure);
                            // getSalarySum();//to get the total salary
                        }
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Json Exception: " + e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "Server returns error response");
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Network Error... Please Try again Later...", Toast.LENGTH_SHORT).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);//add the request to requestQueue queue
    }

}
