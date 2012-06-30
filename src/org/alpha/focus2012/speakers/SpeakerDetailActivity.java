package org.alpha.focus2012.speakers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.alpha.focus2012.AlphaAdapter;
import org.alpha.focus2012.R;
import org.alpha.focus2012.Row;
import org.alpha.focus2012.Section;
import org.alpha.focus2012.data.DataStore;
import org.alpha.focus2012.data.Day;
import org.alpha.focus2012.data.Session;
import org.alpha.focus2012.data.Speaker;
import org.alpha.focus2012.diary.SessionDetailActivity;
import org.alpha.focus2012.resources.Resource;
import org.alpha.focus2012.rows.ButtonBarRow;
import org.alpha.focus2012.rows.DetailRow;
import org.alpha.focus2012.rows.HTMLRow;
import org.alpha.focus2012.rows.DiaryRow;
import org.alpha.focus2012.rows.ImageRow;
import org.alpha.util.MultiValueMap;
import org.alpha.util.ReadablePartialComparator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;


public class SpeakerDetailActivity extends SherlockListActivity {
    
    public static final String EXTRA_SPEAKER_ID = "EXTRA_SPEAKER_ID";
    
    private ActionBar mActionBar;
    private Speaker speaker;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int speakerId = getIntent().getIntExtra(EXTRA_SPEAKER_ID, 0);
        speaker = DataStore.speaker(this, speakerId);
        
        mActionBar = getSupportActionBar(); 
        mActionBar.setTitle(speaker.displayName());
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
        List<Row> rows = new ArrayList<Row>();
        rows.add(new ImageRow(speaker.displayName(), speaker.position, new Resource(speaker.imageKey, Resource.Type.SpeakerImageLarge), this));
        rows.add(new HTMLRow(speaker.biography, this));

        OnClickListener websiteOnClick = null;
        if (speaker.websiteUrl != null) {
            websiteOnClick = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(speaker.websiteUrl)));
                }
            };
        }

        OnClickListener twitterOnClick = null;
        if (speaker.twitterUsername != null) {
            twitterOnClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "http://twitter.com/"+speaker.twitterUsername;
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }
            };
        }

        if (websiteOnClick != null || twitterOnClick != null) {
            ButtonBarRow buttons = new ButtonBarRow(this);
            buttons.setButton1(getString(R.string.website_button), websiteOnClick);
            buttons.setButton2(getString(R.string.twiter_button), twitterOnClick);
            rows.add(buttons);            
        }

//        DetailRow sessionsRow = new DetailRow("View their sessions", null, null, this);
//        sessionsRow.setOnClickListener(new Row.OnClickListener() {
//            public void onRowClicked() {
//                Intent intent = new Intent(SpeakerDetailActivity.this, SessionsBySpeakerActivity.class);
//                intent.putExtra(SessionsBySpeakerActivity.EXTRA_SPEAKER_ID, speaker.speakerId);
//                startActivity(intent);
//            }
//        });
//        rows.add(sessionsRow);
        
        // speakers
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section(null, rows, this));
      
        List<Section> speakerSessions = getSessionsForSpeaker(speaker);
        if (!speakerSessions.isEmpty()) {
        	Section sessionsHeaderSection = new Section(getString(R.string.speaker_sessions_title), new ArrayList<Row>(), this);
            sessionsHeaderSection.mSectionBackgroundColourResource = R.color.fixed_section_header;
            sections.add(sessionsHeaderSection);
        	sections.addAll(speakerSessions);
        }
        ((AlphaAdapter) getListAdapter()).setSections(sections);
    }
    
    private List<Section> getSessionsForSpeaker(Speaker speaker) {
        
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
                @Override
                public int compare(Session a, Session b) {
                    return ReadablePartialComparator.NULLS_FIRST.compare(a.startDateTime, b.startDateTime);
                }
            });
            
            List<Row> rows = new ArrayList<Row>();
            for (final Session session : sessionsForDay) {
                DiaryRow row = DiaryRow.createForSession(session, this);
                row.setOnClickListener(new Row.OnClickListener() {
                    @Override
                    public void onRowClicked() {
                        Intent intent = new Intent(SpeakerDetailActivity.this, SessionDetailActivity.class);
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
        
        return sections;
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
