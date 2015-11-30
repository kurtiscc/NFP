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

import it446.nfp.Adapters.TagRowAdapter;

/**
 * Created by ChristensenKC on 11/29/2015.
 */
public class ViewTagActivity extends AppCompatActivity {
    private List<TagListItem> mSearchTagList = new ArrayList<TagListItem>();
    private ListView searchResultListView;

    // json array response url
    private String urlJsonArry = "http://nfp.capstone.it.et.byu.edu/getlist/tag";
    private static String TAG = ViewTagActivity.class.getSimpleName();
    // temporary string to show the parsed response
    private String jsonResponse;
    // Progress dialog
    private ProgressDialog pDialog;
    //private String jsonResponePatientName, jsonResponsePatientSSN, jsonResponsePatientDOB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        setProgressBarIndeterminateVisibility(true);
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
                            if (mSearchTagList!=null && mSearchTagList.size() == 0) {
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject tag = (JSONObject) response
                                            .get(i);
                                    //jsonResponePatientName = tag.getString("Name");
                                    //jsonResponsePatientSSN = tag.getString("ssn");
                                    //jsonResponsePatientDOB = tag.getString("DoB");

                                    TagListItem tagListItem = new TagListItem();
                                    //tagListItem.setmIconId(R.mipmap.ic_accept_circle);
                                    //tagListItem.setmUserName(jsonResponePatientName);
                                    //tagListItem.setmDOB(jsonResponsePatientDOB);
                                    mSearchTagList.add(tagListItem);
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
        if (mSearchTagList.size() > 0) {
            //ArrayAdapter<TagListItem> adapter = new TagRowAdapter(getBaseContext(), R.layout.row_tag, mSearchTagList);
            //searchResultListView.setAdapter(adapter);

        }
    }

}

