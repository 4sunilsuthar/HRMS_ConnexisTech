package com.lms.admin.lms;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
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

/**
 * Created by Admin on 15-03-2018.
 */

public class SpinnerHelper {
    private static final String TAG = "SpinnerHelper";
    SpinnerCustomAdapter dataAdapter;
    List<SpinnerEmpDetails> empNamesList;
    private Context context;
    private RequestQueue requestQueue;
    private Spinner spEmpNames;

    SpinnerHelper(Context context, Spinner spEmpNames) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
        this.spEmpNames = spEmpNames;
    }


    void setSpinnerLayout(int heightPx) {

        // code for setting the height of the spinner dropdown
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spEmpNames);
            // Set popupWindow height to 2000px
            popupWindow.setHeight(heightPx);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }

    //volley background code to fetch emp details in the spinner
    void fetchJSONEmpNames() {

        final ProgressDialog progressDialog = ProgressDialog.show(context, "Getting you on board", "Please Wait...", false, false);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_URLs.getEmpNamesAPIUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    empNamesList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("server_response");
                    Log.e(TAG, "Result is: " + jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String profileUrl = jsonObject.getString("profileUrl");
                        String empId = jsonObject.getString("empId");
                        SpinnerEmpDetails details = new SpinnerEmpDetails(name, profileUrl, empId);
                        Log.e(TAG, "details list is: " + details.toString());
                        empNamesList.add(details);
                    }
                    dataAdapter = new SpinnerCustomAdapter(context, empNamesList);
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
                Toast.makeText(context, "Error in fetching Employee Names...Please Try again Later...", Toast.LENGTH_SHORT).show();
            }
        });
        //for default retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);


    }
    public List<SpinnerEmpDetails> getEmpList() {
        if (empNamesList != null) {
            return empNamesList;
        } else
            return null;
    }
}
