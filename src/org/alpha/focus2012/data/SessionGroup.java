package org.alpha.focus2012.data;

import org.alpha.util.JSON;
import org.json.JSONObject;



public class SessionGroup {

    public final int sessionGroupId, dayId;
    public final String name;


    SessionGroup(JSONObject o) {
        this.sessionGroupId = o.optInt("id");
        this.dayId = o.optInt("day");
        this.name = JSON.getString(o, "name");
    }

}
