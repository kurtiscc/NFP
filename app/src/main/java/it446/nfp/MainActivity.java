package it446.nfp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.graphics.Color;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static String addressString = "No address found";
    private static String currentTimeStamp;
    public static Double lat;
    public static Double lng;
    TextView testTimeStamp;
    TextView testLat;
    TextView testLng;
    TextView testUID;
    Button getInfo;
    Button scanTag;
    Button sendData;
    private boolean isToScan = false;
    PendingIntent pendingIntent;
    NfcAdapter nfcAdapter;
    IntentFilter[] intentFiltersArray;
    Intent myIntent;


    TextView testAddressString;


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

    /**
     * @return yyyy-MM-dd HH:mm:ss formate date as string
     */
    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
            currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testTimeStamp = (TextView) findViewById(R.id.testTimeStamp);
        testLat = (TextView) findViewById(R.id.testLat);
        testLng = (TextView) findViewById(R.id.testLng);
        testAddressString = (TextView) findViewById(R.id.testAddressString);
        getInfo = (Button) findViewById(R.id.getinfo);
        sendData = (Button) findViewById(R.id.sendData);


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

        if (Build.VERSION.SDK_INT == 23) {

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

        getInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testTimeStamp.setText(getCurrentTimeStamp());
                testLat.setText(String.valueOf(lat));
                testLng.setText(String.valueOf(lng));
                testAddressString.setText(addressString);

            }
        });

        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });



        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        final Button scanTagButton = (Button) findViewById(R.id.scanTag);
        scanTagButton.setBackgroundColor(0xff888888);
        scanTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isToScan = true;
                scanTag = (Button)findViewById(R.id.scanTag);
                scanTag.setText("Scanning");
                scanTag.setBackgroundColor(0xff00ff00);

            }
        });


    }

    public void get_nfc(View v){
        isToScan = true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendData() {

        if(myIntent != null) {
            String timestamp = currentTimeStamp;
            String latitude = lat.toString();
            String longitude = lng.toString();
            String address = addressString;
            String NFCTag = ByteArrayToHexString(myIntent.getByteArrayExtra(NfcAdapter.EXTRA_ID));

            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put("timestamp", timestamp);
                jsonBody.put("latitude", latitude);
                jsonBody.put("longitude", longitude);
                jsonBody.put("address", address);
                jsonBody.put("NFCTag",NFCTag);


            } catch (JSONException e) {

            }
            String url = "http://requestb.in/whbnuuwh";

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(nfcAdapter != null) {

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
        if (isToScan && intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            isToScan = false;
            scanTag = (Button) findViewById(R.id.scanTag);
            scanTag.setText("Scan Tag");
            scanTag.setBackgroundColor(0xff888888);

            if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
                ((TextView) findViewById(R.id.testUID)).setText(
                        "UID = " +
                                ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
            }
        }
    }

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }


}
