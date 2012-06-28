package org.alpha.focus2012.speakers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alpha.focus2012.AlphaAdapter;
import org.alpha.focus2012.Constants;
import org.alpha.focus2012.R;
import org.alpha.focus2012.Row;
import org.alpha.focus2012.Section;
import org.alpha.focus2012.data.DataStore;
import org.alpha.focus2012.data.Speaker;
import org.alpha.focus2012.rows.SpeakerRow;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockListFragment;


public class SpeakersFragment extends SherlockListFragment {
    

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.DATA_WAS_UPDATED_INTENT.equals(intent.getAction())) {
                populate();
            }
        }
    };

    
    private AlphaAdapter adapter;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new AlphaAdapter();
        adapter.showAlphaIndex(true);
        setListAdapter(adapter);
    }

    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnItemClickListener(adapter);
    }
    
    
    private void populate() {
        final Context context = getActivity();
        
        Map<String,List<Speaker>> speakersKeyedByLetter = new HashMap<String,List<Speaker>>();
        List<Speaker> allSpeakers = DataStore.speakers(context);
        for (Speaker s : allSpeakers) {
            String key = s.indexLetter();
            List<Speaker> l = speakersKeyedByLetter.get(key);
            if (l == null) {
                l = new ArrayList<Speaker>();
                speakersKeyedByLetter.put(key, l);
            }
            l.add(s);
        }
    
        List<String> sortedKeys = new ArrayList<String>(speakersKeyedByLetter.keySet());
        Collections.sort(sortedKeys);
        
        List<Section> sections = new ArrayList<Section>();
        for (String key : sortedKeys) {
            List<Row> rows = new ArrayList<Row>();
            for (final Speaker s : speakersKeyedByLetter.get(key)) {
                SpeakerRow row = new SpeakerRow(s, context);
                
                row.setOnClickListener(new Row.OnClickListener() {
                    @Override
                    public void onRowClicked() {
                        Intent intent = new Intent(context, SpeakerDetailActivity.class);
                        intent.putExtra(SpeakerDetailActivity.EXTRA_SPEAKER_ID, s.speakerId);
                        context.startActivity(intent);
                    }
                });
                
                rows.add(row);
            }
            sections.add(new Section(key, rows, context));
        }
        
        adapter.setSections(sections);
//        adapter.notifyDataSetChanged(); // not needed as this is automatically done by the adapter
    }

    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        getListView().setFastScrollEnabled(true);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        populate();
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(receiver, new IntentFilter(Constants.DATA_WAS_UPDATED_INTENT));
    }
    
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(receiver);
    }

    
}
