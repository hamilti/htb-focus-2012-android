package org.alpha.focus2012.more;

import org.alpha.focus2012.R;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {
   private VideoView mVideoView;
   
   private static String sURL = "s3-eu-west-1.amazonaws.com/distribution.media.alpha.org/10135/MB.mp4";
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);

           setContentView(R.layout.videoview);

           VideoView videoView = (VideoView) findViewById(R.id.VideoView);
           MediaController mediaController = new MediaController(this);
           mediaController.setAnchorView(videoView);

           //Set video link (mp4 format )
           Uri video = Uri.parse(sURL);
           videoView.setMediaController(mediaController);

           videoView.setVideoURI(video);
           videoView.start();
   }
}
