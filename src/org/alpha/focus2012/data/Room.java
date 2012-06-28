package org.alpha.focus2012.data;

import org.alpha.util.JSON;
import org.json.JSONObject;



public class Room {

    public final int roomId, venueId;
    public final String name;


    Room(JSONObject o) {
        this.roomId = o.optInt("id");
        this.venueId = o.optInt("venue");
        this.name = JSON.getString(o, "name");
    }

}
