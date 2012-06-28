package org.alpha.focus2012;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

public class AlphaAdapter extends BaseAdapter implements OnItemClickListener, SectionIndexer {
    private List<Cell> mRows = Collections.emptyList();
    private Boolean mShowSeperators;
    
    // section indexer
    private Boolean mShowAlphaIndex;
    private HashMap<String, Integer> alphaIndexer;
    private String[] indexSections;  
    
    
    public AlphaAdapter() {
        mShowAlphaIndex = false;
        mShowSeperators = true;
    }
    
    
    @Override
    public int getCount() {
        return mRows.size();
    }

    
    @Override
    public Object getItem(int position) {
        return mRows.get(position);
    }

    
    @Override
    public long getItemId(int position) {
        return position;
    }

    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cell row = mRows.get(position);
        return row.getView(null);
    }
    
    
    @Override
    public boolean areAllItemsEnabled() {
        return mShowSeperators;
    }
    
    
    @Override
    public boolean isEnabled(int position) {
        return mRows.get(position).isEnabled();
    }
    
    
    /**
     * Set the adapter's data source sections
     * @param sections
     */
    public void setSections(List<Section> sections) {
        mRows = new ArrayList<Cell>(); 
        
        if (mShowAlphaIndex) {
            alphaIndexer = new HashMap<String, Integer>(); 
        }
        
        int i = 0;
        for (Section section : sections) {
            if (section.getTitle() !=  null && section.getTitle().length() > 0) {
                mRows.add(section);

            }
            for (Row row : section.getRows()) {
                mRows.add(row);
                
                if(mShowAlphaIndex) {
                    alphaIndexer.put(row.indexerAlphaString(), i++);
                }
            }
        }
        
        if (mShowAlphaIndex) {
            // flip keys with values we should have got rid of duplicates using HasMap
            Set<String> sectionLetters = alphaIndexer.keySet();
            
            // convert set to ArrayList so we can sort it
            ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
            
            // sort
            Collections.sort(sectionList);
            
            // create new primitive array of strings from sectionList
            indexSections = new String[sectionList.size()];
            sectionList.toArray(indexSections);            
        }
        
        notifyDataSetChanged();
    }
    
    public void showSeperators(Boolean showSeperators) {
        mShowSeperators = showSeperators;
    }
    
    
    /**
     * Set the adapter's data source rows.
     * Use instead of setSections() when no sections are needed.
     * @param rows
     * @param context
     */
    public void setRows(List<Row> rows, Context context) {
        Section section = new Section(null, rows, context);
        List<Section> sections = new ArrayList<Section>();
        sections.add(section);
        setSections(sections);
    }
    
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cell cell = mRows.get(position);
        if (cell != null && cell instanceof Row) {
            Row row = (Row)cell;
            if (row.onClickListener() != null) {
                row.onClickListener().onRowClicked();
            }
        }
    }
    

    @Override
    public int getPositionForSection(int section) {
        return alphaIndexer.get(indexSections[section]);
    }
    

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    
    @Override
    public Object[] getSections() {
        return indexSections;
    }
    
    
    public void showAlphaIndex(Boolean showIndex) {
        mShowAlphaIndex = showIndex;
    }
}
