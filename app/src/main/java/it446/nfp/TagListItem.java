package it446.nfp;

/**
 * Created by ChristensenKC on 11/16/2015.
 */
public class TagListItem {
    private int mIconId;
    private String mTagID;

    public TagListItem() {
        mIconId = -1;
        mTagID = "";
    }

    public int getmIconId() {
        return mIconId;
    }

    public void setmIconId(int mIconId) {
        this.mIconId = mIconId;
    }

    public String getmTagID() {
        return mTagID;
    }

    public void setmTagID(String mTagID) {
        this.mTagID = mTagID;
    }

}
