package org.alpha.focus2012.data;

import org.alpha.util.JSON;
import org.json.JSONObject;



public class Stream {

    public final int streamId;
    public final String name;
    public final Integer color;


    Stream(JSONObject o) {
        this.streamId = o.optInt("id");
        this.name = JSON.getString(o, "name");
        this.color = JSON.getColor(o, "colour");
    }

}
