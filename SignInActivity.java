package com.lms.admin.lms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    static final int REQ_CODE = 9001;
    private static final String TAG = "SignInActivity";
    SignInButton btnSignIn;
    GoogleApiClient googleApiClient;
    ProgressBar progressBar;
    SessionManager sessionManager;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);
        progressBar = findViewById(R.id.progress_bar);
        TextView textView = (TextView) btnSignIn.getChildAt(0);
        textView.setText(R.string.lbl_btn_google_sign);
        sessionManager = new SessionManager(this);

// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
//        check if user is already logged in
        Log.e(TAG, "isLoggedIn: " + sessionManager.isLoggedIn());
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        //get email from the shared preference to authenticate user
        // GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        sessionManager.getUserDetails();
//        Log.e(TAG, "Last SignIn Account is " + account.getEmail());
//        updateUI(account.getEmail());//send Email to UpdateUI
    }

    @Override
    public void onClick(View v) {
//        Log.e(TAG, "OnClickListener Invoked ..");

        switch (v.getId()) {
            case R.id.btn_sign_in:
//                Log.e(TAG, "Sign in Button Called ...");
                signOut();//signOut first to show available email accounts
                signIn();
                break;
        }
    }

    private void signIn() {
        startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(googleApiClient), REQ_CODE);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
//                updateUI(null); //now signedOuts
//                Log.e(TAG, "status: " + status);
            }
        });

    }

    private void handleResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Log.e(TAG, "Sign in Successful");
            GoogleSignInAccount account = result.getSignInAccount();
            if (account != null) {
                //String name = account.getDisplayName(); // for Full Name
                String email = account.getEmail();
                //String img_url = account.getPhotoUrl().toString();
                updateUI(email);
            }
        } else {
//            Log.e(TAG, "Sign in NOT Successful show toast try again");
            updateUI(null);
        }
    }

    private void updateUI(String email) {
        if (email != null) {
            //google sign in successful
            //now authenticate the user with the database
            //Toast.makeText(getApplicationContext(), "Login Successful Enjoy Our App", Toast.LENGTH_LONG).show();
            //generate new volley background request here
            requestQueue = Volley.newRequestQueue(SignInActivity.this);
            //Call the function to validate user email with the database
            checkAuthorization(email);//



          /*  BackgroundTask backgroundTask = new BackgroundTask(this);
            backgroundTask.execute(email);//pass user email and verify it*/

        } else {
            Toast.makeText(getApplicationContext(), "Login Failed... Try Again", Toast.LENGTH_LONG).show();
        }
    }

    private void checkAuthorization(final String email) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Getting you on board", "Please Wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URLs.verifyEmailAPIUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
//                Log.e(TAG, "response is: " + response);
                // got our result in JSON format now parse it if error comes means user is not authorized
                try {
                    JSONObject mainJsonObject = new JSONObject(response);
                    JSONArray jsonArray = mainJsonObject.getJSONArray("server_response");
//                    Log.e(TAG, "jsonArray is : " + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        Log.e(TAG, "json_object2 is : " + jsonObject.toString());
//                        empId = jsonObject.getString("emp_id");
//                        email_address = jsonObject.getString("email_address");
                        Log.e(TAG, "before Pref Values Are : " + Collections.singletonList(sessionManager.getUserDetails()));
                        //store user details in the session preference
                        sessionManager.createLoginSession(jsonObject.getString("emp_id"), jsonObject.getString("email_address")); //shared preference created and email is set to current user email value
                        Log.e(TAG, "after Pref Values Are : " + Collections.singletonList(sessionManager.getUserDetails()));
                    }
                    Toast.makeText(getApplicationContext(), "Login Successful Enjoy Our App", Toast.LENGTH_SHORT).show();

                    //user login successful so now store device token in the database
                    Log.e(TAG, "User Email is :" + email);
                    String refreshedToken = FirebaseInstanceId.getInstance().getToken();//the magical line to be used whenever we want the device token...
                    Log.e(TAG, "SignIn Activity Device Token is: :" + refreshedToken);

                    if (refreshedToken != null && sessionManager.getEmpId() != null) {
                        //token is generated so now call a method to store the token in the database
                        sendRegistrationToServer(sessionManager.getEmpId(), refreshedToken);
                    }

                    //now check if user in Admin or Employee
                    //if Admin then show Admin Panel else show Employee HomeFeed Activity
                    //checkUserType();//function to be written here
                    if (sessionManager.getUserEmail().equals("sunil.suthar@connexistech.com")) {
                        //fr Admin
                        startActivity(new Intent(SignInActivity.this, AdminDashboardActivity.class));
                    } else {
                        //for Employees
                        startActivity(new Intent(SignInActivity.this, UserHomeActivity.class));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(SignInActivity.this, "Invalid Credentials...", Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, "before Pref Values Are : " + Collections.singletonList(sessionManager.getUserDetails()));
                    //clear user session by changing the preferences (Session Details)
                    sessionManager.clearPreferences();
//                    Log.e(TAG,"this is Error: "+e);
//                    Log.e(TAG, "after Pref Values Are : " + Collections.singletonList(sessionManager.getUserDetails()));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "Error : " + error);
                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, "Volley Network Error... Please Try again Later...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_email_addr", email);
                Log.e(TAG, "Params :" + params.toString());
                return params;
            }
        };
        //for default retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

        /*
        //        Log.e(TAG, "request sent>>>");
        @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e(TAG, "JSONObj: " +response);

                    String empId, email;
                    JSONArray jsonArray = response.getJSONArray("server_response");
                    Log.e(TAG, "Response details are: " + jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        empId = jsonObject.getString("emp_id");
                        email = jsonObject.getString("email_address");
                        Log.e(TAG, "emp_id: " + empId + " | email:" + email);
                        //store user details in the session preference
                        sessionManager.createLoginSession(empId, email); //shared preference created and email is set to current user email value
                        Toast.makeText(getApplicationContext(), "Yey Login Successful Enjoy Our App", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Pref Values Are : " + Collections.singletonList(sessionManager.getUserDetails()));
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "Error : " + error);

                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, "Volley Network Error... Please Try again Later...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_email_addr", email);
                Log.e(TAG,"Params :"+params.toString());
                return params;
            }
        };
        //for default retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
//        Log.e(TAG, "request sent>>>");


*/
    }


    private void sendRegistrationToServer(final String empId, final String refreshedToken) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Getting you on board", "Please Wait...", false, false);
//        final StringRequest request = new StringRequest(Request.Method.POST, API_URLs.verifyEmailAPIUrl,null, new Response.Listener<JSONObject>() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URLs.sendRegistrationTokenToServerAPIUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e(TAG, "response is: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "Error : " + error);
                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, "Volley Network Error... Please Try again Later...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("device_token", refreshedToken);
                params.put("emp_id", empId);
                Log.e(TAG, "Params :" + params.toString());
                return params;
            }
        };
        //for default retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

        /*
        //        Log.e(TAG, "request sent>>>");
        @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e(TAG, "JSONObj: " +response);

                    String empId, email;
                    JSONArray jsonArray = response.getJSONArray("server_response");
                    Log.e(TAG, "Response details are: " + jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        empId = jsonObject.getString("emp_id");
                        email = jsonObject.getString("email_address");
                        Log.e(TAG, "emp_id: " + empId + " | email:" + email);
                        //store user details in the session preference
                        sessionManager.createLoginSession(empId, email); //shared preference created and email is set to current user email value
                        Toast.makeText(getApplicationContext(), "Yey Login Successful Enjoy Our App", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Pref Values Are : " + Collections.singletonList(sessionManager.getUserDetails()));
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "Error : " + error);

                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, "Volley Network Error... Please Try again Later...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_email_addr", email);
                Log.e(TAG,"Params :"+params.toString());
                return params;
            }
        };
        //for default retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
//        Log.e(TAG, "request sent>>>");


*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }


   /*
    //<<< background task  to be run on another thread for user sign in >>>
    @SuppressLint("StaticFieldLeak")
    class BackgroundTask extends AsyncTask<String, Void, String> {
        Context context;
        private String verify_email_url;

        BackgroundTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
//            verify_email_url = "http://192.168.0.128/hrms_app/verify_email.php";//old URL
//            verify_email_url = "http://192.168.0.119/hrms_app/verify_email.php";//new URL changed IP
//            Log.e(TAG, "URL is :" + verify_email_url);

        }

        @Override
        protected String doInBackground(String... args) {
            String email;
            email = args[0];
//            Log.e(TAG, "email is : " + email);

            try {
                URL url = new URL(API_URLs.verifyEmailAPIUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
//                Log.e(TAG, "OutputStream created");
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
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
                Log.e(TAG, "result is : " + result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }
            return "Something went wrong Try Again Never say Never!!!!";

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            if (result != null) {
                String[] values = result.split(",");
                Log.e(TAG, "Values Are : " + Arrays.toString(values));

                sessionManager.createLoginSession(values[0], values[1]); //shared preference created and email is set to current user email value
                Toast.makeText(getApplicationContext(), "Login Successful Enjoy Our App", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Pref Values Are : " + Collections.singletonList(sessionManager.getUserDetails()));
//                Toast.makeText(getApplicationContext(), "You Are An ADMIN man", Toast.LENGTH_LONG).show();
            }

        }
    }
*/
}

