package org.alpha.focus2012;

import java.util.List;

public class Page {
    private final String mTitle;
    private final List<Section> mSections;
    
    public Page(String title, List<Section> sections) {
        mTitle = title;
        mSections = sections;
    }
    
    public String getTitle() {
        return mTitle;
    }
    
    public List<Section> getSections() {
        return mSections;
    }    
}
