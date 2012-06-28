package org.alpha.focus2012.alerts;

import java.util.ArrayList;
import java.util.List;

import org.alpha.focus2012.AlphaAdapter;
import org.alpha.focus2012.R;
import org.alpha.focus2012.Row;
import org.alpha.focus2012.Row.OnClickListener;
import org.alpha.focus2012.data.Alert;
import org.alpha.focus2012.data.DataStore;
import org.alpha.focus2012.page.PageActivity;
import org.alpha.focus2012.rows.DetailRow;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListActivity;

public class AlertsActivity extends SherlockListActivity {
    private AlphaAdapter mAdapter;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list);
        
        mAdapter = new AlphaAdapter();
        setListAdapter(mAdapter);
        
        getListView().setOnItemClickListener(mAdapter);        
    }
    
    
    private void populate() {
        List<Alert> alerts = DataStore.alerts(this);        
        List<Row> rows = new ArrayList<Row>();
        final Context context = this;
        
        for (final Alert alert : alerts) {
            Row row = new DetailRow(alert.title, alert.dateTime.toString("HH:mm - dd MMMM YYYY"), null, this);
            row.setOnClickListener(new OnClickListener() {                
                @Override
                public void onRowClicked() {
                    Intent intent = new Intent(context, PageActivity.class);
                    intent.putExtra(PageActivity.TITLE, alert.title);
                    intent.putExtra(PageActivity.BODY, alert.message);
                    context.startActivity(intent);
                }
            });
            rows.add(row);
        }
        
        mAdapter.setRows(rows, this);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        populate();        
    }
}
