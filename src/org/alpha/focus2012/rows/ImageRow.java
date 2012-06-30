package org.alpha.focus2012.rows;

import org.alpha.focus2012.Cell;
import org.alpha.focus2012.DownloadableImageView;
import org.alpha.focus2012.R;
import org.alpha.focus2012.Row;
import org.alpha.focus2012.resources.Resource;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class ImageRow extends Row implements Cell {
	
    private final String mTitle;
    private final String mSubTitle;
    public final Resource mImageResource;
    
    public ImageRow(String title, String subTitle, Resource imageResource, Context context) {
        super(context);
        mTitle = title;
        mSubTitle = subTitle;
        mImageResource = imageResource;
    }
    
	@Override
    public Boolean isEnabled() {
        return mClickListener != null;
    }    

    @Override
    public View getView(View convertView) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = mInflater.inflate(R.layout.image_list_item, null);
            ImageViewHolder holder = new ImageViewHolder();
            holder.titleTextView = (TextView)rowView.findViewById(R.id.titleTextView);
            holder.subTitleTextView = (TextView)rowView.findViewById(R.id.subTitleTextView);
            holder.imageView = (DownloadableImageView)rowView.findViewById(R.id.imageView);
            rowView.setTag(holder);
        }
        
        ImageViewHolder holder = (ImageViewHolder)rowView.getTag();
        
        
        holder.titleTextView.setText(mTitle);
        holder.titleTextView.setVisibility((mTitle == null ? View.GONE : View.VISIBLE));
        
        holder.subTitleTextView.setText(mSubTitle);
        holder.subTitleTextView.setVisibility((mSubTitle == null ? View.GONE : View.VISIBLE));
        
        if (mImageResource != null) {
            holder.imageView.setUrl(mImageResource.url(), mImageResource.cacheFilename());
            holder.imageView.setVisibility(View.VISIBLE);
        }
        else {
            holder.imageView.setVisibility(View.GONE);
        }
        return rowView;
    }
    
    private static class ImageViewHolder {
    	TextView titleTextView;
        TextView subTitleTextView;
        DownloadableImageView imageView;        
    }    
}
