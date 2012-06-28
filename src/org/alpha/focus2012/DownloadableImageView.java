package org.alpha.focus2012;

import java.io.File;
import java.lang.ref.WeakReference;

import org.alpha.util.IO;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;


public class DownloadableImageView extends ImageView {

    private static final String TAG = "DownloadableImageView";
    
    
    private static final class Task extends AsyncTask<Void, Void, Bitmap> {
        private final Context context;
        private final String url;
        private final String cacheFilename;
        private final WeakReference<DownloadableImageView> imageViewRef;
        
        Task(Context context, String url, String cacheFilename, DownloadableImageView imageView) {
            this.context = context;
            this.url = url;
            this.cacheFilename = cacheFilename;
            this.imageViewRef = new WeakReference<DownloadableImageView>(imageView);
        }
        
        @Override
        protected Bitmap doInBackground(Void... params) {
            
            byte[] data = null;
            
            File f = new File(context.getCacheDir(), cacheFilename);
            if (f.exists()) {
                Log.d(TAG, "using cached version of "+url+" from "+cacheFilename);
                data = IO.readContentsOfFile(f);
                return BitmapFactory.decodeByteArray(data, 0, data.length);
            }
            
            if (data == null) {
                Log.d(TAG, "downloading "+url);
                data = IO.readContentsOfUrl(url);
            }
            
            if (data == null) {
                return null;
            }
            
            Log.d(TAG, "caching "+url);
            IO.writeContentsOfFile(data, f);
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        
        
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                //Log.d(TAG, "got image for position "+position+" with url "+url+" but task was cancelled");
                bitmap = null;
            }
            if (imageViewRef != null) {
                DownloadableImageView imageView = imageViewRef.get();
                if (imageView != null && imageView.task == this) {
                    //Log.d(TAG, "setting image for position "+position+" to url "+url);
                    imageView.setImageBitmap(bitmap);
                } else {
                    //Log.d(TAG, "ignoring image for position "+position+" because the task does not match");
                }
            } else {
                //Log.d(TAG, "got image for position "+position+" with url "+url+" but no ImageView existed");
            }
        }
    }
    
    
    private Task task = null;

    public DownloadableImageView(Context context) {
        super(context);
    }
    
    public DownloadableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public DownloadableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    
    public void setUrl(String url, String cacheFilename) {
        if (task != null) {
            if (!task.url.equals(url)) {
                //Log.d(TAG, "cancelling existing task for position "+position+" (was "+task.url+")");
                task.cancel(true);
            } else {
                //Log.d(TAG, "correct task already exists for position "+position+ "(is "+url+")");
                return;
            }
        }

        //Log.d(TAG, "creating new task for position "+position+" with url "+url);
        task = new Task(getContext(), url, cacheFilename, this);
        task.execute();
        setImageDrawable(new ColorDrawable(Color.LTGRAY));
    }

}
