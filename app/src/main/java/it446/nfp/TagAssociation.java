package it446.nfp;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Philip on 11/11/2015.
 */


public class TagAssociation extends AppCompatActivity {

    private TextView valueUID;
    private TextView valueName;
    private TextView valueDOB;
    private TextView valueClinic;
    private TextView valueDoctor;
    private TextView valueLastScanned;
    private TextView valueLastLocation;

    private String doctor;
    private String nurse;
    public String clinic;
    protected String name;
    private String DOB;
    private String tagUID;

    private static String addressString = "No address found";
    private static String currentTimeStamp;
    private static String curTimeDisplay;
    public static Double lat;
    public static Double lng;
    TextView valueTime;
    TextView valueLatitude;
    TextView valueLongitude;
    //TextView valueUID;
    Button submitTag;

    Button scanTag;
    private boolean isToScan = false;
    PendingIntent pendingIntent;
    NfcAdapter nfcAdapter;
    IntentFilter[] intentFiltersArray;
    Intent myIntent;

 //   final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private MainActivity mainActFunction;

    // list of NFC technologies detected:
    private final String[][] techList = new String[][]{
            new String[]{
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    private void updateWithNewLocation(Location location) {

        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Geocoder gc = new Geocoder(this, Locale.getDefault());

            try {
                List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);

                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                        sb.append(address.getAddressLine(i)).append("\n");
                }
                addressString = sb.toString();
            } catch (IOException e) {
            }
        }
    }

    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
            SimpleDateFormat dateFormatDisp = new SimpleDateFormat("HH:mm:ss   dd-MM-yyyy");
            currentTimeStamp = dateFormat.format(new Date()); // Find todays date
            curTimeDisplay = dateFormatDisp.format(new Date());
            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_association);

        Intent newSampleIntent = getIntent();
        Bundle newSampleBundle = newSampleIntent.getExtras();
        name = newSampleBundle.getString("patientName");
        DOB = newSampleBundle.getString("patientDOB");
        doctor = newSampleBundle.getString("doctor");
        nurse = newSampleBundle.getString("nurse");
        clinic = newSampleBundle.getString("clinic");
        valueName = (TextView) findViewById(R.id.valueName);
        valueName.setText(name);






        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        valueTime = (TextView) findViewById(R.id.valueTime);
        valueLatitude = (TextView) findViewById(R.id.valueLatitude);
        valueLongitude = (TextView) findViewById(R.id.valueLongitude);

        final Button scanTagButton = (Button) findViewById(R.id.scanTag);
        scanTagButton.setBackgroundColor(0xff888888);
        scanTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isToScan = true;
                scanTag = (Button) findViewById(R.id.scanTag);
                scanTag.setText("Scanning");
                scanTag.setBackgroundColor(0xff00ff00);
                getCurrentTimeStamp();
                valueTime.setText(curTimeDisplay);
                valueLatitude.setText(String.valueOf(lat));
                valueLongitude.setText(String.valueOf(lng));

            }
        });



        //valueAddress = (TextView) findViewById(R.id.valueAddress);
        submitTag = (Button) findViewById(R.id.submitTag);


        LocationManager locationManager;
        String svcName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(svcName);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);





        if (Build.VERSION.SDK_INT >= 23) {

//            int hasAccessFineLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
//            if (hasAccessFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        REQUEST_CODE_ASK_PERMISSIONS);
//            }

            if (locationManager != null) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    String provider = locationManager.getBestProvider(criteria, true);

                    Location l = locationManager.getLastKnownLocation(provider);

                    updateWithNewLocation(l);


                    locationManager.requestLocationUpdates(provider, 2000, 10,
                            locationListener);

                }
            }

        } else {
            String provider = locationManager.getBestProvider(criteria, true);

            Location l = locationManager.getLastKnownLocation(provider);

            updateWithNewLocation(l);

            locationManager.requestLocationUpdates(provider, 2000, 10,
                    locationListener);
        }



       // valueAddress.setText(addressString);

        submitTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });



    }

    public void sendData() {

        if(myIntent != null) {
            String timestamp = currentTimeStamp;
            String latitude = lat.toString();
            String longitude = lng.toString();
            String address = addressString;
           //tring NFCTag = MainActivity.ByteArrayToHexString(myIntent.getByteArrayExtra(NfcAdapter.EXTRA_ID));

            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put ("patientID", name);
                jsonBody.put("timestamp", timestamp);
                jsonBody.put("lat", latitude);
                jsonBody.put("lon", longitude);
                jsonBody.put("addr", address);
                jsonBody.put("tagUID",tagUID);
                jsonBody.put("doc",doctor);
                jsonBody.put("user", nurse);
                jsonBody.put("clinic", clinic);


            } catch (JSONException e) {

            }
            String url = "http://nfp.capstone.it.et.byu.edu/addtag";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    url, jsonBody,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            if (ApplicationController.getInstance() == null) {
                Log.d("TEST", "here");
            }
            else {
                ApplicationController.getInstance().addToRequestQueue(jsonObjectRequest);
            }
        }
        Toast.makeText(TagAssociation.this, "Sample Submitted", Toast.LENGTH_SHORT)
                .show();
    }


    public void get_nfc(View v) {
        isToScan = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            // creating intent receiver for NFC events:
            IntentFilter filter = new IntentFilter();
            filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
            filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
            filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
            // enabling foreground dispatch for getting intent from NFC event:
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
        }
    }

    @Override
    protected void onPause() {

        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }

        super.onPause();
    }

    @Override

    public void onNewIntent(Intent intent) {
        myIntent = intent;
        valueUID = (TextView) findViewById(R.id.valueUID);
        valueName = (TextView) findViewById(R.id.valueName);
        valueDOB = (TextView) findViewById(R.id.valueDOB);
        valueClinic = (TextView) findViewById(R.id.valueClinic);
        valueDoctor = (TextView) findViewById(R.id.valueDoctor);
        valueLastScanned = (TextView) findViewById(R.id.valueLastScanned);
        valueLastLocation = (TextView) findViewById(R.id.valueLastLocation);

        if (isToScan && intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            isToScan = false;
            scanTag = (Button) findViewById(R.id.scanTag);
            scanTag.setText("Scan Tag");
            scanTag.setBackgroundColor(0xff888888);

            if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
                tagUID = MainActivity.ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
                valueUID.setText(tagUID);



//                valueName.setText("John Doe");
//                valueDOB.setText("1/1/1976");
//                valueClinic.setText("Mayo");
//                valueDoctor.setText("Dr. Doctor");
//                valueLastScanned.setText("10/31/2015 11:11am");
//                valueLastLocation.setText("Mayo Clinic");
            }
        }
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
