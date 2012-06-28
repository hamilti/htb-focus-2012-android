package org.alpha.focus2012.data;

import org.alpha.util.JSON;
import org.json.JSONObject;



public class SpecialOffer {

    public final int specialOfferId;
    public final String title, html;


    SpecialOffer(JSONObject o) {
        this.specialOfferId = o.optInt("id");
        this.title = JSON.getString(o, "title");
        this.html = JSON.getString(o, "description");
    }

}
