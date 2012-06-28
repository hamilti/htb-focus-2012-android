package org.alpha.focus2012;

import android.view.View;

public interface Cell {
    
    public View getView(View convertView);    
    public Boolean isEnabled();
}