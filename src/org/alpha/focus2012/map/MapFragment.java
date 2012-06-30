package org.alpha.focus2012.map;

import java.util.ArrayList;
import java.util.List;

import org.alpha.focus2012.Constants;
import org.alpha.focus2012.R;
import org.alpha.focus2012.data.DataStore;
import org.alpha.focus2012.data.Venue;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public class MapFragment extends SherlockListFragment {


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.DATA_WAS_UPDATED_INTENT.equals(intent.getAction())) {
                populate();
            }
        }
    };


    private List<Venue> mVenues;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, container, false);
    }

    
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(receiver);
    }
    
    
    @Override
    public void onResume() {
        super.onResume();
        populate();
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(receiver, new IntentFilter(Constants.DATA_WAS_UPDATED_INTENT));
    }    
    

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (position == mVenues.size()) {
            Intent intent = new Intent(getActivity(), VenueMapActivity.class);
            startActivity(intent);        
        }
        else {
            Venue venue = mVenues.get(position);

            Intent intent = new Intent(getActivity(), VenueDetailActivity.class);
            intent.putExtra(VenueDetailActivity.VENUE_ID, venue.venueId);
            startActivity(intent);                
        }
        
        super.onListItemClick(l, v, position, id);
    }   
    
    
    private void populate() {
        Context context = getActivity();
        
        mVenues = DataStore.venues(context);
        if (mVenues.isEmpty()) {
            return;
        }
        
        List<String> rows = new ArrayList<String>();
        
        for (Venue venue : mVenues) {
            rows.add(venue.name);            
        }
        
        //Add the all items map
        rows.add(getString(R.string.view_all_venues_row_title));
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, rows);
        
        setListAdapter(adapter);
    }

}