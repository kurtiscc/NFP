package it446.nfp;

/**
 * Created by ChristensenKC on 11/16/2015.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class DoctorListItem implements Parcelable {
    private int mIconId;
    private String mDoctorName;
    private String mDoctorClinic;
    private String mDoctorID;

    public DoctorListItem() {
        mIconId = -1;
        mDoctorName = "";
        mDoctorClinic = "";
        mDoctorID = "";
    }

    public int getmIconId() {
        return mIconId;
    }

    public void setmIconId(int mIconId) {
        this.mIconId = mIconId;
    }

    public String getmDoctorName() {
        return mDoctorName;
    }

    public void setmDoctorName(String mDoctorName) {
        this.mDoctorName = mDoctorName;
    }

    public String getmDoctorClinic() {
        return mDoctorClinic;
    }

    public void setmDoctorClinic(String mDoctorClinic) {
        this.mDoctorClinic = mDoctorClinic;
    }

    public String getmDoctorID() {
        return mDoctorID;
    }

    public void setmDoctorID(String mDoctorID){
        this.mDoctorID = mDoctorID;
    }

    public static final Parcelable.Creator<DoctorListItem> CREATOR = new Creator<DoctorListItem>() {
        public DoctorListItem createFromParcel(Parcel source) {
            DoctorListItem docListItem = new DoctorListItem();
            docListItem.mDoctorName = source.readString();
            docListItem.mDoctorClinic = source.readString();
            docListItem.mDoctorID = source.readString();
            return docListItem;
        }

        public DoctorListItem[] newArray(int size) {
            return new DoctorListItem[size];
        }

    };
    public int describeContents(){
        return 0;
    }
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeString(mDoctorName);
        parcel.writeString(mDoctorClinic);
        parcel.writeString(mDoctorID);
    }
}
