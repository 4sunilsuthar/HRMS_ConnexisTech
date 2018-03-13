package com.lms.admin.lms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ViewPayrollSlipActivity extends FragmentActivity {
    private static final String TAG = "ViewPayrollSlipActivity";
    public String empId = null;//for global reference
    FragmentManager fragment_manager;
    FragmentTransaction fragmentTrans;
    Fragment fragment;
    Spinner spEmpNames;
    SpinnerCustomAdapter dataAdapter;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payroll_slip);
        spEmpNames = findViewById(R.id.sp_emp_names);
        // code for setting the height of the spinner dropdown
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spEmpNames);

            // Set popupWindow height to 500px
            popupWindow.setHeight(2000);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        //background volley request here
        requestQueue = Volley.newRequestQueue(this);
        parseJSONEmpNames();

        spEmpNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //on selecting an item from spinner
//                Log.e(TAG, "Listener called : ");
                spEmpNames.setSelection(position);
                empId = dataAdapter.empGlobalId;
                Log.e(TAG, "empId is: " + empId);
                viewPayrollFragment(spEmpNames);

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

    //volley background code to fetch emp details in the spinner
    private void parseJSONEmpNames() {

        final ProgressDialog progressDialog = ProgressDialog.show(this, "Getting you on board", "Please Wait...", false, false);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_URLs.getEmpNamesAPIUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<SpinnerEmpDetails> empNamesList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("server_response");
                    Log.e(TAG, "Result is: " + jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String profileUrl = jsonObject.getString("profileUrl");
                        String empId = jsonObject.getString("empId");
                        SpinnerEmpDetails details = new SpinnerEmpDetails(name, profileUrl, empId);
                        empNamesList.add(details);
                    }
                    dataAdapter = new SpinnerCustomAdapter(getApplicationContext(), empNamesList);
                    spEmpNames.setAdapter(dataAdapter);
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(ViewPayrollSlipActivity.this, "Error in fetching Employee Names...Please Try again Later...", Toast.LENGTH_SHORT).show();
            }
        });
        //for default retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }


    public void viewPayrollFragment(View view) {
        fragment_manager = this.getSupportFragmentManager();
        fragmentTrans = fragment_manager.beginTransaction();

        if (view.getId() == R.id.sp_emp_names) {
            fragment = new ViewPayslipFragment();
            fragment_manager.popBackStack();//popping all from backStack
            fragmentTrans.replace(R.id.fragment_container, fragment).commit();
        }
        if (view.getId() == R.id.btnEditPayrollSlip) {
            if (empId.equals(null)) {
                Toast.makeText(ViewPayrollSlipActivity.this, "Please Select Employee First!...", Toast.LENGTH_SHORT).show();
            } else {
                fragment = new EditPaySlipFragment();
                fragment_manager.popBackStack();
                fragmentTrans.replace(R.id.fragment_container, fragment).commit();
                //change button and its events
            }
        }
        if (view.getId() == R.id.btnSummaryPayroll) {
            //change button and its events
            startActivity(new Intent(ViewPayrollSlipActivity.this, PayrollSummaryActivity.class));

        }
    }
}
