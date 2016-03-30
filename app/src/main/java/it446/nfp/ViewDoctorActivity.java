package it446.nfp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

import it446.nfp.Adapters.ClinicRowAdapter;
import it446.nfp.Adapters.DoctorRowAdapter;

/**
 * Created by ChristensenKC on 11/29/2015.
 */
public class ViewDoctorActivity extends AppCompatActivity {
    private List<DoctorListItem> mSearchDoctorList = new ArrayList<DoctorListItem>();
    private ListView searchResultListView;

    // json array response url
    private String urlJsonArry = "http://nfp-project.azurewebsites.net/getlist/doctor";
    private static String TAG = ViewDoctorActivity.class.getSimpleName();
    // temporary string to show the parsed response
    private String jsonResponse;
    // Progress dialog
    private ProgressDialog pDialog;
    private String jsonResponeDoctorName, jsonResponseDoctorClinic;
    private String doctorName;
    private String clinicName;
    private Bundle viewDoctorBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        Intent viewDoctorIntent = getIntent();

        viewDoctorBundle = viewDoctorIntent.getExtras();


        /*When creating a sample from the Patient profile page */
        if (viewDoctorBundle != null){
            //String test = viewDoctorBundle.getString("patientName");
            //doctorName = viewDoctorBundle.getString("doctorName");
            clinicName = viewDoctorBundle.getString("clinicName");
//            //SSN = viewDoctorBundle.getString("SSN");
//            TextView nameTVPop = (TextView) findViewById(R.id.patientNameTVPopulated);
//            TextView DOBTVPop = (TextView) findViewById(R.id.DOBTVPopulated);
//            nameTVPop.setText(intentName);
//            DOBTVPop.setText(intentDOB);
//            name = intentName;
//            DOB = intentDOB;

        }

        setProgressBarIndeterminateVisibility(true);
        searchResultListView = (ListView) findViewById(R.id.search_results_list_view);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        // making json array request
        makeJsonArrayRequest();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        else if (id == R.id.action_go_home) {
            goHome();
        }
        return true;
    }

    public void goHome() {
        Intent intent = new Intent(ViewDoctorActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    /**
     * Method to make json array request where response starts with [
     * */
    private void makeJsonArrayRequest() {
        showpDialog();
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            if (mSearchDoctorList!=null && mSearchDoctorList.size() == 0) {
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject doctor = (JSONObject) response
                                            .get(i);
                                    jsonResponeDoctorName = doctor.getString("Name");
                                    jsonResponseDoctorClinic = doctor.getString("Clinic");


                                    DoctorListItem doctorListItem = new DoctorListItem();
                                    doctorListItem.setmIconId(R.mipmap.ic_accept_circle);
                                    doctorListItem.setmDoctorName(jsonResponeDoctorName);
                                    doctorListItem.setmDoctorClinic(jsonResponseDoctorClinic);
                                    mSearchDoctorList.add(doctorListItem);
                                }
                                populateListView();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        ApplicationController.getInstance().addToRequestQueue(req);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void populateListView() {
        if (mSearchDoctorList.size() > 0) {
            ArrayAdapter<DoctorListItem> adapter = new DoctorRowAdapter(getBaseContext(), R.layout.row_doctor, mSearchDoctorList);
            searchResultListView.setAdapter(adapter);
            searchResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ViewDoctorActivity.this, ViewPatientActivity.class);
                    Integer doctorIndex = position;

                    doctorName = mSearchDoctorList.get(position).getmDoctorName();
                    
                    viewDoctorBundle.putString("doctorName", doctorName);

                    intent.putExtras(viewDoctorBundle);


                    startActivity(intent);
                }
            });
        }
    }
}