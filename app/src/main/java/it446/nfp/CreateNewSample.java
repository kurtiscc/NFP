package it446.nfp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
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
                Bundle bundle = new Bundle();
                bundle.putString("patientName", patientNameTVPopulated.getText().toString());
                bundle.putString("patientDOB", DOBTVPopulated.getText().toString());
                startActivity(intent);
            }
        });

        selectDoctor = (Spinner) findViewById(R.id.select_doctor);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.doctor_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDoctor.setAdapter(adapter);

        selectNurse = (Spinner) findViewById(R.id.select_nurse);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.nurse_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectNurse.setAdapter(adapter2);


        selectDestination = (Spinner) findViewById(R.id.select_destination);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.destination_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDestination.setAdapter(adapter3);

        proceedToTagAssociation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewSample.this, TagAssociation.class);
                Bundle bundle = new Bundle();
                bundle.putString("patientName", patientNameTVPopulated.getText().toString());
                bundle.putString("patientDOB", DOBTVPopulated.getText().toString());
                bundle.putString("doctor", selectDoctor.getSelectedItem().toString());
                bundle.putString("nurse", selectNurse.getSelectedItem().toString());
                bundle.putString("clinic", selectDestination.getSelectedItem().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


}
