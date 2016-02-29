package it446.nfp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ChristensenKC on 11/5/2015.
 */
public class HomePage extends AppCompatActivity {

    private Button createNewSampleBtn;
    private Button searchBrowsePatientBtn;
    private Button readExistingTagBtn;
    private Button trackSampleBtn;
    private Button testLogin;
    private Button testLogout;
    private String user_id;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //  Bundle b = getIntent().getExtras();
//        user_id = b.getString("user_id");

        createNewSampleBtn = (Button) findViewById(R.id.createNewSampleBtn);
        createNewSampleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, CreateNewSample.class);
                startActivity(intent);
            }
        });

        searchBrowsePatientBtn = (Button) findViewById(R.id.searchBrowsePatientBtn);
        searchBrowsePatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, SearchBrowse.class);
                startActivity(intent);
            }
        });

        readExistingTagBtn = (Button) findViewById(R.id.readExistingTagBtn);
        readExistingTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, ReadExistingTag.class);
                startActivity(intent);
            }
        });

        trackSampleBtn = (Button) findViewById(R.id.trackSampleBtn);
        trackSampleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, TrackSampleActiviy.class);
                startActivity(intent);
            }
        });

        testLogin = (Button) findViewById(R.id.test_login);
        testLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        trackSampleBtn = (Button) findViewById(R.id.trackSampleBtn);
        trackSampleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, TrackSampleActiviy.class);
                startActivity(intent);
            }
        });

//        testLogout = (Button) findViewById(R.id.test_logout);
//        testLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                JSONObject jsonBody = new JSONObject();
//
//                try {
//                    jsonBody.put("userid", user_id);
//
//                } catch (JSONException e) {
//
//                }
//                String url = "http://nfp.capstone.it.et.byu.edu/logout";
//
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
//                        url, jsonBody,
//                        new Response.Listener<JSONObject>() {
//
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                Log.d("Response", response.toString());
//                            }
//                        }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("Response: ", error.toString());
//                    }
//                });
//
//                if (ApplicationController.getInstance() == null) {
//                    Log.d("TEST", "here");
//                } else {
//                    ApplicationController.getInstance().addToRequestQueue(jsonObjectRequest);
//                }
//                Toast.makeText(HomePage.this, "User has been logged out...", Toast.LENGTH_SHORT)
//                        .show();
//            }
//
//        });



        if (Build.VERSION.SDK_INT >= 23) {

            int hasAccessFineLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasAccessFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(HomePage.this, "ACCESS_FINE_LOCATION Allowed", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(HomePage.this, "ACCESS_FINE_LOCATION Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
