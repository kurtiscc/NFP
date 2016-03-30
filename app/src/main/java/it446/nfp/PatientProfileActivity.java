package it446.nfp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ChristensenKC on 11/29/2015.
 */
public class PatientProfileActivity extends AppCompatActivity {
    private Button createNewSampleBtn;
    private TextView patientName;
    private TextView patientDOB;
    private TextView patientSSN;
    private String name;
    private String DOB;
    private String SSN;
    private Bundle newSampleBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        patientName = (TextView) findViewById(R.id.patient_name);
        patientDOB = (TextView) findViewById(R.id.patient_dob);
        patientSSN = (TextView) findViewById(R.id.patient_ssn);


        Intent newSampleIntent = getIntent();
        newSampleBundle = newSampleIntent.getExtras();
        name = newSampleBundle.getString("patientName");
        DOB = newSampleBundle.getString("patientDOB");
        SSN = newSampleBundle.getString("SSN");

        patientName.setText(name);
        patientDOB.setText(DOB);
        patientSSN.setText(SSN);

        createNewSampleBtn = (Button) findViewById(R.id.create_new_sample_btn);

        createNewSampleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientProfileActivity.this, TagAssociation.class);

//                Bundle bundle = new Bundle();
//                bundle.putString("patientName",name);
//                bundle.putString("patientDOB", DOB);
//                bundle.putString("SSN", SSN);
                intent.putExtras(newSampleBundle);

                startActivity(intent);
            }
        });
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
        Intent intent = new Intent(PatientProfileActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
