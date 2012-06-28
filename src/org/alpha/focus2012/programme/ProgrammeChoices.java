package org.alpha.focus2012.programme;

import org.alpha.focus2012.data.Session;
import android.content.Context;
import android.content.SharedPreferences;


class ProgrammeChoices {

    private static final String PREFS_NAME = "ProgrammeChoices";

    private ProgrammeChoices() {}


    static boolean isSessionBookmarked(Context context, Session s) {
        Integer i = bookmarkedSessionForGroup(context, s.sessionGroupId);
        return i != null && i == s.sessionId;
    }

    static boolean doesSessionGroupHaveBookmark(Context context, int sessionGroupId) {
        return get(context, sessionGroupId) != null;
    }

    static Integer bookmarkedSessionForGroup(Context context, int sessionGroupId) {
        return get(context, sessionGroupId);
    }

    static void bookmarkSession(Context context, Session s) {
        set(context, s.sessionGroupId, s.sessionId);
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
