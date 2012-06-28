package org.alpha.focus2012;


public final class Constants {

    public static final String DATA_WAS_UPDATED_INTENT = "DATA_WAS_UPDATED_INTENT";
    public static final String TWITTER_WAS_UPDATED_INTENT = "TWITTER_WAS_UPDATED_INTENT";
    public static final String SHOW_LOADING_MESSAGE_INTENT = "SHOW_LOADING_MESSAGE_INTENT";
    public static final String HIDE_LOADING_MESSAGE_INTENT = "HIDE_LOADING_MESSAGE_INTENT";
    public static final String SHOW_OFFLINE_INTENT = "SHOW_OFFLINE_INTENT";

    public static final int CONFERENCE_ID = 2;
    
    public static final String DATABASE_NAME = "focus2012";
    public static final int DATABASE_VERSION = 1;
    
    public static final String TWITTER_SEARCH_TERM = "#focus2012";
    public static final int TWITTER_UPDATE_FREQUENCY = 40;  // seconds

    // android debug key
    public static final String mapsAPIKey = "0QEZYsIrsaAtPTcDHDWrgWlbbf7juWE8ZpyS-KQ";

    //ACS Server location - changes this for prod!
    public static final String ACS_SERVER_URL = "acs.alpha.org";
    //public static final String ACS_SERVER_URL = "acs.test.alpha.org";
    
    private Constants() {}

}
