package org.alpha.focus2012.home;

import java.util.ArrayList;
import java.util.List;

import org.alpha.focus2012.AlphaAdapter;
import org.alpha.focus2012.Constants;
import org.alpha.focus2012.R;
import org.alpha.focus2012.Row;
import org.alpha.focus2012.Row.OnClickListener;
import org.alpha.focus2012.data.Conference;
import org.alpha.focus2012.data.DataStore;
import org.alpha.focus2012.data.Venue;
import org.alpha.focus2012.events.EventDetailActivity;
import org.alpha.focus2012.programme.SessionDetailActivity;
import org.alpha.focus2012.rows.DetailRow;

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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;

import com.actionbarsherlock.app.SherlockListFragment;



public class HomeFragment extends SherlockListFragment {

	private static final int REQUEST_SHOW_OPTIONS = 10;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.DATA_WAS_UPDATED_INTENT.equals(intent.getAction())) {
                populate();
            }
        }
    };


    private AlphaAdapter mAdapter;
    private static String DATE_FORMAT = "dd MMMM YYYY";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list);
        
        mAdapter = new AlphaAdapter();
        setListAdapter(mAdapter);
        
        getListView().setOnItemClickListener(mAdapter);        
    }
    
 
    

    private void setContentView(int list) {
		// TODO Auto-generated method stub
    	
    	
    	
    	
		
	}




	private void populate() {
    	
    	final Context context = getActivity();
        List<Conference> conferences = DataStore.otherConferences(context);        
        List<Row> rows = new ArrayList<Row>();
                
        for (final Conference conference : conferences) {
                        
            String dateText = conference.startDate.toString(DATE_FORMAT);
            if (conference.endDate != null && (conference.endDate.compareTo(conference.startDate) > 0)) {
                dateText += " - " + conference.endDate.toString(DATE_FORMAT);                        
            }            
            
            Row row = new DetailRow(conference.name, dateText, null, context);
            row.setOnClickListener(new OnClickListener() {                
                @Override
                public void onRowClicked() {
                    
                    Intent intent = new Intent(context, EventDetailActivity.class);
                    intent.putExtra(EventDetailActivity.CONFERENCE_ID, conference.conferenceId);
                    context.startActivity(intent);
                }
            });
            rows.add(row);
        }
        
        mAdapter.setRows(rows, context);
    }
    
    
	@Override
	public void onActivityResult(int request, int result, Intent data) {
	    if (request == REQUEST_SHOW_OPTIONS && result == SessionDetailActivity.RESULT_OK) {
	        populate();
	    }
	}
	
}

