package org.alpha.focus2012.twitter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import org.alpha.focus2012.Constants;
import org.alpha.focus2012.data.DBHelper;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;


public class TwitterActivity extends SherlockListActivity {


    private final ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {}
        public void onServiceConnected(ComponentName name, IBinder service) {}
    };


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.TWITTER_WAS_UPDATED_INTENT.equals(intent.getAction())) {
                populate();
            }
        }
    };

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();    
        mActionBar.setTitle("Twitter Stream");
        mActionBar.setDisplayHomeAsUpEnabled(true);
        setListAdapter(new TwitterAdapter(this));
        populate();
    }


    private void populate() {
        DBHelper h = null;
        SQLiteDatabase db = null;
        Log.d("Twitter", "activity start");
        try {
            h = new DBHelper(this);
            db = h.getWritableDatabase();

            List<Tweet> tweets = new ArrayList<Tweet>();
            for (JSONObject j : DBHelper.getTweets(db)) {
                tweets.add(new Tweet(j));
            }
            TwitterAdapter a = (TwitterAdapter) getListAdapter();
            a.setTweets(tweets);
            a.notifyDataSetChanged();

        } finally {
            if (db != null) db.close();
            if (h != null) h.close();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(Constants.TWITTER_WAS_UPDATED_INTENT));
        bindService(new Intent(this, TwitterPollService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onPause() {
        super.onPause();
        unbindService(serviceConnection);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

}
