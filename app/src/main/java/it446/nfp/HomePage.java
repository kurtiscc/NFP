package it446.nfp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by ChristensenKC on 11/5/2015.
 */
public class HomePage extends AppCompatActivity {

    private Button createNewSampleBtn;
    private Button searchBrowsePatientBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

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
                Intent intent = new Intent(HomePage.this, SearchBrowsePatient.class);
                startActivity(intent);
            }
        });

    }
}
