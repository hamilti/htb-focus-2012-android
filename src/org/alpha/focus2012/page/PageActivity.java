package org.alpha.focus2012.page;

import java.util.ArrayList;
import java.util.List;

import org.alpha.focus2012.AlphaAdapter;
import org.alpha.focus2012.Row;
import org.alpha.focus2012.rows.DetailRow;
import org.alpha.focus2012.rows.HTMLRow;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;

public class PageActivity extends SherlockListActivity {

    public static final String TITLE = "TITLE";
    public static final String BODY = "BODY";
    
    private ActionBar mActionBar;
    AlphaAdapter mAdapter;
    
    private String mTitle;
    private String mBody;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mTitle = getIntent().getStringExtra(TITLE);
        mBody = getIntent().getStringExtra(BODY);
        
        mActionBar = getSupportActionBar(); 
        mActionBar.setTitle(mTitle);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        
        mAdapter = new AlphaAdapter();
        mAdapter.showSeperators(false);
        setListAdapter(mAdapter);        
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        populate();
    }    

    
    private void populate() {
        List<Row> rows = new ArrayList<Row>();
        
        Row detailRow = new DetailRow(mTitle, null, null, this);
        rows.add(detailRow);
        
        Row htmlRow = new HTMLRow(mBody, this);
        rows.add(htmlRow);
        
        mAdapter.setRows(rows, this);
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
