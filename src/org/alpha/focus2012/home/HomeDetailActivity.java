package org.alpha.focus2012.home;

import org.alpha.focus2012.DownloadableImageView;
import org.alpha.focus2012.R;
import org.alpha.focus2012.data.Conference;
import org.alpha.focus2012.data.DataStore;
import org.alpha.focus2012.resources.Resource;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class HomeDetailActivity extends SherlockActivity {
    public static String CONFERENCE_ID = "CONFERENCE_ID";
    private ActionBar mActionBar;
    private Conference mConference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int conferenceId = getIntent().getIntExtra(CONFERENCE_ID, 0);
        mConference = DataStore.conference(this, conferenceId);
        
        mActionBar = getSupportActionBar(); 
        mActionBar.setTitle(mConference.name);
        mActionBar.setDisplayHomeAsUpEnabled(true);        
        
        setContentView(R.layout.event);
        
        DownloadableImageView imageView = (DownloadableImageView)findViewById(R.id.imageView);
        TextView descriptionTextView = (TextView)findViewById(R.id.description);
        Button bookButton = (Button)findViewById(R.id.bookButton);
     
        Resource imageResource = new Resource(mConference.imageKey, Resource.Type.ConferenceImage);
        imageView.setUrl(imageResource.url(), imageResource.cacheFilename());
        
        descriptionTextView.setText(Html.fromHtml(mConference.text));
        
        bookButton.setVisibility((mConference.bookingUrl.length() > 0? View.VISIBLE : View.GONE));
        bookButton.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mConference.bookingUrl));
                startActivity(intent);
            }
        });
    }
}
