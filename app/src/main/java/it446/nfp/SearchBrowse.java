package it446.nfp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChristensenKC on 11/5/2015.
 */
public class SearchBrowse extends AppCompatActivity{
    private List<ClinicListItem> mSearchClinicList = new ArrayList<ClinicListItem>();
    private ListView searchResultListView;
    private LinearLayout noResultsLayout;

    private ArrayAdapter<ClinicListItem> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_browse);

        setProgressBarIndeterminateVisibility(true);

        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) findViewById(R.id.search_friends);
        searchResultListView = (ListView) findViewById(R.id.search_results_list_view);
        noResultsLayout = (LinearLayout) findViewById(R.id.no_results_container);

        getClinicData();
    }

    public void getClinicData() {

    }
}
