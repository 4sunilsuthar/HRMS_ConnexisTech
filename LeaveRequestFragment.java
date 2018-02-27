package com.lms.admin.lms;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class LeaveRequestFragment extends Fragment {
    private static final String TAG = "LeaveRequestFragment";
    EditText edDateFrom, edDateTo;
    TextView totalLeaveDays;
    java.util.Calendar mDateFrom, mDateTo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning the view --> return view
        return inflater.inflate(R.layout.fragment_leave_request, container, false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        totalLeaveDays = getView().findViewById(R.id.tvTotalLeaveDays);
        edDateFrom = getView().findViewById(R.id.edDateFrom);
        edDateTo = getView().findViewById(R.id.edDateTo);
        //disable the edDateTo to get minDate from the edDateFrom datePicker
        edDateTo.setEnabled(false);
        final Spinner spinLeaveType = getView().findViewById(R.id.leave_type_spinner);
        List<String> leaveTypes = new ArrayList<>();

        leaveTypes.add("Medical Leave");
        leaveTypes.add("Casual Leave");
        leaveTypes.add("Other Leave");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, leaveTypes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLeaveType.setAdapter(dataAdapter);
        spinLeaveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //on selecting an item from spinner
                spinLeaveType.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //when no item is selected

            }
        });


        //select leave start date
        edDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateFrom = java.util.Calendar.getInstance();
                int year = mDateFrom.get(java.util.Calendar.YEAR);
                int month = mDateFrom.get(java.util.Calendar.MONTH);
                int day = mDateFrom.get(java.util.Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialogFrom = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        mDateFrom.set(selectedYear, selectedMonth, selectedDayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM", Locale.US);
                        String formattedDate = dateFormat.format(mDateFrom.getTime());
                        Log.e(TAG, "Date is " + mDateFrom.getTime());
                        edDateFrom.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        edDateFrom.setText(formattedDate);
                        //enable dateTo editText for toDate
                        edDateTo.setEnabled(true);

                    }
                }, year, month, day);
                //setting calender to have future dates only restricting the past dates
                datePickerDialogFrom.getDatePicker().setMinDate(System.currentTimeMillis());
                Long dur = 5184000000L; //for two months duration @@@@@@
                datePickerDialogFrom.getDatePicker().setMaxDate(System.currentTimeMillis() + dur);
                datePickerDialogFrom.show(); //show the datePicker
            }
        });

        //select leave end date
        edDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateTo = java.util.Calendar.getInstance();
                int year = mDateTo.get(java.util.Calendar.YEAR);
                int month = mDateTo.get(java.util.Calendar.MONTH); // months are from 0 to 11
                int day = mDateTo.get(java.util.Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialogTo = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        mDateTo.set(selectedYear, selectedMonth, selectedDayOfMonth);

                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM");
                        String formattedDate = dateFormat.format(mDateTo.getTime());
                        edDateTo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        edDateTo.setText(formattedDate);
                        //calculate the date difference and show it as total days
                        int days = daysBetween(mDateFrom, mDateTo);
                        totalLeaveDays.setText(days);

                    }
                }, year, month, day);
                datePickerDialogTo.getDatePicker().setMinDate(mDateFrom.getTimeInMillis());
                Long dur = 5184000000L; //for two months duration @@@@@@
                datePickerDialogTo.getDatePicker().setMaxDate(mDateFrom.getTimeInMillis() + dur);
                datePickerDialogTo.show();
            }
        });
    }

    //function to calculate total leave days from two given dates
    public int daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        int totalLeaveDuration = (int) TimeUnit.MILLISECONDS.toDays(Math.abs(end - start) + 1);
//        return (int) TimeUnit.MILLISECONDS.toDays(Math.abs(end - start)+1);

        //logic to exclude weekends from total leave days
        int day1 = startDate.get(Calendar.DAY_OF_WEEK);
        Log.e(TAG, "startDate Day is : " + day1);
        int totalWorkingDays = 0;
        for (int i = 0; i <= totalLeaveDuration; i++, day1++) {
            if (day1 != Calendar.SATURDAY && day1 != Calendar.SUNDAY) {
                totalWorkingDays += 1;//=> ++
            }
            if (day1 == 7) {
                day1 = 0;
            }
        }
        return totalWorkingDays;

    }

/*    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        int workDays = 0;

        Calendar c = Calendar.getInstance();
        c.setTime(d1);
        int day1 = c.get(Calendar.DAY_OF_WEEK);
        //logic to exclude weekends from total leave days
        Log.e(TAG,"d1 time is : "+day1);


        Log.e(TAG,"d2 time is : "+d2.getTime());

//        Log.e(TAG,"working days are : "+diff);
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }*/

   /* public static int daysBetween(Calendar startCal, Calendar endCal) {

        int workDays = 0;
//        Log.e(TAG,"start date is: "+startCal.getTime());
//        Log.e(TAG,"end date is : "+endCal.getTime());

        //Return 0 if start and end are the same
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 1;
        }

        *//*if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }*//*
        Calendar tempCal = startCal;

        do {
            //excluding start date
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
//            Log.e(TAG,"day of month is : "+startCal.get(Calendar.DAY_OF_WEEK));
            if (tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
        } while (tempCal.getTimeInMillis() <= endCal.getTimeInMillis()); //excluding end date
//        Log.e(TAG,"working days are : "+workDays);
        return workDays;
    }*/


}