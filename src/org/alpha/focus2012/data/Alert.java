package org.alpha.focus2012.data;

import org.alpha.util.JSON;
import org.alpha.util.ReadablePartialComparator;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.json.JSONObject;



public class Alert implements Comparable<Alert> {

    public final int alertId;
    public final String title, message;
    public final LocalDateTime dateTime;


    Alert(JSONObject o) {
        this.alertId = o.optInt("id");
        this.title = JSON.getString(o, "title");
        this.message = JSON.getString(o, "message");
        this.dateTime = JSON.getLocalDateTime(o, "sent_datetime", JSON.DateIntepretation.SECONDS_SINCE_1970, DateTimeZone.UTC);
    }


    @Override
    public int compareTo(Alert that) {
        return ReadablePartialComparator.NULLS_LAST.compare(this.dateTime, that.dateTime);
    }


}
