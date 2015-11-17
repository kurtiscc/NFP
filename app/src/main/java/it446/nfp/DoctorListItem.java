package it446.nfp;

/**
 * Created by ChristensenKC on 11/16/2015.
 */
public class DoctorListItem {
    private int mIconId;
    private String mDoctorName;

    public DoctorListItem() {
        mIconId = -1;
        mDoctorName = "";
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


}
