package it446.nfp;

/**
 * Created by ChristensenKC on 11/16/2015.
 */
public class ClinicListItem {
    private String mClinicName;
    private String mClinicAddress;
    private int mIconId;

    public ClinicListItem() {
        mIconId = -1;
        mClinicName = "";
        mClinicAddress = "";
    }

    public String getmClinicName() {
        return mClinicName;
    }

    public void setmClinicName(String mClinicName) {
        this.mClinicName = mClinicName;
    }

    public String getmClinicAddress() {
        return mClinicAddress;
    }

    public void setmClinicAddress(String mClinicAddress) {
        this.mClinicAddress = mClinicAddress;
    }

    public int getmIconId() {
        return mIconId;
    }

    public void setmIconId(int mIconId) {
        this.mIconId = mIconId;
    }
}
