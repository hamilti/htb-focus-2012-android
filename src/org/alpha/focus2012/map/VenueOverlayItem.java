package org.alpha.focus2012.map;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class VenueOverlayItem extends OverlayItem {
    private final int mVenueId;
    
    public VenueOverlayItem(GeoPoint point, String title, String snippet, int venueId) {
        super(point, title, snippet);
        mVenueId = venueId;
    }
    
    public int getVenueId() {
        return mVenueId;
    }
}
