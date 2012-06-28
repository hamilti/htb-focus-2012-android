package org.alpha.focus2012.twitter;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.alpha.focus2012.Constants;
import org.alpha.focus2012.data.DBHelper;
import org.alpha.util.JSON;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


public class TwitterPollService extends Service {
    
    private static final String TAG = "TwitterPollService";
    
    private Handler handler = null;

    
    private Runnable task = new Runnable() {    	
        @Override
        public void run() {
            (new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    update();
                    return null;
                }
                
                protected void onPostExecute(Void result) {
                    if (handler != null) {
                        handler.postDelayed(task, Constants.TWITTER_UPDATE_FREQUENCY * 1000);
                    }
                }
                
            }).execute();
        }
    };


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        handler = new Handler();
        handler.post(task);
    }

    
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        handler.removeCallbacks(task);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    
    private void update() {
        DBHelper h = null;
        SQLiteDatabase db = null;
        Log.d(TAG, "Update: " + Constants.TWITTER_SEARCH_TERM);
        try {
            h = new DBHelper(this);
            db = h.getWritableDatabase();

            Long mostRecentTweetId = DBHelper.getMostRecentTweetId(db);
            
            // download new tweets
            String queryString = "q="+URLEncoder.encode(Constants.TWITTER_SEARCH_TERM, "US-ASCII");
            if (mostRecentTweetId != null) {
                queryString += "&since_id="+mostRecentTweetId;
            }
            
            String url = "http://search.twitter.com/search.json?" + queryString;
            JSONObject newData = JSON.loadFromUrl(url);
            
            if (newData == null) {
                return;
            }
            
            populateDatabase(db, newData);

            // inform the user
            Log.d(TAG, "got updated data");
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Constants.TWITTER_WAS_UPDATED_INTENT));
            
        } catch (Exception e) {
            Log.e(TAG, "unable to get twitter data", e);
            
        } finally {
            if (db != null) db.close();
            if (h != null) h.close();
        }

    }
    
    
    private static void populateDatabase(SQLiteDatabase db, JSONObject o) {
        try {
            JSONArray results = o.getJSONArray("results");
            Log.d(TAG, "got "+results.length()+" results");
            for (int x=0; x<results.length(); x++) {
                JSONObject result = results.getJSONObject(x);
                long id = result.getLong("id");
                Log.d(TAG, "saving tweet "+id);
                DBHelper.insertOrUpdateTweet(db, id, result);
            }
        } catch (JSONException e) {
            Log.e(TAG, "could not parse json", e);
        }
    }
    
}
