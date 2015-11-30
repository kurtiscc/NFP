package it446.nfp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it446.nfp.ClinicListItem;
import it446.nfp.DoctorListItem;
import it446.nfp.R;

/**
 * Created by ChristensenKC on 11/16/2015.
 */
public class DoctorRowAdapter extends ArrayAdapter<DoctorListItem> {
    Context mContext;
    private List<DoctorListItem> mDoctorList = new ArrayList<>();

    public DoctorRowAdapter(Context context, int resource, List<DoctorListItem> mDoctorList) {
        super(context, resource, mDoctorList);
        mContext = context;
        this.mDoctorList = mDoctorList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DoctorRowHolder holder = null;

        if(row == null) {
            row = LayoutInflater.from(mContext).inflate(R.layout.row_doctor, parent, false);

            holder = new DoctorRowHolder();
            holder.doctorNameTV = (TextView) row.findViewById(R.id.doctor_name);
            holder.doctorClinicTV = (TextView) row.findViewById(R.id.doctor_clinic);
            holder.doctorThumbnail = (ImageView) row.findViewById(R.id.doctor_thumbnail);

            row.setTag(holder);
        }
        else {
            holder = (DoctorRowHolder) row.getTag();
        }

        final DoctorListItem currentItem = mDoctorList.get(position);

        holder.doctorNameTV.setText(currentItem.getmDoctorName());
        holder.doctorClinicTV.setText(currentItem.getmDoctorClinic());

        return row;
    }

    static class DoctorRowHolder {
        TextView doctorNameTV, doctorClinicTV;
        ImageView doctorThumbnail;
    }
}

