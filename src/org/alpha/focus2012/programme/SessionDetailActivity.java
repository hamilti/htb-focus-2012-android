package org.alpha.focus2012.programme;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.StrBuilder;

import org.alpha.focus2012.AlphaAdapter;
import org.alpha.focus2012.R;
import org.alpha.focus2012.Row;
import org.alpha.focus2012.Section;
import org.alpha.focus2012.data.DataStore;
import org.alpha.focus2012.data.Room;
import org.alpha.focus2012.data.Session;
import org.alpha.focus2012.data.Speaker;
import org.alpha.focus2012.data.Venue;
import org.alpha.focus2012.map.VenueDetailActivity;
import org.alpha.focus2012.rows.ButtonBarRow;
import org.alpha.focus2012.rows.DetailRow;
import org.alpha.focus2012.rows.HTMLRow;
import org.alpha.focus2012.rows.SpeakerRow;
import org.alpha.focus2012.speakers.SpeakerDetailActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;


public class SessionDetailActivity extends SherlockListActivity {
    
    public static final String EXTRA_SESSION_ID = "EXTRA_SESSION_ID";
    
    private ActionBar mActionBar;
    private Session session;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int sessionId = getIntent().getIntExtra(EXTRA_SESSION_ID, 0);
        session = DataStore.session(this, sessionId);
        
        mActionBar = getSupportActionBar(); 
        mActionBar.setTitle(session.name);
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
        List<Row> detailRows = new ArrayList<Row>();
        List<Row> speakerRows = new ArrayList<Row>();
        
        Room room = DataStore.room(this, session.roomId);
        final Venue venue;
        if (room != null) {
            venue = DataStore.venue(this, room.venueId);
        }
        else {
            venue = null;
        }
        
        // details
        
        StrBuilder subtitle = new StrBuilder();
        if (venue != null) {
            subtitle.append(venue.name + ", " + room.name);
        }
        subtitle.appendSeparator("\n");
        subtitle.append(session.startDateTime.toString("HH:mm") + " - "+session.endDateTime.toString("HH:mm"));
        subtitle.append(", ").append(session.startDateTime.toString("d MMMM yyyy"));
        detailRows.add(new DetailRow(session.name, subtitle.toString(), null, this));

        // buttons
        
        OnClickListener venueButtonHandler = null;
        if (venue != null) {
            venueButtonHandler = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), VenueDetailActivity.class);
                    intent.putExtra(VenueDetailActivity.VENUE_ID, venue.venueId);
                    startActivity(intent);    
                }
            };
        }
        
        boolean bookmarked = ProgrammeChoices.isSessionBookmarked(this, session) || session.type == Session.Type.MAIN;
        String bookmarkButtonTitle = bookmarked ? getString(R.string.session_bookmarked_button_title) : getString(R.string.session_bookmark_button_title);
        OnClickListener bookmarkButtonHandler = null;
        if (!bookmarked && session.type == Session.Type.SEMINAR_OPTION) {
            bookmarkButtonHandler = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgrammeChoices.bookmarkSession(SessionDetailActivity.this, session);
                    setResult(RESULT_OK);
                    finish();
                }
            };
        }
        
        ButtonBarRow buttons = new ButtonBarRow(this);
        buttons.setButton1(getString(R.string.venue_details_button_title), venueButtonHandler);
        buttons.setButton2(bookmarkButtonTitle, bookmarkButtonHandler);
        detailRows.add(buttons);
        
        // description
        
        detailRows.add(new HTMLRow(session.text, this));

        // speakers
        
        for (int speakerId : session.speakerIds) {
            final Speaker speaker = DataStore.speaker(this, speakerId);
            if (speaker != null) {
                SpeakerRow row = new SpeakerRow(speaker, this);
                row.setOnClickListener(new Row.OnClickListener() {
                    @Override
                    public void onRowClicked() {
                        Intent intent = new Intent(SessionDetailActivity.this, SpeakerDetailActivity.class);
                        intent.putExtra(SpeakerDetailActivity.EXTRA_SPEAKER_ID, speaker.speakerId);
                        startActivity(intent);
                    }
                });
                speakerRows.add(row);
            }
        }

        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section(null, detailRows, this));
        Section speakersSection = new Section(getString(R.string.session_speakers_title), speakerRows, this);
        speakersSection.mSectionBackgroundColourResource = R.color.fixed_section_header;
        sections.add(speakersSection);
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
