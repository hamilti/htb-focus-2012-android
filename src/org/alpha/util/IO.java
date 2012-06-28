package org.alpha.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public final class IO {
    
    private static final int BUFFER_SIZE = 4096;
    
    private IO() {}
    
    
    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        int c;
        while ((c = in.read(buf)) > -1) {
            out.write(buf, 0, c);
        }
    }
    
    public static byte[] readIntoByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        IO.copy(in, bout);
        return bout.toByteArray();
    }
    
    public static void writeFromByteArray(byte[] b, OutputStream out) throws IOException {
        ByteArrayInputStream bin = new ByteArrayInputStream(b);
        IO.copy(bin, out);
    }
    
    
    public static byte[] readContentsOfFile(File f) {
        FileInputStream fin = null;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            fin = new FileInputStream(f);
            IO.copy(fin, bout);
            return bout.toByteArray();
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (fin != null) fin.close();
            } catch (IOException e) {}
        }
    }

    
    public static void writeContentsOfFile(byte[] data, File f) {
        FileOutputStream fout = null;
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        try {
            fout = new FileOutputStream(f);
            IO.copy(bin, fout);
        } catch (IOException e) {
            // ignore
        } finally {
            try {
                if (fout != null) fout.close();
            } catch (IOException e) {}
        }
    }
    
    
    public static byte[] readContentsOfUrl(String url) {
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream in = null;
                try {
                    in = entity.getContent();
                    return IO.readIntoByteArray(in);
                } finally {
                    if (in != null) in.close();
                    entity.consumeContent();
                }
            } else {
                //Log.e(TAG, "Could not download image");
            }
        } catch (Exception e) {
            //Log.e(TAG, "Could not download data", e);
            httpGet.abort();
        }
        return null;
    }
    

}
