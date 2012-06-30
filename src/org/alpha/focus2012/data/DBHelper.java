package org.alpha.focus2012.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import org.alpha.focus2012.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {

    
    
    private static final String TAG = "DBHelper";

    
    public DBHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "create database");
        onUpgrade(db, 0, Constants.DATABASE_VERSION);
    }

    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "database needs to be upgraded from "+oldVersion+" to "+newVersion);
        
        for (int v=oldVersion+1; v<=newVersion; ++v) {
            switch (v) {
            case 1:
                upgradeToVersion1(db);
                break;
            }
            Log.i(TAG, "upgraded to version "+v);
        }
    }
    
    private void upgradeToVersion1(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE entities ("+
                "type TEXT NOT NULL, "+
                "entity_id INTEGER NOT NULL, "+
                "json TEXT NOT NULL, "+
                "PRIMARY KEY (type, entity_id))");

        db.execSQL("CREATE TABLE tweets ("+
                "tweet_id TEXT NOT NULL PRIMARY KEY, "+
                "json TEXT NOT NULL)");

        db.execSQL("CREATE TABLE entities_updated (id INTEGER NOT NULL PRIMARY KEY, time INTEGER NOT NULL)");
    }
    
//TODO: remove this for API 7 support...
    
    public static void insertOrUpdateEntity(SQLiteDatabase db, String type, int id, String json) {
        ContentValues values = new ContentValues();
        values.put("type", type);
        values.put("entity_id", id);
        values.put("json", json);
        db.insertWithOnConflict("entities", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    
    
    public static void deleteEntity(SQLiteDatabase db, String type, int id) {
        db.delete("entities", "type=? AND entity_id=?", new String[] {type, String.valueOf(id)});
    }


    public static void setEntitiesUpdatedTime(SQLiteDatabase db, long time) {
        Log.d(TAG, "saving entities updated time, it is "+time);
        ContentValues values = new ContentValues();
        values.put("id", 0);
        values.put("time", time);
        db.insertWithOnConflict("entities_updated", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    
    public static List<JSONObject> getEntities(SQLiteDatabase db, String type) {
        List<JSONObject> results = new ArrayList<JSONObject>();
        String[] columns = {"json"};
        String selection = "type=?";
        String[] selectionArgs = {type};
        Cursor c = db.query("entities", columns, selection, selectionArgs, null, null, null);
        while (c.moveToNext()) {
            try {
                String json = c.getString(0);
                results.add(new JSONObject(json));
            } catch (JSONException e) {
                Log.w("could not parse json for entity, ignoring", e);
            }
        }
        c.close();
        return results;
    }
    
    
    public static JSONObject getEntity(SQLiteDatabase db, String type, int entityId) {
        JSONObject result = null;
        String[] columns = {"json"};
        String selection = "type=? and entity_id=?";
        String[] selectionArgs = {type, String.valueOf(entityId)};
        Cursor c = db.query("entities", columns, selection, selectionArgs, null, null, null);
        while (c.moveToNext()) {
            try {
                String json = c.getString(0);
                result = new JSONObject(json);
            } catch (JSONException e) {
                Log.w("could not parse json for entity, ignoring", e);
            }
        }
        c.close();
        return result;
    }
        
    public static Long getEntitiesUpdatedTime(SQLiteDatabase db) {
        Cursor c = db.query("entities_updated", new String[] {"time"}, null, null, null, null, null);
        Long l = null;
        while (c.moveToNext()) {
            l = c.getLong(0);
        }
        Log.d(TAG, "fetching entities updated time, it is "+l);
        c.close();
        return l;
    }
    
    public static void insertOrUpdateTweet(SQLiteDatabase db, long id, JSONObject data) {
        ContentValues values = new ContentValues();
        values.put("tweet_id", id);
        values.put("json", data.toString());
        db.insertWithOnConflict("tweets", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
    
    public static Long getMostRecentTweetId(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT MAX(tweet_id) FROM tweets", null);
        Long tweetId = null;
        while (c.moveToNext()) {
            tweetId = c.getLong(0);
        }
        c.close();
        return tweetId;
    }

    public static List<JSONObject> getTweets(SQLiteDatabase db) {
        List<JSONObject> results = new ArrayList<JSONObject>();
        Cursor c = db.query(false, "tweets", new String[] {"json"}, null, null, null, null, "tweet_id desc", "100");
        while (c.moveToNext()) {
            try {
                String json = c.getString(0);
                results.add(new JSONObject(json));
            } catch (JSONException e) {
                Log.w("TwitterActivity", e);
            }
        }
        c.close();
        return results;
    }

}
