package org.alpha.focus2012.twitter;

import org.json.JSONObject;

import org.alpha.focus2012.resources.Resource;
import org.alpha.util.JSON;


public class Tweet {
    
    public final String name;
    public final String text;
    public final String time;
    public final Resource avatar;

    
    Tweet(JSONObject o) {
        this.name = JSON.getString(o, "from_user_name");
        this.text = JSON.getString(o, "text");
        this.time = JSON.getLocalDateTime(o, "created_at", JSON.DateIntepretation.TWITTER, null).toString("d MMMM - H:mm");
        this.avatar = new Resource(JSON.getString(o, "from_user"), Resource.Type.TwitterAvatar);
    }
    
}
