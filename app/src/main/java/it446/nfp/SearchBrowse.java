package it446.nfp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it446.nfp.Adapters.ClinicRowAdapter;

/**
 * Created by ChristensenKC on 11/5/2015.
 */
public class SearchBrowse extends AppCompatActivity{
    private List<ClinicListItem> mSearchClinicList = new ArrayList<ClinicListItem>();
    private ListView searchResultListView;

    // json array response url
    private String urlJsonArry = "http://nfp.capstone.it.et.byu.edu/getlist/clinic";
    private static String TAG = SearchBrowse.class.getSimpleName();
    // temporary string to show the parsed response
    private String jsonResponse;
    // Progress dialog
    private ProgressDialog pDialog;
    private String jsonResponeClinicName, jsonResponseClinicAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_search_browse);

        setProgressBarIndeterminateVisibility(true);

        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) findViewById(R.id.search_friends);
        searchResultListView = (ListView) findViewById(R.id.search_results_list_view);



        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

                // making json array request
                makeJsonArrayRequest();

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
                            if (mSearchClinicList!=null && mSearchClinicList.size() == 0) {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject clinic = (JSONObject) response
                                        .get(i);
                                jsonResponeClinicName = clinic.getString("Name");
                                jsonResponseClinicAddress = clinic.getString("Address");

                                    ClinicListItem clinicListItem = new ClinicListItem();
                                    clinicListItem.setmIconId(R.mipmap.ic_accept_circle);
                                    clinicListItem.setmClinicName(jsonResponeClinicName);
                                    clinicListItem.setmClinicAddress(jsonResponseClinicAddress);
                                    mSearchClinicList.add(clinicListItem);
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
        if (mSearchClinicList.size() > 0) {
            ArrayAdapter<ClinicListItem> adapter = new ClinicRowAdapter(getBaseContext(), R.layout.row_clinic, mSearchClinicList);
            searchResultListView.setAdapter(adapter);
            searchResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(SearchBrowse.this, ViewDoctorActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
