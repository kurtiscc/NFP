package it446.nfp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;

/**
 * Created by ChristensenKC on 11/5/2015.
 */
public class CreateNewSample extends AppCompatActivity{
    private TextView patientNameTV;
    private TextView DOBTV;
    private TextView patientNameTVPopulated;
    private TextView DOBTVPopulated;
    protected Spinner selectDoctor;
    protected Spinner selectNurse;
    protected Spinner selectDestination;
    protected Button proceedToTagAssociation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_sample);

        patientNameTV = (TextView) findViewById(R.id.patientNameTV);
        DOBTV = (TextView) findViewById(R.id.DOBTV);
        patientNameTVPopulated = (TextView) findViewById(R.id.patientNameTVPopulated);
        DOBTVPopulated = (TextView) findViewById(R.id.DOBTVPopulated);
        proceedToTagAssociation = (Button) findViewById(R.id.proceed_to_tag_association);

        proceedToTagAssociation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewSample.this, TagAssociation.class);
                startActivity(intent);
            }
        });

    }


}
