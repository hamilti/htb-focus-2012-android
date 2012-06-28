package org.alpha.focus2012.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.alpha.focus2012.R;
import org.alpha.util.JSON;
import org.alpha.util.JSON.DateIntepretation;


public class Session {

    public static enum Type {
        NONE(0, R.color.session_default),
        MAIN(1, R.color.session_type_main),
        SEMINAR_OPTION(2, R.color.session_type_seminar_option),
        SEMINAR_SLOT(3, R.color.session_type_seminar_slot),
        BREAK(4, R.color.session_type_break),
        ADMIN(5, R.color.session_type_admin),
        STANDARD(6, R.color.session_default);
        
        public static final List<Type> TYPES = Collections.unmodifiableList(Arrays.asList(NONE, MAIN, SEMINAR_OPTION, SEMINAR_SLOT, BREAK, ADMIN, STANDARD));
        
        public final int intValue;
        public final int color;
        
        private Type(int intValue, int color) {
            this.intValue = intValue;
            this.color = color;
        }
    }

    public final int sessionId, dayId, roomId, streamId, sessionGroupId;
    public final Type type;
    public final String name, text;
    public final LocalDateTime startDateTime, endDateTime;

    public final List<Integer> speakerIds = new ArrayList<Integer>();
        
    Session(JSONObject o) {
        this.sessionId = o.optInt("id");
        this.dayId = o.optInt("day");
        this.type = Type.TYPES.get(o.optInt("session_type"));
        this.roomId = o.optInt("room");
        this.streamId = o.optInt("stream");
        this.sessionGroupId = o.optInt("session_group");
        this.name = JSON.getString(o, "name");
        this.startDateTime = JSON.getLocalDateTime(o, "start_datetime", DateIntepretation.SECONDS_SINCE_1970, DateTimeZone.UTC);
        this.endDateTime = JSON.getLocalDateTime(o, "end_datetime", DateIntepretation.SECONDS_SINCE_1970, DateTimeZone.UTC);
        this.text = JSON.getString(o, "description");
        try {
            JSONArray a = o.getJSONArray("speakers");
            for (int x=0; x<a.length(); x++) {
                speakerIds.add(a.getInt(x));
            } 
        } catch (JSONException e) {
            // ignore
        }
    }


    public LocalDateTime startHour() {
        return startDateTime.hourOfDay().roundFloorCopy();
    }

}
