package org.alpha.focus2012.data;

import org.alpha.util.JSON;
import org.alpha.util.JSON.DateIntepretation;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.json.JSONObject;



public class Day implements Comparable<Day> {

    public final int dayId;
    public final LocalDate date;


    Day(JSONObject o) {
        this.dayId = o.optInt("id");
        this.date = JSON.getLocalDate(o, "date", DateIntepretation.SECONDS_SINCE_1970, DateTimeZone.UTC);
    }

    @Override
    public int compareTo(Day another) {
        return this.date.compareTo(another.date);
    }

}
