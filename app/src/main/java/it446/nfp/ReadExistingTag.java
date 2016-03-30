package it446.nfp;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ParseException;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import javax.security.cert.Certificate;
import javax.security.cert.*;
import java.security.*;
import javax.net.ssl.*;
import javax.net.*;

/**
 * Created by Philip on 11/9/2015.
 */
public class ReadExistingTag extends AppCompatActivity implements OnMapReadyCallback {
    private List<PatientListItem> mSearchPatientList = new ArrayList<PatientListItem>();
    private List<String> patientNameList = new ArrayList<String>();
    private TextView valueUID;
    //private TextView valueName;
    //private TextView valueDOB;
    private TextView valueClinic;
    private TextView valueDoctor;
    private TextView valueLastScanned;
    private TextView valueLastLocation;
    private String jsonResponseSample;
    private String tagUID;
    //private ScrollView scrollViewHistory;
    //private ImageView imageViewMap;
    //private LinearLayout trackingHistory;
    private ProgressDialog pDialog;
    private static String TAG = SearchBrowse.class.getSimpleName();
    private String urlJsonArryPatient = "http://nfp-project.azurewebsites.net/getlist/patient";
    private String urlJsonArraySample = "http://nfp-project.azurewebsites.net/getsample/";
    Button scanTag;
    private boolean isToScan = false;
    PendingIntent pendingIntent;
    NfcAdapter nfcAdapter;
    IntentFilter[] intentFiltersArray;
    Intent myIntent;
    static String jsonResponsePatient;
//    private List<String> patientIdOnlyList = new ArrayList<String>();
    private String lat;
    private String lon;

    private String jsonResponseSample_id;
//    private String jsonResponsePatientid;
    private String jsonResponseUserid;
    private String jsonResponseTagUID;
    private JSONArray jsonArrayTrackingSteps;
    private JSONObject jsonObjectTrackStep;
    private String jsonResponsePatientName, jsonResponsePatientDoB, jsonResponsePatient_id, jsonResponsePatientssn, jsonResponsePatientDoctorid;
    private MainActivity mainActFunction;
    private Button mapButton;
    private String stringSteps = "";
    private List<String> trackList = new ArrayList<String>();
    //protected ScrollView trackStepsScroll;
    //protected ListView trackStepsList;
    // list of NFC technologies detected:
    private ListView trackStepsList;
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
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //trackStepsList = (ListView) findViewById(R.id.trackingLV);

        // Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
//        CertificateFactory cf = null;
//        try {
//            cf = CertificateFactory.getInstance("X.509");
//        } catch (java.security.cert.CertificateException e) {
//            e.printStackTrace();
//        }
//// From https://www.washington.edu/itconnect/security/ca/load-der.crt
//        InputStream caInput = new BufferedInputStream(new FileInputStream("load-der.crt"));
//        java.security.cert.Certificate ca;
//        try {
//            ca = cf.generateCertificate(caInput);
//            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                caInput.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//// Create a KeyStore containing our trusted CAs
//        String keyStoreType = KeyStore.getDefaultType();
//        KeyStore keyStore = null;
//        try {
//            keyStore = KeyStore.getInstance(keyStoreType);
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        }
//        keyStore.load(null, null);
//        keyStore.setCertificateEntry("ca", ca);
//
//// Create a TrustManager that trusts the CAs in our KeyStore
//        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//        tmf.init(keyStore);
//
//// Create an SSLContext that uses our TrustManager
//        SSLContext context = SSLContext.getInstance("TLS");
//        context.init(null, tmf.getTrustManagers(), null);
//
//// Tell the URLConnection to use a SocketFactory from our SSLContext
//        URL url = null;
//        try {
//            url = new URL("https://certs.cac.washington.edu/CAtest/");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        HttpsURLConnection urlConnection =
//                (HttpsURLConnection)url.openConnection();
//        urlConnection.setSSLSocketFactory(context.getSocketFactory());
//        InputStream in = null;
//        try {
//            in = urlConnection.getInputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        copyInputStreamToOutputStream(in, System.out);



        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        //makeJsonArrayRequestPatient();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_existing_tag);


        trackStepsList = (ListView) findViewById(R.id.trackingLV);



        //imageViewMap = (ImageView) findViewById(R.id.imageView);
        //trackingHistory = (LinearLayout) findViewById(R.id.trackingHistoryLL);
        //trackingHistory.setVisibility(View.INVISIBLE);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        trackList.add("blach");


        final Button scanTagButton = (Button) findViewById(R.id.scanTag);
//        mapButton = (Button) findViewById(R.id.map);
        scanTagButton.setBackgroundColor(0xff888888);
        scanTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isToScan = true;
                scanTag = (Button) findViewById(R.id.scanTag);
                scanTag.setText("Scanning");
                scanTag.setBackgroundColor(0xff00ff00);


            }
        });

//        mapButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              Intent intent = new Intent(ReadExistingTag.this, MapsActivity.class);
//                startActivity(intent);
//            }
//        });

    }

    private void copyInputStreamToOutputStream(InputStream in, PrintStream out) {
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //for(int i = 0; i < .size(); i++){
        Double lat = 40.226702;
        Double lng = -111.644335;

        // Add a marker in Provo and move the camera
        LatLng provo = new LatLng(40.26021831087763, -111.63983692066112);
//        mMap.addMarker(new MarkerOptions()
//                .position(provo)
//                .title("Marker in Provo")
//                .visible(false)
//        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 11.0f));
        //}
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
        Intent intent = new Intent(ReadExistingTag.this, HomeActivity.class);
        startActivity(intent);
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
       // valueName = (TextView) findViewById(R.id.valueName);
        //valueDOB = (TextView) findViewById(R.id.valueDOB);
        valueClinic = (TextView) findViewById(R.id.valueClinic);
        valueDoctor = (TextView) findViewById(R.id.valueDoctor);
        valueLastScanned = (TextView) findViewById(R.id.valueLastScanned);
        valueLastLocation = (TextView) findViewById(R.id.valueLastLocation);

        ArrayAdapter<String> adapterList = new ArrayAdapter<String>(ReadExistingTag.this, android.R.layout.simple_list_item_1, trackList);


        if (isToScan && intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            isToScan = false;
            scanTag = (Button) findViewById(R.id.scanTag);
            scanTag.setText("Scan Tag");
            scanTag.setBackgroundColor(0xff888888);
            valueLastScanned.setText("");
            valueLastLocation.setText("");
            //valueName.setText("");
            //valueDOB.setText("");
            valueClinic.setText("");
            valueDoctor.setText("");

            if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
                tagUID = MainActivity.ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
                        valueUID.setText("" + tagUID);
                makeJsonArrayRequestSample(tagUID);
                //valueName.setText("John Doe");
                //valueDOB.setText("1/1/1976");
                valueClinic.setText("Family Medical");
                valueDoctor.setText("Dr. Doctor");
                //valueLastScanned.setText("12/4/2015 11:50am");
                //valueLastLocation.setText("Pathology Lab");
                //trackingHistory.setVisibility(View.VISIBLE);


                //Log.d("trackingStep", stringSteps);

            }
        }
    }


//    private void makeJsonArrayRequestPatient() {
//        showpDialog();
//        JsonArrayRequest req = new JsonArrayRequest(urlJsonArryPatient,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d("TAG", response.toString());
//
//                        try {
//                            // Parsing json array response
//                            // loop through each json object
//                            jsonResponsePatient = "";
//                            if (mSearchPatientList!=null && mSearchPatientList.size() == 0) {
//                                for (int i = 0; i < response.length(); i++) {
//
//                                    JSONObject patient = (JSONObject) response.get(i);
//                                    jsonResponsePatientName = patient.getString("Name");
//                                    jsonResponsePatientDoB = patient.getString("DoB");
//                                    jsonResponsePatientssn = patient.getString("ssn");
//                                    jsonResponsePatient_id = patient.getString("_id");
//                                    jsonResponsePatientDoctorid = patient.getString("Doctorid");
//
//                                    String autoCompItem = jsonResponsePatientName + ", " + jsonResponsePatientDoB;
//                                    patientNameList.add(autoCompItem);
//                                    patientIdOnlyList.add(jsonResponsePatient_id);
//                                    // patientNameOnlyList.add(jsonResponsePatientName);
//                                    //patientDOBOnlyList.add(jsonResponsePatientDoB);
//
//
//
//                                    PatientListItem patientListItem = new PatientListItem();
//                                    patientListItem.setmIconId(R.mipmap.ic_accept_circle);
//                                    patientListItem.setmUserName(jsonResponsePatientName);
//                                    patientListItem.setmSSN(jsonResponsePatientDoB);
//                                    patientListItem.setmDOB(jsonResponsePatientssn);
//                                    patientListItem.setmPatientId(jsonResponsePatient_id);
//                                    patientListItem.setmDoctorId(jsonResponsePatientDoctorid);
//                                    mSearchPatientList.add(patientListItem);
//                                }
//                                //populateListView();
//                            }
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(),
//                                    "Error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//
//                        hidepDialog();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hidepDialog();
//            }
//        });
//
//        // Adding request to request queue
//        ApplicationController.getInstance().addToRequestQueue(req);
//    }
    private void makeJsonArrayRequestSample(String tagUID) {
        showpDialog();
        String url = urlJsonArraySample + tagUID;

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            Integer sampleNum = 1;
                            jsonResponseSample = "";
                                //for (int i = 0; i < response.length(); i++) {

                                    JSONObject sample = (JSONObject) response.get(0);
                                    jsonResponseSample_id = sample.getString("_id");
                                    //jsonResponsePatientid = sample.getString("patientid");
                                    jsonResponseUserid = sample.getString("userid");
                                    jsonResponseTagUID = sample.getString("tagUID");
                                    jsonArrayTrackingSteps = sample.getJSONArray("trackingSteps");
                                    //stringSteps = jsonArrayTrackingSteps.toString();
                                    Integer n = 0;
                                    try {

                                        Integer j = jsonArrayTrackingSteps.length()-1;

                                        //jsonObjectTrackStep = jsonArrayTrackingSteps.getJSONObject(j);
//                                        lat = jsonObjectTrackStep.getString("lat");
//                                        //valueLastLocation.setText(jsonObjectTrackStep.getString("addr"));
//                                        lon = jsonObjectTrackStep.getString("lon");
//                                        valueLastScanned.setText(jsonObjectTrackStep.getString("timestamp"));
                                        for (int k = 0; k <= j; k++){
                                            jsonObjectTrackStep = jsonArrayTrackingSteps.getJSONObject(k);
                                            LatLng stepLatLng = new LatLng(Double.parseDouble(jsonObjectTrackStep.getString("lat")),
                                                    (Double.parseDouble(jsonObjectTrackStep.getString("lon"))));
                                            String addr = jsonObjectTrackStep.getString("addr");
                                            String purpose = jsonObjectTrackStep.getString("purpose");
                                            String timeStamp = jsonObjectTrackStep.getString("timestamp");
                                            String userID = jsonObjectTrackStep.getString("userid");
                                            String userName = "Courier TK421";

                                            Date parsed = new Date();
                                            try {
                                                SimpleDateFormat format =
                                                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
                                                parsed = format.parse(timeStamp);
                                            }
                                            catch (java.text.ParseException e) {
                                                // Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                            SimpleDateFormat formatSimple = new SimpleDateFormat("MMM dd h:mma", Locale.US);
                                            String time3 = formatSimple.format(parsed);



                                            valueLastLocation.setText(addr + " " + purpose + " " + time3);
                                            String trackLine = Integer.toString(sampleNum) + ". " + Html.fromHtml("<b>" + purpose + "</b>") + "\n" + addr + time3;


                                            String time2 = parsed.toString();

                                            trackList.add(trackLine);
                                            mMap.addMarker(new MarkerOptions()
                                                    .position(stepLatLng)
                                                    .title(sampleNum+". "+addr)
                                                    .snippet(time3+"\n"+userName)
                                            );
                                            sampleNum++;
                                        }
//                                        trackStepsScroll = (ScrollView) findViewById(R.id.scrollView);
//                                        ArrayAdapter<String> adapterScroll = new ArrayAdapter<String>(ReadExistingTag.this, android.R.layout.simple_selectable_list_item, trackList);
//                                        adapterScroll.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
//                                        trackStepsScroll.setAdapter(adapterScroll);
                                        ArrayAdapter<String> adapterList = new ArrayAdapter<String>(ReadExistingTag.this, android.R.layout.simple_list_item_1, trackList);
                                        trackStepsList.setAdapter(adapterList);

                                        //use parseDouble(string s) on lat lon strings from json. LatLng(Double, Double) is an object. mLatlng.getLatituted(); mlatlng.getLongitude();
                                        // drawMarker(mLatLng); or drawMarker(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
                                    }
                                    catch (JSONException e){
                                        //do stuff?
                                    }

                                    ArrayAdapter<String> adapterList = new ArrayAdapter<String>(ReadExistingTag.this, android.R.layout.simple_list_item_1, trackList);
                                    trackStepsList.setAdapter(adapterList);
//                                    PatientListItem patientListItem = new PatientListItem();
//                                    patientListItem.setmIconId(R.mipmap.ic_accept_circle);
//                                    patientListItem.setmUserName(jsonResponsePatientName);
//                                    patientListItem.setmSSN(jsonResponsePatientDoB);
//                                    patientListItem.setmDOB(jsonResponsePatientssn);
//                                    patientListItem.setmPatientId(jsonResponsePatient_id);
//                                    patientListItem.setmDoctorId(jsonResponsePatientDoctorid);
//                                    mSearchPatientList.add(patientListItem);
                                //}
                                //populateListView();


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

}
