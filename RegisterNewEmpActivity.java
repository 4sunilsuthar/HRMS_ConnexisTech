package com.lms.admin.lms;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RegisterNewEmpActivity extends AppCompatActivity {

    private static final String TAG = "RegisterNewEmpActivity";
    EditText edName, edDateOfJoining, edEmail;
    Spinner spDesignation;
    Button btnSubmit, btnReset;
    java.util.Calendar mDateOfJoining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_emp);

        spDesignation = findViewById(R.id.spEmpDesignation);
        edName = findViewById(R.id.edEmpName);
        edEmail = findViewById(R.id.edEmpEmail);
        btnSubmit = findViewById(R.id.btnRegisterEmp);
        btnReset = findViewById(R.id.btnResetRegistration);
        edDateOfJoining = findViewById(R.id.edDateOfJoining);

        //get List of Designations
        List<String> designationsList = new ArrayList<>();

        designationsList.add("Manager");
        designationsList.add("CTO");
        designationsList.add("Tech Lead");
        designationsList.add("Developer");
        designationsList.add("Intern");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, designationsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDesignation.setAdapter(dataAdapter);
        spDesignation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //on selecting an item from spinner
                spDesignation.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //when no item is selected

            }
        });


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
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                        String formattedDate = dateFormat.format(mDateOfJoining.getTime());
                        Log.e(TAG, "Date is " + mDateOfJoining.getTime());
                        edDateOfJoining.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        edDateOfJoining.setText(formattedDate);
                    }
                }, year, month, day);

                //setting calender to have future dates only restricting the past dates
                Long dur = 5184000000L; //for two months duration @@@@@@

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + dur);

                datePickerDialog.show();
            }
        });//end of dateOfJoining Code

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
