package org.alpha.focus2012.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.alpha.focus2012.Constants;
import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


public class DownloadService extends IntentService {
    
    private static final String TAG = "DownloadService";
        
    public DownloadService() {
        super("DownloadService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        DBHelper h = null;
        SQLiteDatabase db = null;
        Log.d(TAG, "Start intent");
        try {
            h = new DBHelper(this);
            db = h.getWritableDatabase();

            final Long lastUpdatedTime = DBHelper.getEntitiesUpdatedTime(db);

            if (lastUpdatedTime == null) {
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.SHOW_LOADING_MESSAGE_INTENT));
            }
            
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            String timestamp = (lastUpdatedTime != null) ? df.format(new Date(lastUpdatedTime * 1000L)) : "0";
            String url = "http://acs.alpha.org/api/rest/v1/conferences/getObjects/" + Constants.CONFERENCE_ID + "/" + timestamp;
            JSONObject newData = fetch(url);

            if (newData != null) {
                populateDatabase(db, newData);
            }
            
            if (lastUpdatedTime == null) {
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.HIDE_LOADING_MESSAGE_INTENT));
            }
            
            if (newData == null && lastUpdatedTime == null) {
                // got no data at all
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.SHOW_OFFLINE_INTENT));
                return;
            }
            
            // inform the user
            Log.d(TAG, "got updated data");
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.DATA_WAS_UPDATED_INTENT));
            
        } finally {
            if (db != null) db.close();
            if (h != null) h.close();
        }
    }

    
    private static JSONObject fetch(String url) {
        Log.d(TAG, "downloading "+url);
        
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(TAG, "Could not download data");
            }
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Could not download data", e);
        } catch (IOException e) {
            Log.e(TAG, "Could not download data", e);
        }
        
        if (builder.length() > 0) {
            try {
                JSONObject o = new JSONObject(builder.toString());
                return o;
                
            } catch (JSONException e) {
                Log.e(TAG, "Could not parse data", e);
            }
        }
        
        return null;
    }

    
    private void populateDatabase(SQLiteDatabase db, JSONObject o) {
        try {
            JSONObject body = o.getJSONObject("body");
        
            for (Iterator i = body.keys(); i.hasNext(); ) {
                String key = (String) i.next();
                Object values = body.get(key);
                if (values instanceof JSONArray) {
                    // multiple entities
                    JSONArray entities = (JSONArray) values;
                    for (int x=0; x<entities.length(); x++) {
                        JSONObject entityDict = entities.getJSONObject(x);
                        populateEntity(db, key, entityDict);
                    }
                    
                } else if (values instanceof JSONObject) {
                    // single entity
                    JSONObject entityDict = (JSONObject) values;
                    populateEntity(db, key, entityDict);
                    
                }
            }
            
            long time = body.optLong("request_timestamp");
            if (time > 0) {
                DBHelper.setEntitiesUpdatedTime(db, time);
            }
            
        } catch (JSONException e) {
            Log.e(TAG, "could not parse json", e);
        }
    }

    
    private void populateEntity(SQLiteDatabase db, String key, JSONObject entity) {
        int entityId = entity.optInt("id");
        if (entityId == 0) {
            return;
        }
        boolean active = entity.optInt("active", 1) > 0;
        if (active) {
            Log.d(TAG, "adding entity with id "+entityId+" to "+key);
            DBHelper.insertOrUpdateEntity(db, key, entityId, entity.toString());
        } else {
            Log.d(TAG, "removing entity with id "+entityId+" to "+key);
            DBHelper.deleteEntity(db, key, entityId);
        }
    }
    
    
}
