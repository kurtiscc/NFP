package it446.nfp;

import android.Manifest;
import android.app.Activity;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by ChristensenKC on 3/29/2016.
 */
public class TrackSampleFragment extends Fragment {

    private Context context;
    private TextView valueUID;
    private TextView valueLastScanned;
    private TextView valueLastLocation;
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

    private MainActivity mainActFunction;

    public TrackSampleFragment() {
        // Required empty public constructor
    }

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
            Geocoder gc = new Geocoder(context, Locale.getDefault());

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_track_sample, container, false);
        context = rootView.getContext();

        nfcAdapter = NfcAdapter.getDefaultAdapter(context);

        valueTime = (TextView) rootView.findViewById(R.id.valueTime);
        valueLatitude = (TextView) rootView.findViewById(R.id.valueLatitude);
        valueLongitude = (TextView) rootView.findViewById(R.id.valueLongitude);
        scanTag = (Button) rootView.findViewById(R.id.scanTag);

        final Button scanTagButton = (Button) rootView.findViewById(R.id.scanTag);
        scanTagButton.setBackgroundColor(0xff888888);
        scanTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isToScan = true;
                scanTag.setText("Scanning");
                scanTag.setBackgroundColor(0xff00ff00);
                getCurrentTimeStamp();
                valueTime.setText(curTimeDisplay);
                valueLatitude.setText(String.valueOf(lat));
                valueLongitude.setText(String.valueOf(lng));

            }
        });



        //valueAddress = (TextView) findViewById(R.id.valueAddress);
        submitTag = (Button) rootView.findViewById(R.id.submitTag);


        LocationManager locationManager;
        String svcName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) context.getSystemService(svcName);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);

        if (Build.VERSION.SDK_INT >= 23) {

            if (locationManager != null) {
                if (checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

        submitTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });


        return rootView;

    }

    public void goHome() {
        Intent intent = new Intent(getActivity().getApplication(), HomeActivity.class);
        startActivity(intent);
    }

    public void sendData() {

        if(myIntent != null) {
            String timestamp = currentTimeStamp;
            String latitude = lat.toString();
            String longitude = lng.toString();
            String address = addressString;
            //String NFCTag = MainActivity.ByteArrayToHexString(myIntent.getByteArrayExtra(NfcAdapter.EXTRA_ID));

            JSONObject jsonBody = new JSONObject();

            try {
                //jsonBody.put ("patientid", patientID);
                jsonBody.put("lat", latitude);
                jsonBody.put("lon", longitude);
                jsonBody.put("addr", address);
                //jsonBody.put("tagUID",tagUID);
                jsonBody.put("purpose", "Sample Creation");
                jsonBody.put("timestamp", timestamp);
                //jsonBody.put("doc",doctor);
//                jsonBody.put("userid", nurse);
                //jsonBody.put("clinic", clinic);


            } catch (JSONException e) {

            }
            String url = "http://nfp-project.azurewebsites.net/addtrackingstep/"+tagUID;

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
        Toast.makeText(getActivity().getApplication(), "Sample Tracked", Toast.LENGTH_SHORT)
                .show();
    }


    public void get_nfc(View v) {
        isToScan = true;
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (nfcAdapter != null) {
//
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(context, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//            // creating intent receiver for NFC events:
//            IntentFilter filter = new IntentFilter();
//            filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
//            filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
//            filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
//            // enabling foreground dispatch for getting intent from NFC event:
//            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
//            nfcAdapter.enableForegroundDispatch((Activity) context, pendingIntent, new IntentFilter[]{filter}, this.techList);
//        }
//    }
//
//    @Override
//    public void onPause(Context context) {
//
//        if (nfcAdapter != null) {
//            nfcAdapter.disableForegroundDispatch(this);
//        }
//
//        super.onPause();
//    }
//
//    @Override
//    public void onNewIntent(Intent intent) {
//        myIntent = intent;
//        valueUID = (TextView) rootView.findViewById(R.id.valueUID);
//
//        if (isToScan && intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
//            isToScan = false;
//            scanTag = (Button) rootView.findViewById(R.id.scanTag);
//            scanTag.setText("Scan Tag");
//            scanTag.setBackgroundColor(0xff888888);
//
//            if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
//                tagUID = MainActivity.ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
//                valueUID.setText(tagUID);
//
//            }
//        }
//    }


}
