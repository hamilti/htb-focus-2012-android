package org.alpha.focus2012.more;

import org.alpha.focus2012.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {
   private VideoView mVideoView; 
   
   private static String sURL = "http://static.alpha.org.s3.amazonaws.com/mobile-apps/video/MB.mp4";
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.videoview);
           VideoView videoView = (VideoView) findViewById(R.id.VideoView);
           MediaController mediaController = new MediaController(this);
           mediaController.setMediaPlayer(videoView);
           videoView.setVideoPath(sURL);
           videoView.setMediaController(mediaController);
           videoView.requestFocus();
           videoView.start();
           mediaController.show();
           
   }
}
