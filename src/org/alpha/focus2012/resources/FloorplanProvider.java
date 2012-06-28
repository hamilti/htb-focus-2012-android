package org.alpha.focus2012.resources;

import java.io.File;
import java.io.IOException;

import org.alpha.util.IO;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;


public class FloorplanProvider extends ContentProvider {
    
    private static final String TAG = "FloorplanProvider";
    

    @Override
    public boolean onCreate() {
        return true;
    }
    
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sort) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues _initialValues) {
        throw new RuntimeException("can't insert");
    }
    
    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        throw new RuntimeException("can't update");
    }
    
    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        throw new RuntimeException("can't delete");
    }
    
    
    // TODO: if the file is already in the data directory, don't copy it again
    
    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) {
        try {
            String key = uri.getLastPathSegment();
            Resource resource = new Resource(key, Resource.Type.VenueFloorplan);
            Log.i(TAG, "open PDF: " + resource.key);
            
            File f = new File("data/data/"+getContext().getPackageName()+"/files/"+key+".pdf");
            f.getParentFile().mkdirs();
            if (!f.exists()) {
                Log.d(TAG, "downloading "+resource.url());
                byte[] data = IO.readContentsOfUrl(resource.url());
                IO.writeContentsOfFile(data, f);
            }
            
            Log.d(TAG, "returning ParcelFileDescriptor for "+f+" "+f.exists());
            return ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_ONLY);
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    
    @Override
    public String getType(Uri uri) {
        return "application/pdf";
    }

}
