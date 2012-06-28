package org.alpha.focus2012.data;

import org.alpha.util.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.json.JSONObject;



public class Venue {

    public final int venueId;
    public final String name, details, imageKey, floorplanKey;
    public final double latitude, longitude;

    private final String streetAddress, county, country, postcode;


    Venue(JSONObject o) {
        this.venueId = o.optInt("id");
        this.name = JSON.getString(o, "name");
        this.latitude = o.optDouble("latitude");
        this.longitude = o.optDouble("longitude");
        this.streetAddress = JSON.getString(o, "street_address");
        this.county = JSON.getString(o, "county");
        this.country = JSON.getString(o, "country");
        this.postcode = JSON.getString(o, "postcode");
        this.details = JSON.getString(o, "details");
        this.imageKey = JSON.getString(o, "image_key");
        this.floorplanKey = JSON.getString(o, "floorplan_key");
    }


    public String address() {
        StrBuilder sb = new StrBuilder();
        if (StringUtils.isNotBlank(streetAddress)) sb.append(streetAddress);
        if (StringUtils.isNotBlank(county)) sb.appendSeparator('\n').append(county);
        if (StringUtils.isNotBlank(postcode)) sb.appendSeparator('\n').append(postcode);
        if (StringUtils.isNotBlank(country)) sb.appendSeparator('\n').append(country);
        return sb.toString();
    }

}
