package it446.nfp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philip.Jones on 1/25/2016.
 */




public class GetObjects extends AppCompatActivity {

    private List<DoctorListItem> mSearchDoctorList = new ArrayList<DoctorListItem>();
    private ListView searchResultListView;


    // json array response url
    private static String urlJsonArry = "http://nfp.capstone.it.et.byu.edu/";
    private static String urlGetDocArray = urlJsonArry+"getlist/doctor";
    private static String TAG = ViewDoctorActivity.class.getSimpleName();
    // temporary string to show the parsed response
    //private String jsonResponse;

    // Progress dialog
    //private ProgressDialog pDialog;
    private String jsonResponseDoctorName, jsonResponseDoctorClinic;

    public List<DoctorListItem> makeDocArray() {
        String jsonResponseDoc;
//        ProgressDialog pDialog;
//        pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Please wait...");
//        pDialog.setCancelable(false);

//        showpDialog();



        JsonArrayRequest req = new JsonArrayRequest(urlGetDocArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            //jsonResponseDoc = "";
                            if (mSearchDoctorList!=null && mSearchDoctorList.size() == 0) {
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject doctor = (JSONObject) response
                                            .get(i);
                                    jsonResponseDoctorName = doctor.getString("Name");
                                    jsonResponseDoctorClinic = doctor.getString("Clinic");

                                    DoctorListItem doctorListItem = new DoctorListItem();
                                    doctorListItem.setmIconId(R.mipmap.ic_accept_circle);
                                    doctorListItem.setmDoctorName(jsonResponseDoctorName);
                                    doctorListItem.setmDoctorClinic(jsonResponseDoctorClinic);
                                    mSearchDoctorList.add(doctorListItem);

                                }
                                //populateListView();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(),
//                                    "Error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
                        }

//                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hidepDialog();
            }
        });

        // Adding request to request queue
        ApplicationController.getInstance().addToRequestQueue(req);

        return mSearchDoctorList;
    }

//    private void showpDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }
//
//    private void hidepDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }





}
