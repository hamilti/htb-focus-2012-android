package org.alpha.focus2012.diary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alpha.focus2012.AlphaAdapter;
import org.alpha.focus2012.Row;
import org.alpha.focus2012.Section;
import org.alpha.focus2012.data.DataStore;
import org.alpha.focus2012.data.Session;
import org.alpha.focus2012.data.Stream;
import org.alpha.focus2012.rows.DiaryRow;
import org.alpha.util.MultiValueMap;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;


public class SeminarOptionsActivity extends SherlockListActivity {

    public static final String EXTRA_SESSION_GROUP_ID = "EXTRA_SESSION_GROUP_ID";

    public static final int REQUEST_SHOW_DETAIL = 10;

    private ActionBar mActionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar = getSupportActionBar(); 
        mActionBar.setTitle("Seminar Options");
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
        
        int sessionGroupId = getIntent().getIntExtra(EXTRA_SESSION_GROUP_ID, 0);
        List<Session> sessions = DataStore.sessionsForGroup(this, sessionGroupId);

        Map<Integer,Stream> streams = new HashMap<Integer,Stream>();
        for (Stream s : DataStore.streams(this)) {
            streams.put(s.streamId, s);
        }
        
        MultiValueMap<Integer, Session> sessionsKeyedByStreamId = new MultiValueMap<Integer, Session>();
        for (Session session : sessions) {
            sessionsKeyedByStreamId.put(session.streamId, session);
        }
        
        List<Stream> streamsSortedAlphabetically = new ArrayList<Stream>(streams.values());
        Collections.sort(streamsSortedAlphabetically, new Comparator<Stream>() {
            public int compare(Stream a, Stream b) {
                return a.name.compareTo(b.name);
            }
        });
        
        List<Section> sections = new ArrayList<Section>();
        
        for (Stream stream : streamsSortedAlphabetically) {
            if (sessionsKeyedByStreamId.containsKey(stream.streamId)) {
                List<Row> rows = new ArrayList<Row>();
                for (final Session session : sessionsKeyedByStreamId.get(stream.streamId)) {
                    DiaryRow row = DiaryRow.createForSession(session, this);
                    row.setOnClickListener(new Row.OnClickListener() {
                        public void onRowClicked() {
                            Intent intent = new Intent(SeminarOptionsActivity.this, SessionDetailActivity.class);
                            intent.putExtra(SessionDetailActivity.EXTRA_SESSION_ID, session.sessionId);
                            startActivityForResult(intent, REQUEST_SHOW_DETAIL);
                        }
                    });
                    rows.add(row);
                }
                sections.add(new Section(stream.name, rows, this));
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


    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        if (request == REQUEST_SHOW_DETAIL && result == SessionDetailActivity.RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

}
