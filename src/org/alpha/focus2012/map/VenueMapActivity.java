package org.alpha.focus2012.map;

import java.util.ArrayList;
import java.util.List;

import org.alpha.focus2012.Constants;
import org.alpha.focus2012.R;
import org.alpha.focus2012.data.DataStore;
import org.alpha.focus2012.data.Venue;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class VenueMapActivity extends SherlockMapActivity {
    private ActionBar mActionBar;
    private MapView mMapView;    
    
    
    
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        
        mMapView = new MapView(this, Constants.mapsAPIKey);
        mMapView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        mMapView.setBuiltInZoomControls(true);
        mMapView.setEnabled(true);
        mMapView.setClickable(true);
        layout.addView(mMapView);
        
        setContentView(layout);
        
        mActionBar = getSupportActionBar();    
        mActionBar.setTitle(getString(R.string.all_venues_action_bar_title));
        mActionBar.setDisplayHomeAsUpEnabled(true);        
    }
    
    
    @Override
    public void onResume() {
        super.onResume();
        populate();
    }    
    
    
    private void populate() {
        Context context = this;
        double south = 90;
        double north = -90;
        double west = 180;
        double east = -180;        
        Log.d("VenueMapActivity", "start");
        List<Venue> mVenues = DataStore.venues(context);
        
        if (mVenues.isEmpty()) {
            return;
        }        
        
        List<VenueOverlayItem> overlayItems = new ArrayList<VenueOverlayItem>();
        for (Venue venue : mVenues) {
            GeoPoint point = new GeoPoint((int)(venue.latitude *1E6), (int)(venue.longitude *1E6));
            VenueOverlayItem overlayItem  = new VenueOverlayItem(point, venue.name, venue.address(), venue.venueId);
            overlayItems.add(overlayItem);        
            
            south = Math.min(south, venue.latitude);
            north = Math.max(north, venue.latitude);
            west = Math.min(west, venue.longitude);
            east = Math.max(east, venue.longitude);
        }    
        
        List<Overlay> mapOverlays = mMapView.getOverlays();
        
        Drawable defaultMarker = getResources().getDrawable(R.drawable.marker);
        VenueBalloonItemizedOverlay itemizedOverlay = new VenueBalloonItemizedOverlay(defaultMarker, mMapView);        
        itemizedOverlay.addOverlayItems(overlayItems);    
        
        mapOverlays.add(itemizedOverlay);
        
        MapController mapController = mMapView.getController();
        
        // set centre point of map
        int centerLat = (int)(((north + south) / 2) *1E6);
        int centerLong = (int)(((east + west) / 2) *1E6);
        mapController.setCenter(new GeoPoint(centerLat, centerLong));
        
        // zoom pan to accommodate overlays
        int latSpan = (int)((Math.abs(north - ((north + south) / 2)) *3) *1E6);
        int longSpan = (int)((Math.abs(east - ((east + west) / 2)) *3) *1E6);
                
        mapController.zoomToSpan(latSpan, longSpan);        
    }
    
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
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