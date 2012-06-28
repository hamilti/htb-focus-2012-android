package org.alpha.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.util.Log;


public final class JSON {
    
    public static enum DateIntepretation {
        SECONDS_SINCE_1970,
        TWITTER
    }


    private static final String TAG = "JSON";
    
    private JSON() {}


    public static final String getString(JSONObject j, String name) {
        return j.isNull(name) ? null : j.optString(name);
    }

    public static final LocalDate getLocalDate(JSONObject j, String name, DateIntepretation di, DateTimeZone tz) {
        return getLocalDateTime(j, name, di, tz).toLocalDate();
    }
    
    public static final LocalDateTime getLocalDateTime(JSONObject j, String name, DateIntepretation di, DateTimeZone tz) {
        switch (di) {
        case SECONDS_SINCE_1970:
            return j.isNull(name) ? null : new LocalDateTime(j.optLong(name) * 1000L, tz);
        case TWITTER:
            String str = j.optString(name);
            if (str != null) {
                DateTimeFormatter f = DateTimeFormat.forPattern("EEE, d MMM yyyy HH:mm:ss Z");
                return f.parseDateTime(str).toLocalDateTime();
            } else {
                return null;
            }
        default:
            throw new RuntimeException("unknown DateIntepretation");
        }
    }

    public static final Integer getColor(JSONObject j, String name) {
        try {
            return Color.parseColor("#" + JSON.getString(j, "colour"));
        } catch (Exception e) {
            return null;
        }
    }

    
    public static JSONObject loadFromUrl(String url) {
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
            Log.w(TAG, "Could not download data", e);
        } catch (IOException e) {
            Log.w(TAG, "Could not download data", e);
        }

        if (builder.length() > 0) {
            try {
                JSONObject o = new JSONObject(builder.toString());
                return o;

            } catch (JSONException e) {
                Log.w(TAG, "Could not parse data", e);
            }
        }

        return null;
    }
    
}
