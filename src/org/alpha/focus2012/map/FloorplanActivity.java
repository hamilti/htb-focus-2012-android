package org.alpha.focus2012.map;

import org.alpha.focus2012.R;
import org.alpha.focus2012.resources.Resource;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;


public class FloorplanActivity extends SherlockActivity {

    public static final String FLOORPLAN_KEY = "FLOORPLAN_KEY";
    
    private ActionBar mActionBar;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        String floorplanKey = getIntent().getStringExtra(FLOORPLAN_KEY);
        Resource pdfResource = new Resource(floorplanKey, Resource.Type.VenueFloorplan);
        
        mActionBar = getSupportActionBar();    
        mActionBar.setTitle(getString(R.string.floorplan_activity_title));
        mActionBar.setDisplayHomeAsUpEnabled(true);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        
        WebView webView = new WebView(this);
        webView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
            
        });
        layout.addView(webView);
        
        setContentView(layout);        
        
        Log.v("***", pdfResource.url());
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfResource.url());        
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
}
