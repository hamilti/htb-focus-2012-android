package org.alpha.focus2012.map;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class VenueBalloonItemizedOverlay extends BalloonItemizedOverlay<VenueOverlayItem> {
    private List<VenueOverlayItem> mOverlayItems = new ArrayList<VenueOverlayItem>();
    private final Context mContext;
    
    public VenueBalloonItemizedOverlay(Drawable defaultMarker, MapView mapView) {
        super(boundCenter(defaultMarker), mapView);
        mContext = mapView.getContext();
    }


    @Override
    protected VenueOverlayItem createItem(int i) {
        return mOverlayItems.get(i);
    }


    @Override
    public int size() {
        return mOverlayItems.size();
    }

    
    @Override
    protected boolean onBalloonTap(int index, VenueOverlayItem item) {
        Intent intent = new Intent(mContext, VenueDetailActivity.class);
        intent.putExtra(VenueDetailActivity.VENUE_ID, mOverlayItems.get(index).getVenueId());
        mContext.startActivity(intent);            
        return true;
    }
    
    
    public void addOverlayItems(List<VenueOverlayItem> overlayItems) {
        mOverlayItems = overlayItems;
        populate();
    }
}