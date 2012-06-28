package org.alpha.focus2012;

import android.content.Context;
import android.view.LayoutInflater;

public abstract class Row implements Cell {    
    protected static LayoutInflater mInflater;
    protected Context mContext;
    protected OnClickListener mClickListener;
    
    protected Row(Context context) {
        mContext = context;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);        
    }
    
    protected String indexerAlphaString() {
        return null;
    }
    
    public void setOnClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }
    
    public OnClickListener onClickListener() {
        return mClickListener;
    }
    
    public interface OnClickListener {
        public void onRowClicked();
    }
}