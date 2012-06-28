package org.alpha.focus2012.diary;

import org.alpha.focus2012.data.Session;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


class DiaryChoices {

    private static final String PREFS_NAME = "DiaryChoices";

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
