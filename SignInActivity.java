package com.lms.admin.lms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

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

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    static final int REQ_CODE = 9001;
    private static final String TAG = "SignInActivity";
    SignInButton btnSignIn;
    GoogleApiClient googleApiClient;
    ProgressBar progressBar;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);
        progressBar = findViewById(R.id.progress_bar);
        TextView textView = (TextView) btnSignIn.getChildAt(0);
        textView.setText(R.string.lbl_btn_google_sign);

// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Log.e(TAG, "Last SignIn Account is " + account.getEmail());

        //updateUI(account);
    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "OnClickListener Invoked ..");

        switch (v.getId()) {
            case R.id.btn_sign_in:
                Log.e(TAG, "Sign in Button Called ...");
                signIn();
                break;
        }
    }

    private void signIn() {

        startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(googleApiClient), REQ_CODE);

    }

    private void signout() {

    }

    private void handleResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            Log.e(TAG, "Sign in Successful");
            GoogleSignInAccount account = result.getSignInAccount();
            if (account != null) {
                //String name = account.getDisplayName(); // for Full Name
                String email = account.getEmail();
                //String img_url = account.getPhotoUrl().toString();
                updateUI(email);
            }
        } else {
            Log.e(TAG, "Sign in Successful");
            updateUI(null);
        }
    }

    private void updateUI(String email) {
        if (email != null) {
            //google sign in successful
            //now authenticate the user with the database
            //Toast.makeText(getApplicationContext(), "Login Successful Enjoy Our App", Toast.LENGTH_LONG).show();
            BackgroundTask backgroundTask = new BackgroundTask(this);
            backgroundTask.execute(email);//pass user email and verify it

        } else {
            Toast.makeText(getApplicationContext(), "Login Failed... Try Again", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }


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
            verify_email_url = "http://192.168.0.128/hrms_app/verify_email.php";
            Log.e(TAG, "URL is :" + verify_email_url);

        }

        @Override
        protected String doInBackground(String... args) {
            String email;
            email = args[0];
            Log.e(TAG, "email is : " + email);

            try {
                URL url = new URL(verify_email_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                Log.e(TAG, "OutputStream created");
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "", line;

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                Log.e(TAG, "result is : " + result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
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
                sessionManager.createLoginSession(result); //shared preference created and email is set to current user email value
                Toast.makeText(getApplicationContext(), "Login Successful Enjoy Our App", Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(), "You Are An ADMIN man", Toast.LENGTH_LONG).show();
            }

        }
    }
}

