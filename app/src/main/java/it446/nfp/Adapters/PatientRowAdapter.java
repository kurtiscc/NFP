package it446.nfp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it446.nfp.PatientListItem;
import it446.nfp.R;


/**
 * Created by ChristensenKC on 11/9/2015.
 */
public class PatientRowAdapter extends ArrayAdapter<PatientListItem> {
    Context mContext;
    private List<PatientListItem> mPatientList = new ArrayList<>();

    public PatientRowAdapter(Context context, int resource, List<PatientListItem> mPatientList) {
        super(context, resource, mPatientList);
        mContext = context;
        this.mPatientList = mPatientList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PatientRowHolder holder = null;

        if(row == null) {
            row = LayoutInflater.from(mContext).inflate(R.layout.row_patient, parent, false);

            holder = new PatientRowHolder();
            holder.patientNameTV = (TextView) row.findViewById(R.id.patient_name);
            holder.patientDOBTV = (TextView) row.findViewById(R.id.patient_dob);
            holder.patientThumbnail = (ImageView) row.findViewById(R.id.patient_thumbnail);

            row.setTag(holder);
        }
        else {
            holder = (PatientRowHolder) row.getTag();
        }

        final PatientListItem currentItem = mPatientList.get(position);

        holder.patientNameTV.setText(currentItem.getmUserName());
        holder.patientDOBTV.setText(currentItem.getmDOB());

        return row;
    }

    static class PatientRowHolder {
        TextView patientNameTV, patientDOBTV;
        ImageView patientThumbnail;
    }
}
