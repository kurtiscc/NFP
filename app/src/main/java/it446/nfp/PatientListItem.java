package it446.nfp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ChristensenKC on 11/9/2015.
 */
public class PatientListItem implements Parcelable {
    private int mIconId;
    private String mUserName;
    private String mSSN;
    private String mDOB;
    private String mPatientId;
    private String mDoctorId;

    public PatientListItem() {
        mIconId = -1;
        mUserName = "";
        mDOB = "";
        mSSN = "";
        mPatientId = "";
        mDoctorId = "";
    }

    public int getmIconId() {
        return mIconId;
    }

    public void setmIconId(int mIconId) {
        this.mIconId = mIconId;
    }

    public String getmUserName() {

        return mUserName;
    }

    public void setmUserName(String mUserName) {

        this.mUserName = mUserName;
    }

    public String getmDOB() {

        return mDOB;
    }

    public void setmDOB(String mDOB) {

        this.mDOB = mDOB;
    }

    public String getmSSN() {

        return mSSN;
    }

    public void setmSSN(String mSSN) {

        this.mSSN = mSSN;
    }

    public String getmPatientId() {
        return mPatientId;
    }

    public void setmPatientId(String mPatientId) {
        this.mPatientId = mPatientId;
    }

    public String getmDoctorId() {
        return mDoctorId;
    }

    public void setmDoctorId(String mDoctorId) {
        this.mDoctorId = mDoctorId;
    }

    public static final Parcelable.Creator<PatientListItem> CREATOR = new Creator<PatientListItem>() {
        public PatientListItem createFromParcel(Parcel source) {
            PatientListItem patientListItem = new PatientListItem();
            patientListItem.mIconId = source.readInt();
            patientListItem.mUserName = source.readString();
            patientListItem.mSSN = source.readString();
            patientListItem.mDOB = source.readString();
            patientListItem.mPatientId= source.readString();
            patientListItem.mDoctorId = source.readString();
            return patientListItem;
        }

        public PatientListItem[] newArray(int size) {
            return new PatientListItem[size];
        }

    };
    public int describeContents(){
        return 0;
    }
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeInt(mIconId);
        parcel.writeString(mUserName);
        parcel.writeString(mSSN);
        parcel.writeString(mDOB);
        parcel.writeString(mPatientId);
        parcel.writeString(mDoctorId);
    }

}
