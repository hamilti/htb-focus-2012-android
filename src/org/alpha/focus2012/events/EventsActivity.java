package org.alpha.focus2012.events;

import java.util.ArrayList;
import java.util.List;

import org.alpha.focus2012.AlphaAdapter;
import org.alpha.focus2012.R;
import org.alpha.focus2012.Row;
import org.alpha.focus2012.Row.OnClickListener;
import org.alpha.focus2012.data.Conference;
import org.alpha.focus2012.data.DataStore;
import org.alpha.focus2012.rows.DetailRow;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListActivity;

public class EventsActivity extends SherlockListActivity {
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
    
    
    private void populate() {
        List<Conference> conferences = DataStore.otherConferences(this);        
        
        List<Row> rows = new ArrayList<Row>();
        final Context context = this;
        
        
        for (final Conference conference : conferences) {
                        
            String dateText = conference.startDate.toString(DATE_FORMAT);
            if (conference.endDate != null && (conference.endDate.compareTo(conference.startDate) > 0)) {
                dateText += " - " + conference.endDate.toString(DATE_FORMAT);                        
            }            
            
            Row row = new DetailRow(conference.name, dateText, null, this);
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
        
        mAdapter.setRows(rows, this);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        populate();        
    }
}