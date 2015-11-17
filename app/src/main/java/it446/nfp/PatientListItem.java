package it446.nfp;

/**
 * Created by ChristensenKC on 11/9/2015.
 */
public class PatientListItem {
    private int mIconId;
    private String mUserName;
    private String mDOB;

    public PatientListItem() {
        mIconId = -1;
        mUserName = "";
        mDOB = "";
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


}
