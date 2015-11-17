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
import it446.nfp.R;

/**
 * Created by ChristensenKC on 11/16/2015.
 */
public class ClinicRowAdapter extends ArrayAdapter<ClinicListItem> {
    Context mContext;
    private List<ClinicListItem> mClinicList = new ArrayList<>();

    public ClinicRowAdapter(Context context, int resource, List<ClinicListItem> mClinicList) {
        super(context, resource, mClinicList);
        mContext = context;
        this.mClinicList = mClinicList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ClinicRowHolder holder = null;

        if(row == null) {
            row = LayoutInflater.from(mContext).inflate(R.layout.row_clinic, parent, false);

            holder = new ClinicRowHolder();
            holder.clinicNameTV = (TextView) row.findViewById(R.id.clinic_name);
            holder.clinicAddressTV = (TextView) row.findViewById(R.id.clinic_address);
            holder.clinicThumbnail = (ImageView) row.findViewById(R.id.clinic_thumbnail);

            row.setTag(holder);
        }
        else {
            holder = (ClinicRowHolder) row.getTag();
        }

        final ClinicListItem currentItem = mClinicList.get(position);

        holder.clinicNameTV.setText(currentItem.getmClinicName());
        holder.clinicAddressTV.setText(currentItem.getmClinicAddress());

        return row;
    }

    static class ClinicRowHolder {
        TextView clinicNameTV, clinicAddressTV;
        ImageView clinicThumbnail;
    }
}

