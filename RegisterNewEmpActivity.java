package com.lms.admin.lms;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RegisterNewEmpActivity extends AppCompatActivity {

    private static final String TAG = "RegisterNewEmpActivity";
    EditText edName, edDateOfJoining, edEmail;
    Spinner spDesignation;
    ProgressBar progressBar;
    Button btnSubmit, btnReset;
    java.util.Calendar mDateOfJoining;
    private String name, designation, dateOfJoining, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_emp);

        spDesignation = findViewById(R.id.spEmpDesignation);
        progressBar = findViewById(R.id.progress_bar_fetch_designation);
        edName = findViewById(R.id.edEmpName);
        edEmail = findViewById(R.id.edEmpEmail);
        btnSubmit = findViewById(R.id.btnRegisterEmp);
        btnReset = findViewById(R.id.btnResetRegistration);
        edDateOfJoining = findViewById(R.id.edDateOfJoining);

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
                        //date format for the user interface
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                        //date format for the database date type
                        SimpleDateFormat dateDbFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        String formattedDate = dateFormat.format(mDateOfJoining.getTime());


//                        Log.e(TAG, "Date is " + mDateOfJoining.getTime());

//                        edDateOfJoining.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);//hiding the calendar icon from the editText
                        edDateOfJoining.setText(formattedDate);
                        //setting dateOfJoining for the database format
                        dateOfJoining = dateDbFormat.format(mDateOfJoining.getTime());
                        Log.e(TAG, "Formatted Date is " + dateOfJoining);
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
                //on click of this button perform various operations like
                // #1. check validations of all the fields
                // #2. call the alertDialog for confirmation of registration
                // #3.then call the background task that manages data insertion in the db tables.
                // #4. then show the Toast that employee added successfully ..

                // #1.call the alertDialog for confirmation of registration
                new AlertDialog.Builder(RegisterNewEmpActivity.this, R.style.CustomDialogTheme)
                        .setIcon(R.drawable.ic_warning_white)
                        .setTitle("Confirm Registration")
                        .setMessage("Do You Want To Register ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Register the new Employee
                                //code here to save given details

                                //run background task to save emp details
                                //get all values

                                name = edName.getText().toString();
                                email = edEmail.getText().toString();
                                designation = spDesignation.getSelectedItem().toString();
                                //dateOfJoining = edDateOfJoining.getText().toString();

//                                call background task to save data in the database
                                SaveEmpData saveEmpData = new SaveEmpData(name, email, dateOfJoining, designation);
                                saveEmpData.execute();//calling background task
                                //now go to admin dashboard
                                startActivity(new Intent(RegisterNewEmpActivity.this, AdminDashboardActivity.class));

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

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set all input fields to empty
                edName.setText("");
                edDateOfJoining.setText("");
                edEmail.setText("");
                spDesignation.setSelection(1);

            }
        });

        //calling the background task to populate the designations in the spinner
        GetJSONDesignation getJSON = new GetJSONDesignation();
        getJSON.execute();
    }

    /*
        * As fetching the json string is a network operation
        * And we cannot perform a network operation in main thread
        * so we need an AsyncTask
        * The constrains defined here are
        * Void -> We are not passing anything
        * Void -> Nothing at progress update as well
        * String -> After completion it should return a string and it will be the json string
        * */
    @SuppressLint("StaticFieldLeak")
    class GetJSONDesignation extends AsyncTask<Void, Void, String> {
        Context context;
        String getDesignations_url;

        //this method will be called before execution
        //you can display a progress bar or something
        //so that user can understand that he should wait
        //as network operation may take some time
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            getDesignations_url = "http://192.168.0.128/hrms_app/get_designations.php";
//            Log.e(TAG, "URL is :" + url);

        }

        //in this method we are fetching the json string
        @Override
        protected String doInBackground(Void... voids) {


            try {
                //creating a URL
                URL url = new URL(getDesignations_url);

                //Opening the URL using HttpURLConnection
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                //StringBuilder object to read the string from the service
                StringBuilder result = new StringBuilder();

                //We will use a buffered reader to read the string from service
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                //A simple string to read values from each line
                String json_line;

                //reading until we don't find null
                while ((json_line = bufferedReader.readLine()) != null) {

                    //appending it to string builder
                    result.append(json_line);
//                    result.append(json_line + "\n");
                }

                bufferedReader.close();
                httpURLConnection.disconnect();
                Log.e(TAG, "result is : " + result);
                //finally returning the read string
                return result.toString().trim();
            } catch (Exception e) {
                return null;
            }

        }

        //this method will be called after execution
        //so here we are displaying a toast with the json string
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            Log.e(TAG, "result is : " + result);
            parseJson(result);//parsing the result
//            Log.e(TAG, "MyList is "+myList.toString());
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

        }

        public void parseJson(String jsonData) {
            String designationName;
            List<String> designationList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;

                while (count < jsonArray.length()) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(count);
                    designationName = jsonObject2.getString("name");
                    designationList.add(designationName);
                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayAdapter<String> dataAdapter;
            dataAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, designationList);
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

        }//end of parseJSON
    }

    @SuppressLint("StaticFieldLeak")
    class SaveEmpData extends AsyncTask<Void, Void, String> {
        String register_new_emp_url;
        String name, email, dateOfJoining, designation;

        public SaveEmpData(String name, String email, String dateOfJoining, String designation) {
            this.name = name;
            this.email = email;
            this.dateOfJoining = dateOfJoining;
            this.designation = designation;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
//            register_new_emp_url = "http://192.168.0.128/hrms_app/register_new_emp.php";// old URL
            register_new_emp_url = "http://192.168.0.119/hrms_app/register_new_emp.php";// new URL with changed IP
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(register_new_emp_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("date_of_joining", "UTF-8") + "=" + URLEncoder.encode(dateOfJoining, "UTF-8") + "&" +
                        URLEncoder.encode("designation", "UTF-8") + "=" + URLEncoder.encode(designation, "UTF-8");

                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
//                Log.e(TAG, "result is : " + result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result.toString(); //returning result from the web service
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }
            return "Something Went Wrong... Please Try Again!!!!";


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            try {
                Toast.makeText(RegisterNewEmpActivity.this, result, Toast.LENGTH_SHORT).show();
                Thread.sleep(Toast.LENGTH_SHORT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
