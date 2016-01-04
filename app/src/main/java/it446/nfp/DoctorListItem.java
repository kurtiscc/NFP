package it446.nfp;

/**
 * Created by ChristensenKC on 11/16/2015.
 */
public class DoctorListItem {
    private int mIconId;
    private String mDoctorName;
    private String mDoctorClinic;

    public DoctorListItem() {
        mIconId = -1;
        mDoctorName = "";
        mDoctorClinic = "";
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
}
