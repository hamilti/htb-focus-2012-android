package org.alpha.focus2012.data;

import java.util.ArrayList;
import java.util.List;

import org.alpha.util.JSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Speaker implements Comparable<Speaker> {

    public final int speakerId;
    public final String twitterUsername;
    public final String websiteUrl;
    public final String biography;
    public final String position;
    public final String imageKey;
    public final List<Integer> sessionIds = new ArrayList<Integer>();
    
    private final String firstName, lastName, alias, sortableName;
    
    
    Speaker(JSONObject o) {
        this.speakerId = o.optInt("id");
        this.firstName = JSON.getString(o, "first_name");
        this.lastName = JSON.getString(o, "last_name");
        this.biography = JSON.getString(o, "biography");
        this.position = JSON.getString(o, "position");
        this.imageKey = JSON.getString(o, "image_key");
        this.twitterUsername = JSON.getString(o, "twitter_username");
        this.websiteUrl = JSON.getString(o, "website_url");
        this.alias = JSON.getString(o, "alias");
        this.sortableName = lastName+" "+firstName;
        
        try {
            JSONArray a = o.getJSONArray("sessions");
            for (int x=0; x<a.length(); x++) {
                sessionIds.add(a.getInt(x));
            }
        } catch (JSONException e) {
            // ignore
        }
    }
    
    
    public String displayName() {
        return (alias != null) ? alias : firstName+" "+lastName;
    }

    public String indexLetter() {
        return (lastName != null && lastName.length() > 0) ? lastName.substring(0, 1).toUpperCase() : "?";
    }

    @Override
    public int compareTo(Speaker another) {
        return this.sortableName.compareTo(another.sortableName);
    }
    
}
