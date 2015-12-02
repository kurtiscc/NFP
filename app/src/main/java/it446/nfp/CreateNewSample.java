package it446.nfp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

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
 * Created by ChristensenKC on 11/5/2015.
 */
public class CreateNewSample extends AppCompatActivity{
    private List<PatientListItem> mSearchPatientList = new ArrayList<PatientListItem>();
    private List<String> doctorNameList = new ArrayList<String>();
    private List<String> patientNameList = new ArrayList<String>();
    private List<String> nurseNameList = new ArrayList<String>();
 //   private TextView patientNameTV;
    private TextView DOBTV;
    private TextView patientNameTVPopulated;
    private TextView DOBTVPopulated;
    protected Spinner selectDoctor;
    protected Spinner selectNurse;
    protected Spinner selectDestination;
    protected Button proceedToTagAssociation;

    AutoCompleteTextView patientNameTV = null;
    private ArrayAdapter<String> adapter;
    String item[];

    private String urlJsonArryPatient = "http://nfp.capstone.it.et.byu.edu/getlist/patient";
    private String urlJsonArryDoctor = "http://nfp.capstone.it.et.byu.edu/getlist/doctor";
    private String urlJsonArryNurse = "http://nfp.capstone.it.et.byu.edu/getlist/nurse";
    //private String urlJsonArryPatient = "http://nfp.capstone.it.et.byu.edu/getlist/patient";
    private static String TAG = SearchBrowse.class.getSimpleName();
    // temporary string to show the parsed response
    private String jsonResponsePatient;
    private String jsonResponseDoctor;
    private String jsonResponseNurse;
    // Progress dialog
    private ProgressDialog pDialog;
    private String jsonResponsePatientName, jsonResponsePatientDOB, jsonResponseDoctorName, jsonResponseNurseName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_sample);

        setProgressBarIndeterminateVisibility(true);

        //android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) findViewById(R.id.search_friends);
        //searchResultListView = (ListView) findViewById(R.id.search_results_list_view);



        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        // making json array request
        nurseNameList.add("Select a Nurse");
        doctorNameList.add("Select a Doctor");
        makeJsonArrayRequestPatient();
        makeJsonArrayRequestDoctor();
        makeJsonArrayRequestNurse();
//        patientNameTV = (AutoCompleteTextView) findViewById(R.id.patientNameTV);
//
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item);
//        patientNameTV.setThreshold(1);
//
//        patientNameTV.setAdapter(adapter);
//        patientNameTV.setOnItemSelectedListener();
//        patientNameTV.setOnItemClickListener(this);

        ArrayAdapter<String> adapterName = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, patientNameList);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.patientNameTVPopulated);
        textView.setAdapter(adapterName);



//        autocompleteTextView.setAdapter = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_dropdown_item_1line, stringArray);
//        myAutocompleteTextView.setAdapter(autocompletetextAdapter);


        DOBTV = (TextView) findViewById(R.id.DOBTV);
        patientNameTVPopulated = (TextView) findViewById(R.id.patientNameTVPopulated);
        DOBTVPopulated = (TextView) findViewById(R.id.DOBTVPopulated);
        proceedToTagAssociation = (Button) findViewById(R.id.proceed_to_tag_association);

        proceedToTagAssociation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewSample.this, TagAssociation.class);
                Bundle bundle = new Bundle();
                bundle.putString("patientName", patientNameTVPopulated.getText().toString());
                bundle.putString("patientDOB", DOBTVPopulated.getText().toString());
                startActivity(intent);
            }
        });

        selectDoctor = (Spinner) findViewById(R.id.select_doctor);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, doctorNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDoctor.setAdapter(adapter);


        selectNurse = (Spinner) findViewById(R.id.select_nurse);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, nurseNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectNurse.setAdapter(adapter2);


        selectDestination = (Spinner) findViewById(R.id.select_destination);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.destination_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDestination.setAdapter(adapter3);

        proceedToTagAssociation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewSample.this, TagAssociation.class);
                Bundle bundle = new Bundle();
                bundle.putString("patientName", patientNameTVPopulated.getText().toString());
                bundle.putString("patientDOB", DOBTVPopulated.getText().toString());
                bundle.putString("doctor", selectDoctor.getSelectedItem().toString());
                bundle.putString("nurse", selectNurse.getSelectedItem().toString());
                bundle.putString("clinic", selectDestination.getSelectedItem().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    /**
     * Method to make json array request where response starts with [
     * */
    private void makeJsonArrayRequestPatient() {
        showpDialog();
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArryPatient,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponseDoctor = "";
                            if (mSearchPatientList!=null && mSearchPatientList.size() == 0) {
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject patient = (JSONObject) response
                                            .get(i);
                                    jsonResponsePatientName = patient.getString("Name");
                                    jsonResponsePatientDOB = patient.getString("DoB");
                                    String autoCompItem = jsonResponsePatientName + ", " + jsonResponsePatientDOB;
                                    patientNameList.add(autoCompItem);



                                    PatientListItem patientListItem = new PatientListItem();
                                    patientListItem.setmIconId(R.mipmap.ic_accept_circle);
                                    patientListItem.setmUserName(jsonResponsePatientName);
                                    patientListItem.setmDOB(jsonResponsePatientDOB);
                                    mSearchPatientList.add(patientListItem);
                                }
                                //populateListView();
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

    private void makeJsonArrayRequestDoctor() {
        showpDialog();
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArryDoctor,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponseDoctor = "";
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject patient = (JSONObject) response
                                            .get(i);
                                    jsonResponseDoctorName = patient.getString("Name");
//                                    jsonResponsePatientDOB = patient.getString("DOB");
                                    doctorNameList.add(jsonResponseDoctorName);
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
    private void makeJsonArrayRequestNurse() {
        showpDialog();
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArryNurse,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponseNurse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject nurse = (JSONObject) response
                                        .get(i);
                                jsonResponseNurseName = nurse.getString("Name");
                                nurseNameList.add(jsonResponseNurseName);
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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
