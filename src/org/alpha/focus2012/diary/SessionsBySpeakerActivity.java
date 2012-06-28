package org.alpha.focus2012.diary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.alpha.focus2012.AlphaAdapter;
import org.alpha.focus2012.Row;
import org.alpha.focus2012.Section;
import org.alpha.focus2012.data.DataStore;
import org.alpha.focus2012.data.Day;
import org.alpha.focus2012.data.Session;
import org.alpha.focus2012.data.Speaker;
import org.alpha.focus2012.rows.DiaryRow;
import org.alpha.util.MultiValueMap;
import org.alpha.util.ReadablePartialComparator;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;


public class SessionsBySpeakerActivity extends SherlockListActivity {

    public static final String EXTRA_SPEAKER_ID = "EXTRA_SPEAKER_ID";

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
        
        int speakerId = getIntent().getIntExtra(EXTRA_SPEAKER_ID, 0);
        Speaker speaker = DataStore.speaker(this, speakerId);
        
        List<Day> days = DataStore.days(this);
        
        MultiValueMap<Integer,Session> sessionsKeyedByDayId = new MultiValueMap<Integer,Session>();
        
        for (int sessionId : speaker.sessionIds) {
            Session session = DataStore.session(this, sessionId);
            sessionsKeyedByDayId.put(session.dayId, session);
        }

        List<Section> sections = new ArrayList<Section>();
        
        for (Day day : days) {
            Collection<Session> sessionsForDay = sessionsKeyedByDayId.get(day.dayId);
            if (sessionsForDay == null) {
                continue;
            }
            
            List<Session> sortedSessionsForDay = new ArrayList<Session>(sessionsForDay);
            Collections.sort(sortedSessionsForDay, new Comparator<Session>() {
                public int compare(Session a, Session b) {
                    return ReadablePartialComparator.NULLS_FIRST.compare(a.startDateTime, b.startDateTime);
                }
            });
            
            List<Row> rows = new ArrayList<Row>();
            for (final Session session : sessionsForDay) {
                DiaryRow row = DiaryRow.createForSession(session, this);
                row.setOnClickListener(new Row.OnClickListener() {
                    public void onRowClicked() {
                        Intent intent = new Intent(SessionsBySpeakerActivity.this, SessionDetailActivity.class);
                        intent.putExtra(SessionDetailActivity.EXTRA_SESSION_ID, session.sessionId);
                        startActivity(intent);
                    }
                });
                rows.add(row);
            }
            
            if (!rows.isEmpty()) {
                sections.add(new Section(day.date.toString("d MMMM yyyy"), rows, this));
            }
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
