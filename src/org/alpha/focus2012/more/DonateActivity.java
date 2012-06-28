package org.alpha.focus2012.more;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.alpha.focus2012.AlphaAdapter;
import org.alpha.focus2012.Constants;
import org.alpha.focus2012.Row;
import org.alpha.focus2012.data.Conference;
import org.alpha.focus2012.data.DataStore;
import org.alpha.focus2012.rows.ButtonBarRow;
import org.alpha.focus2012.rows.HTMLRow;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;


public class DonateActivity extends SherlockListActivity {
    
    private ActionBar mActionBar;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar = getSupportActionBar(); 
        mActionBar.setTitle("Donate");
        mActionBar.setDisplayHomeAsUpEnabled(true);

        AlphaAdapter adapter = new AlphaAdapter();
        setListAdapter(adapter);
    }
    

    @Override
    protected void onResume() {
        super.onResume();
        populate();
    }


    private void populate() {
        final Conference conference = DataStore.conference(this, Constants.CONFERENCE_ID);

        List<Row> rows = new ArrayList<Row>();
        rows.add(new HTMLRow(conference.donationDescription, this));

        View.OnClickListener donateBySmsHandler = null;
        if (StringUtils.isNotBlank(conference.donationTelephoneNumber)) {
            donateBySmsHandler = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:"+conference.donationTelephoneNumber));
                    //intent.putExtra("sms_body", "HLCG12");
                    startActivity(intent);
                }
            };
        }

        View.OnClickListener donateOnlineHandler = null;
        if (StringUtils.isNotBlank(conference.donationUrl)) {
            donateOnlineHandler = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(conference.donationUrl)));
                }
            };
        }

        ButtonBarRow buttons = new ButtonBarRow(this);
        buttons.setButton1("Donate by SMS", donateBySmsHandler);
        buttons.setButton2("Donate online", donateOnlineHandler);
        rows.add(buttons);

        ((AlphaAdapter) getListAdapter()).setRows(rows, this);
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
