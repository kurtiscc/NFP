package it446.nfp;

import android.app.PendingIntent;
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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Philip on 11/9/2015.
 */
public class ReadExistingTag extends AppCompatActivity {
    private TextView valueUID;
    private TextView valueName;
    private TextView valueDOB;
    private TextView valueClinic;
    private TextView valueDoctor;
    private TextView valueLastScanned;
    private TextView valueLastLocation;


    Button scanTag;
    private boolean isToScan = false;
    PendingIntent pendingIntent;
    NfcAdapter nfcAdapter;
    IntentFilter[] intentFiltersArray;
    Intent myIntent;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_existing_tag);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        final Button scanTagButton = (Button) findViewById(R.id.scanTag);
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
                valueUID.setText("" + MainActivity.ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
                valueName.setText("John Doe");
                valueDOB.setText("1/1/1976");
                valueClinic.setText("Mayo");
                valueDoctor.setText("Dr. Doctor");
                valueLastScanned.setText("10/31/2015 11:11am");
                valueLastLocation.setText("Mayo Clinic");
            }
        }
    }

    private void getTagInfo(String UID) {
        //get json from server. Array of maps? arrayByUID[0] = name:John Dow?


    }

}
