package org.alpha.focus2012.diary;

import java.util.ArrayList;
import java.util.List;

import org.alpha.focus2012.AlphaAdapter;
import org.alpha.focus2012.Row;
import org.alpha.focus2012.Section;
import org.alpha.focus2012.data.DataStore;
import org.alpha.focus2012.data.Session;
import org.alpha.focus2012.rows.DiaryRow;
import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;


public class SessionsByHourActivity extends SherlockListActivity {

	public static final String HOUR_NUMBER = "HOUR_NUMBER";
    public static final String DAY_NUMBER = "DAY_NUMBER";
    
    private ActionBar mActionBar;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar = getSupportActionBar(); 
        mActionBar.setTitle("Sessions");
        mActionBar.setDisplayHomeAsUpEnabled(true);

        AlphaAdapter adapter = new AlphaAdapter();
        setListAdapter(adapter);
        getListView().setOnItemClickListener(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        populate();
    }

    private void populate() {
        int hour = getIntent().getIntExtra(HOUR_NUMBER, 0);
        int dayId = getIntent().getIntExtra(DAY_NUMBER, 0);
        
        List<Session> sessions = DataStore.sessionsForDayHour(this, dayId, hour);
        List<Section> sections = new ArrayList<Section>();
        List<Row> rows = new ArrayList<Row>();
        //Log.d("SessionsByHourActivity", "C:");
         
        for (final Session session : sessions) {
        	 //Log.d("SessionsByHourActivity", "D: " + session.name);
             DiaryRow row = DiaryRow.createForSession(session, this);
             row.setOnClickListener(new Row.OnClickListener() {
                 public void onRowClicked() {
                     Intent intent = new Intent(SessionsByHourActivity.this, SessionDetailActivity.class);
                     intent.putExtra(SessionDetailActivity.EXTRA_SESSION_ID, session.sessionId);
                     startActivity(intent);
                 }
              });
              rows.add(row);
        }
        if (!rows.isEmpty()) {
          sections.add(new Section("Choose a Session", rows, this));
        }
        ((AlphaAdapter) getListAdapter()).setSections(sections); 
 }    	
    
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }


}
