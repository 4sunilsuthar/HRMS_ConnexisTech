package com.lms.admin.lms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class EditPaySlipFragment extends Fragment {

    private static final String TAG = "EditPaySlipFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_pay_slip, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvNetSalary = getView().findViewById(R.id.tv_net_salary);
        Button btnUpdatePayStructure = getView().findViewById(R.id.btnUpdateSalary);

        //get Bundle here and create number of textViews and EditTexts with default values from the Bundle
        Bundle arguments = getArguments();
        assert arguments != null;
//        String empId = arguments.getString("myEmpID"); //getting the empId of the selected employee
        Log.e(TAG, "Bundle Received...");
        Log.e(TAG, "Bundle Size is ..." + arguments.size());

        List<String> valuesList = new ArrayList<>();
        for (String key : arguments.keySet()) {
            Log.d(TAG, key + ": " + arguments.getString(key));
            LinearLayout mLayout = getView().findViewById(R.id.layout_edit_payroll);

            TextView myTextView = new TextView(getContext());
            myTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            )); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            myTextView.setTextAppearance(R.style.MyTextViewStyle);
            myTextView.setText(key);
            mLayout.addView(myTextView);

            Log.e(TAG, "myTextView value is: " + myTextView.getText().toString());

            EditText myEditText = new EditText(getContext()); // Pass it an Activity or Context
            myEditText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            )); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            //generate edit_text id based on key (title)

            myEditText.setId(View.generateViewId());
            Log.e(TAG, "generated id: " + myEditText.getId());
            myEditText.setTextAppearance(R.style.MyEditTextStyle);
            myEditText.setText(arguments.getString(key));
            mLayout.addView(myEditText);
            valuesList.add(myEditText.getText().toString());

            Log.e(TAG, "myEditText value is: " + myEditText.getText().toString());
        }
        tvNetSalary.setText(String.valueOf(getTotalSalary(valuesList)));

        btnUpdatePayStructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code here to update values in the database
                //fetch all the values from the edit texts
                //fetch all the values and then display them in log

                //validate for empty values
                //show an Alert Dialog

            }
        });
    }

    private float getTotalSalary(List<String> valueOfEditText) {
        float sum = 0.f;
        for (String num : valueOfEditText) {
            //convert num into integer
//            then add it to the sum
            sum = sum + Float.parseFloat(num);

        }
        Log.e(TAG, "sum of salary is : " + sum);
        return sum;
    }
}