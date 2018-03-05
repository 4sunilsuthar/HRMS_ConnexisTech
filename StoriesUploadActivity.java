package com.lms.admin.lms;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class StoriesUploadActivity extends AppCompatActivity {
    public static final int RequestPermissionCode = 1;

    private static final String TAG = "StoriesUploadActivity"; //for debugging
    EditText edPostTextMsg;
    Button btnUploadImg, btnUploadPost, btnCancel;
    ImageView imgPostPreview, imgCompanyLogo, imgAppLogo;
    TextView tvHeading;
    Bitmap bitmap;
    boolean check = true;
    ProgressDialog progressDialog;
    String ServerUploadPathURL = "http://192.168.0.128/hrms_app/img_upload_to_server.php";
    SessionManager sessionManager;
    private String convertImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories_upload);
        edPostTextMsg = findViewById(R.id.edTextPost);
        btnUploadImg = findViewById(R.id.btnUploadImg);
        btnUploadPost = findViewById(R.id.btnPostStory);
        btnCancel = findViewById(R.id.btnCancelStory);
        imgPostPreview = findViewById(R.id.imgPreview);
        imgCompanyLogo = findViewById(R.id.ImgLogo);
        imgAppLogo = findViewById(R.id.imgAppLogo);
        tvHeading = findViewById(R.id.tvHeading);
        sessionManager = new SessionManager(this);
        //for the action bar back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //uploading the story post on btnSubmit Click
        btnUploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              Call the function to upload the post to the server
                ImageUploadToServerFunction();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        //check if user is logged in
        Log.e(TAG, String.valueOf(sessionManager.getUserDetails()));
        sessionManager.checkLogin();

    }

    //for back button on the title bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void uploadImageFun(View view) {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
//                get Image URL
                Uri resultUri = result.getUri();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    imgPostPreview.setImageBitmap(bitmap);
                    //hiding the logo and other views to show the image preview
                    imgCompanyLogo.setVisibility(View.GONE);
                    imgAppLogo.setVisibility(View.GONE);
                    tvHeading.setVisibility(View.GONE);
                    imgPostPreview.setVisibility(View.VISIBLE);
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.MATCH_PARENT, 0);
//                    imgPostPreview.setLayoutParams(params);
//                    // btnUploadImg.setText("Change Photo");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }
    }

    public void ImageUploadToServerFunction() {
        //before uploading the story check the validations and then proceed accordingly...

        //Toast.makeText(this, "Uploading Post Function Called ", Toast.LENGTH_SHORT).show();


        //validations here

        if (TextUtils.isEmpty(edPostTextMsg.getText()) && imgPostPreview.getDrawable() == null) {
            Toast.makeText(this, "Nothing to Upload...", Toast.LENGTH_SHORT).show();
            return;// not performing any background task
        }

        if (bitmap != null) {

            ByteArrayOutputStream byteArrayOutputStreamObject;
            byteArrayOutputStreamObject = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
            byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
            convertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
        }

        //calling the background task  to begin execution on secondary thread
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
        //end of if validation block
    }

    //background task class that extends the AsyncTask class
    @SuppressLint("StaticFieldLeak")
    class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(StoriesUploadActivity.this, "Uploading Image", "Please Wait...", false, false);
        }

        @Override
        protected String doInBackground(Void... params) {
            //imageProcessClass to process the image
            ImageProcessClass imageProcessClass = new ImageProcessClass();
            //HashMap for values
            HashMap<String, String> HashMapParams = new HashMap<>();

            //finding values to be stored in the database
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            //get current date
            String mCurrentDate = sdf.format(cal.getTime());

            sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
            String mCurrentTime = sdf.format(cal.getTime());
            Log.e(TAG, "current Date is : " + mCurrentDate);
            Log.e(TAG, "current Time is : " + mCurrentTime);
            String txtMsg = edPostTextMsg.getText().toString();
            if (txtMsg.isEmpty()) {
                txtMsg = null;
            }
            //get emp_id from the shared preference obj
            HashMap<String, String> userDetails = sessionManager.getUserDetails();
//            Log.e(TAG, "emp_id is : " + userDetails.get("emp_id"));

            //add more values here to be uploaded to the server
            HashMapParams.put("date", mCurrentDate);
            HashMapParams.put("text_message", txtMsg);
            HashMapParams.put("times", mCurrentTime);
            HashMapParams.put("image_url", convertImage);
            HashMapParams.put("added_by", userDetails.get("emp_id"));

//            Log.e(TAG,"time is "+HashMapParams.get("times"));
//            HashMapParams.put("is_active", "true");
            //returning data to the server as a String
            return imageProcessClass.ImageHttpRequest(ServerUploadPathURL, HashMapParams);
        }

        @Override
        protected void onPostExecute(String string1) {
//            Log.e(TAG, "String response is : " + string1);
            super.onPostExecute(string1);
            // Dismiss the progress dialog after done uploading.
            progressDialog.dismiss();
            // Printing uploading success message coming from server on android app.
            Toast.makeText(getApplicationContext(), string1, Toast.LENGTH_SHORT).show();
//            Log.e(TAG,"msg is : "+string1);

            startActivity(new Intent(getApplicationContext(), AdminDashboardActivity.class));
            // Setting image as transparent after done uploading.
            //imgPostPreview.setImageResource(android.R.color.transparent);
        }
    }

    public class ImageProcessClass {

        String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url;
                HttpURLConnection httpURLConnectionObject;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject;
                BufferedReader bufferedReaderObject;
                int RC;
                url = new URL(requestURL);
                httpURLConnectionObject = (HttpURLConnection) url.openConnection();
                httpURLConnectionObject.setReadTimeout(19000);
                httpURLConnectionObject.setConnectTimeout(19000);
                httpURLConnectionObject.setRequestMethod("POST");
                httpURLConnectionObject.setDoInput(true);
                httpURLConnectionObject.setDoOutput(true);
                OutPutStream = httpURLConnectionObject.getOutputStream();
                bufferedWriterObject = new BufferedWriter(new OutputStreamWriter(OutPutStream, "UTF-8"));
                bufferedWriterObject.write(bufferedWriterDataFN(PData));
//                Log.e(TAG,"map object is : "+PData.toString());

                bufferedWriterObject.flush();
                bufferedWriterObject.close();
                OutPutStream.close();
                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {
                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReaderObject.readLine()) != null) {
                        stringBuilder.append(RC2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;
            stringBuilderObject = new StringBuilder();
//            Log.e(TAG,"HashMapParams is : "+HashMapParams.toString());
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilderObject.append("=");

                if (KEY.getValue() == null) {
                    stringBuilderObject.append(URLEncoder.encode("null"));
                } else {
                    stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
                }
            }
//            Log.e(TAG, "Data Sent is : " + stringBuilderObject.toString());
            return stringBuilderObject.toString();
        }
    }
}



