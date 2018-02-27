package com.lms.admin.lms;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

public class RegisterNewEmpActivity extends AppCompatActivity {
    EditText edDateOfJoining;
    java.util.Calendar mDateOfJoining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_emp);
        edDateOfJoining = (EditText) findViewById(R.id.edDateOfJoining
        );
        edDateOfJoining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateOfJoining = java.util.Calendar.getInstance();
                int year = mDateOfJoining.get(java.util.Calendar.YEAR);
                int month = mDateOfJoining.get(java.util.Calendar.MONTH);
                int day = mDateOfJoining.get(java.util.Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterNewEmpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        mDateOfJoining.set(selectedYear, selectedMonth, selectedDayOfMonth);

                        selectedMonth++;// months are from 0 to 11
                        edDateOfJoining.setText(selectedDayOfMonth + "/" + selectedMonth + "/" + selectedYear);


                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }
}
