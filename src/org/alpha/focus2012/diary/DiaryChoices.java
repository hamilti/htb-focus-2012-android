package org.alpha.focus2012.diary;

import java.util.Calendar;
import org.alpha.focus2012.data.Session;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


class DiaryChoices {

    private static final String PREFS_NAME = "DiaryChoices";
	private static final int HELLO_ID = 1;

    private DiaryChoices() {}

    static boolean isSessionBookmarked(Context context, Session s) {
    	
        Integer i = get(context, s.sessionId);       
        if (i != null && i == s.sessionId) {
          return true;
        } else {
          return false;
        }
    }
    
    static boolean doesSessionGroupHaveBookmark(Context context, int sessionGroupId) {
        return get(context, sessionGroupId) != null;
    }

    static Integer bookmarkedSessionForGroup(Context context, int sessionGroupId) {
        return get(context, sessionGroupId);
    }

    static void bookmarkSession(Context context, Session s) {
    	Log.d("DiaryChoices", "BOOKMARK! sessionId:" + Integer.toString(s.sessionId));
        set(context, s.sessionId, s.sessionId);
    }

    static void unbookmarkSession(Context context, Session s) {
    	Log.d("DiaryChoices", "UNBOOKMARK! sessionId:" + Integer.toString(s.sessionId));
        set(context, s.sessionId, 0);
    }
    
    static void setAlarmSession(Context context, Session s) {
    	Log.d("DiaryChoices", "Set Alarm");
    	
    	Calendar cal = Calendar.getInstance();       
    	 
		// add minutes to the calendar object
		cal.set(Calendar.MONTH, 6);
		cal.set(Calendar.YEAR, 2012);				
		cal.set(Calendar.DAY_OF_MONTH, 30);
		cal.set(Calendar.HOUR_OF_DAY, 11);
		cal.set(Calendar.MINUTE, 8);
		
		//cal.set will set the alarm to trigger exactly at: 21:43, 5 May 2011
		//if you want to trigger the alarm after let's say 5 minutes after is activated you need to put
		//cal.add(Calendar.MINUTE, 5);
		Intent alarmintent = new Intent(context, AlarmReceiver.class);
		alarmintent.putExtra("title", s.name);
		alarmintent.putExtra("note","Focus 2012 Notification");
		
		PendingIntent sender = PendingIntent.getBroadcast(context, HELLO_ID, alarmintent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);
		//VERY IMPORTANT TO SET FLAG_UPDATE_CURRENT... this will send correct extra's informations to 
		//AlarmReceiver Class
		//Get the AlarmManager service
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
    	
    }
    
    
    private static void set(Context context, int key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(String.valueOf(key), value);
        editor.commit();
    }

    private static Integer get(Context context, int key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int i = settings.getInt(String.valueOf(key), 0);
        return (i != 0) ? i : null;
    }

}
