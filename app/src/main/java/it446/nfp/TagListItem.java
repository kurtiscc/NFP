package it446.nfp;

import org.json.JSONArray;
//import LatLng;
import java.lang.Object;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ChristensenKC on 11/16/2015.
 */
public class TagListItem {
    private int mIconId;
    private String mTagUID;
    private String mUserdid;
    private String mLat;
    private String mLon;
    private LatLng mLatLng;
    private String mAddr;
    private String mPurpose;
    private String mTimestamp;
    private JSONArray mTrackSteps;
    //private LatLng
//use parseDouble(string s) on lat lon strings from json. LatLng(Double, Double) is an object. mLatlng.getLatituted(); mlatlng.getLongitude();
    // drawMarker(mLatLng); or drawMarker(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
    public TagListItem() {
        mIconId = -1;
        mTagUID = "";
    }

    public int getmIconId() {
        return mIconId;
    }

    public void setmIconId(int mIconId) {
        this.mIconId = mIconId;
    }

    public String getmTagID() {
        return mTagUID;
    }

    public void setmTagID(String mTagID) {
        this.mTagUID = mTagUID;
    }

}


//var sampleTemplate = {
//        userid:null,
//        tagUID:null,
//        trackingSteps:[]
//        }
//        var trackingStepTemplate = {
//        lat:null,
//        lon:null,
//        addr:null,
//        userid:null,
//        purpose:null,
//        timestamp:null
//        }
//        router.post('/addsample', function(req, res, next){
//        var localsample = {
//        userid:null,
//        tagUID:null,
//        trackingSteps:[]
//        }
//        var localtracking = {
//        lat:null,
//        lon:null,
//        addr:null,
//        userid:null,
//        purpose:null,
//        timestamp:null
//        }