package org.alpha.focus2012.more;

import org.alpha.focus2012.Constants;
import org.alpha.focus2012.R;
import org.alpha.focus2012.alerts.AlertsActivity;
import org.alpha.focus2012.events.EventDetailActivity;
import org.alpha.focus2012.events.EventsActivity;
import org.alpha.focus2012.faqs.FaqsActivity;
import org.alpha.focus2012.offers.OffersActivity;
import org.alpha.focus2012.rows.MoreRow;
import org.alpha.focus2012.twitter.TwitterActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public class MoreFragment extends SherlockListFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        MoreRow twitterRow = new MoreRow();
        twitterRow.title = getString(R.string.twitter_menu_title);
        twitterRow.iconImageResource =  R.drawable.twitter;
        twitterRow.onClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TwitterActivity.class));
            }
        };
        
        MoreRow videoRow = new MoreRow();
        videoRow.title = getString(R.string.video_menu_title);
        videoRow.iconImageResource =  R.drawable.about;
        videoRow.onClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VideoActivity.class));
            }
        };
        
       /*
       MoreRow aboutRow = new MoreRow();
       aboutRow.title = getString(R.string.about_menu_title);        
       aboutRow.iconImageResource = R.drawable.about;
       aboutRow.onClickListener = new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                intent.putExtra(EventDetailActivity.CONFERENCE_ID, Constants.CONFERENCE_ID);
                startActivity(intent);                
            }
        };
        */
        
        
       // MoreRow donateRow = new MoreRow();
       // donateRow.title = getString(R.string.donate_menu_title);
       // donateRow.iconImageResource =  R.drawable.donate;
       // donateRow.onClickListener = new View.OnClickListener() {
       //     @Override
       //     public void onClick(View v) {
       //         Intent intent = new Intent(v.getContext(), DonateActivity.class);
       //        v.getContext().startActivity(intent);
       //     }
       // };
        
        MoreRow alertRow = new MoreRow();
        alertRow.title = getString(R.string.alerts_menu_title);
        alertRow.iconImageResource =  R.drawable.alerts;
        alertRow.onClickListener = new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AlertsActivity.class);
                v.getContext().startActivity(intent);                
            }
        };
        
        MoreRow faqsRow = new MoreRow();
        faqsRow.title = getString(R.string.faqs_menu_title);
        faqsRow.iconImageResource =  R.drawable.faqs;
        faqsRow.onClickListener = new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FaqsActivity.class);
                v.getContext().startActivity(intent);                
            }
        };        
        
        MoreRow otherRow = new MoreRow();
        otherRow.title = getString(R.string.other_events_menu_title);
        otherRow.iconImageResource =  R.drawable.other_events;
        otherRow.onClickListener = new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EventsActivity.class);
                v.getContext().startActivity(intent);                
            }
        };            
        
        MoreRow specialOffersRow = new MoreRow();
        specialOffersRow.title = getString(R.string.special_offers_menu_title);
        specialOffersRow.iconImageResource =  R.drawable.special_offers;    
        specialOffersRow.onClickListener = new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OffersActivity.class);
                v.getContext().startActivity(intent);                
            }
        };
        
        
        MoreRow[] listItems  = {
                twitterRow,
                videoRow,
                //aboutRow,
                //donateRow,
                alertRow,
                faqsRow,
                otherRow,
                specialOffersRow
        };
        
        MoreListAdapter listAdapter = new MoreListAdapter(getActivity(), listItems);
        setListAdapter(listAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        MoreRow row = ((MoreListAdapter) l.getAdapter()).getItem(position);
        if (row.onClickListener != null) {
            row.onClickListener.onClick(v);
        }
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, container, false);
    }

    
    @Override
    public void onPause() {
        super.onPause();
    }
    
}