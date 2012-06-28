package org.alpha.focus2012.data;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import org.alpha.focus2012.Constants;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DataStore {

    private static final String TAG = "DataStore";

    
    public static Conference conference(Context context, int conferenceId) {
        if (conferenceId == Constants.CONFERENCE_ID) {
            return entity(context, Conference.class, "conference", conferenceId);
        } else {
            return entity(context, Conference.class, "other_conferences", conferenceId);
        }
    }

    public static List<Conference> otherConferences(Context context) {
        List<Conference> result = entities(context, Conference.class, "other_conferences");
        Collections.sort(result);
        return result;
    }


    public static List<Speaker> speakers(Context context) {
        return entities(context, Speaker.class, "speakers");
    }

    public static Speaker speaker(Context context, int speakerId) {
        return entity(context, Speaker.class, "speakers", speakerId);
    }

    public static List<Day> days(Context context) {
        List<Day> result = new ArrayList<Day>();
        DBHelper h = null;
        SQLiteDatabase db = null;
        try {
            h = new DBHelper(context);
            db = h.getWritableDatabase();

            List<JSONObject> entities = DBHelper.getEntities(db, "days");
            for (JSONObject j : entities) {
                result.add(new Day(j));
            }
            
        } finally {
            if (db != null) db.close();
            if (h != null) h.close();
        }
        
        Collections.sort(result);
        return result;
    }


    public static List<Session> sessions(Context context) {
        List<Session> result = new ArrayList<Session>();
        DBHelper h = null;
        SQLiteDatabase db = null;
        try {
            h = new DBHelper(context);
            db = h.getWritableDatabase();
            //Log.d("Datastore", Constants.ACS_SERVER_URL);
            List<JSONObject> entities = DBHelper.getEntities(db, "sessions");
            for (JSONObject j : entities) {
            	//Log.d("Datastore add", j.toString());
            	result.add(new Session(j));
            }
        } finally {
        	//Log.d("Datastore", "Close DB Connections");
            if (db != null) db.close();
            if (h != null) h.close();
        }
        return result;
    }

    public static Session session(Context context, int sessionId) {
        return entity(context, Session.class, "sessions", sessionId);
    }


    public static List<Session> sessionsForGroup(Context context, int sessionGroupId) {
        List<Session> result = new ArrayList<Session>();
        for (Session s : sessions(context)) {
            if (s.sessionGroupId == sessionGroupId) {
                result.add(s);
            }
        }
        return result;
    }
    
    public static List<Session> sessionsForDayHour(Context context, int dayId, int hour) {
        List<Session> result = new ArrayList<Session>();
        int localHour = 0;
        for (Session s : sessions(context)) {
        	//Log.d("sessionsForDayHour", "day:" + Integer.toString(s.dayId) + "=" + Integer.toString(dayId) + "  hr:" + Integer.toString(localHour) + "=" + Integer.toString(hour));
        	localHour = Integer.parseInt(s.startDateTime.toString("HH"));       	
            if (s.dayId == dayId && localHour == hour) {
            	//Log.d("sessionsForDayHour", "Match");
                result.add(s);
            }
        }
        return result;
    }


    public static List<SessionGroup> sessionGroups(Context context) {
        return entities(context, SessionGroup.class, "session_groups");
    }


    public static List<Stream> streams(Context context) {
        return entities(context, Stream.class, "streams");
    }

    public static Stream stream(Context context, int streamId) {
        return entity(context, Stream.class, "streams", streamId);
    }


    public static List<Venue> venues(Context context) {
        List<Venue> result = new ArrayList<Venue>();
        DBHelper h = null;
        SQLiteDatabase db = null;
        try {
            h = new DBHelper(context);
            db = h.getWritableDatabase();
            List<JSONObject> entities = DBHelper.getEntities(db, "venues");
            for (JSONObject j : entities) {
                result.add(new Venue(j));
            }
        } finally {
            if (db != null) db.close();
            if (h != null) h.close();
        }
        return result;
    }

    public static Venue venue(Context context, int venueId) {
        return entity(context, Venue.class, "venues", venueId);
    }

    public static Room room(Context context, int roomId) {
        return entity(context, Room.class, "rooms", roomId);
    }


    public static List<Alert> alerts(Context context) {
        List<Alert> alerts = entities(context, Alert.class, "alerts");
        Collections.sort(alerts);
        Collections.reverse(alerts);
        return alerts;
    }

    public static Alert alert(Context context, int alertId) {
        return entity(context, Alert.class, "alerts", alertId);
    }


    public static List<FAQ> faqs(Context context) {
        return entities(context, FAQ.class, "faqs");
    }

    public static FAQ faq(Context context, int faqId) {
        return entity(context, FAQ.class, "faqs", faqId);
    }


    public static List<SpecialOffer> specialOffers(Context context) {
        return entities(context, SpecialOffer.class, "special_offers");
    }

    public static SpecialOffer specialOffer(Context context, int specialOfferId) {
        return entity(context, SpecialOffer.class, "special_offers", specialOfferId);
    }


    private static <C> List<C> entities(Context context, Class<C> c, String type) {
        DBHelper h = null;
        SQLiteDatabase db = null;
        try {
            h = new DBHelper(context);
            db = h.getWritableDatabase();
            List<JSONObject> entities = DBHelper.getEntities(db, type);
            List<C> result = new ArrayList<C>();

            try {
                for (JSONObject j : entities) {
                    C instance = c.getDeclaredConstructor(JSONObject.class).newInstance(j);
                    result.add(instance);
                }
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "unexpected error", e);
            } catch (InstantiationException e) {
                Log.e(TAG, "unexpected error", e);
            } catch (IllegalAccessException e) {
                Log.e(TAG, "unexpected error", e);
            } catch (InvocationTargetException e) {
                Log.e(TAG, "unexpected error", e);
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "unexpected error", e);
            }

            return result;

        } finally {
            if (db != null) db.close();
            if (h != null) h.close();
        }
    }


    private static <C> C entity(Context context, Class<C> c, String type, int entityId) {
        DBHelper h = null;
        SQLiteDatabase db = null;
        try {
            h = new DBHelper(context);
            db = h.getWritableDatabase();
            C result = null;

            try {
                JSONObject j = DBHelper.getEntity(db, type, entityId);
                if (j != null) {
                    result = c.getDeclaredConstructor(JSONObject.class).newInstance(j);
                }
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "unexpected error", e);
            } catch (InstantiationException e) {
                Log.e(TAG, "unexpected error", e);
            } catch (IllegalAccessException e) {
                Log.e(TAG, "unexpected error", e);
            } catch (InvocationTargetException e) {
                Log.e(TAG, "unexpected error", e);
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "unexpected error", e);
            }

            return result;

        } finally {
            if (db != null) db.close();
            if (h != null) h.close();
        }
    }


}
