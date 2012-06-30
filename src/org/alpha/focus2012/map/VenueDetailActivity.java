package org.alpha.focus2012.map;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.alpha.focus2012.AlphaAdapter;
import org.alpha.focus2012.R;
import org.alpha.focus2012.Row;
import org.alpha.focus2012.data.DataStore;
import org.alpha.focus2012.data.Venue;
import org.alpha.focus2012.resources.Resource;
import org.alpha.focus2012.rows.ButtonBarRow;
import org.alpha.focus2012.rows.DetailRow;
import org.alpha.focus2012.rows.HTMLRow;
import org.alpha.focus2012.rows.ImageRow;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;


public class VenueDetailActivity extends SherlockListActivity {
    
    public static final String VENUE_ID = "VENUE_ID";
    
    private ActionBar mActionBar;
    private Venue mVenue;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int venueId = getIntent().getIntExtra(VENUE_ID, 0);
        mVenue = DataStore.venue(this, venueId);
        
        mActionBar = getSupportActionBar(); 
        mActionBar.setTitle(mVenue.name);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        
        AlphaAdapter adapter = new AlphaAdapter();
        setListAdapter(adapter);                        
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        populate();
    }    
    
    
    private void populate() {
        
         List<Row> rows = new ArrayList<Row>();
         rows.add(new ImageRow(mVenue.name, mVenue.address(), new Resource(mVenue.imageKey, Resource.Type.VenueImageLarge), this));
         //removed the image from the row
         //rows.add(new DetailRow(mVenue.name, mVenue.address(), null, this));
         
         OnClickListener viewMapOnClick = new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                String url = "http://maps.google.co.uk?q=" + URLEncoder.encode(mVenue.name) + "@" + mVenue.latitude + "," + mVenue.longitude;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));                
            }
        };
        
        /*
        OnClickListener viewFloorplanOnClick = new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                try {
                    String url = "content://org.alpha.focus2012.provider/"+mVenue.floorplanKey;
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(VenueDetailActivity.this, "Sorry, no PDF viewer was found", Toast.LENGTH_SHORT).show();
                }
            }
        };
        */
        
        //rows.add(new ImageRow(new Resource(mVenue.imageKey, Resource.Type.VenueImageLarge), this));
        
        ButtonBarRow buttons = new ButtonBarRow(this);
        buttons.setButton1(getString(R.string.map_button_title), viewMapOnClick);
        //buttons.setButton2(getString(R.string.floorplan_button_title), viewFloorplanOnClick);
        rows.add(buttons);    
        
        rows.add(new HTMLRow(mVenue.details, this));
        
        ((AlphaAdapter) getListAdapter()).setRows(rows, this);
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
