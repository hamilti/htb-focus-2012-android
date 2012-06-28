package org.alpha.focus2012.rows;

import org.alpha.focus2012.DownloadableImageView;
import org.alpha.focus2012.R;
import org.alpha.focus2012.Row;
import org.alpha.focus2012.data.Speaker;
import org.alpha.focus2012.resources.Resource;
import android.content.Context;
import android.view.View;
import android.widget.TextView;


public class SpeakerRow extends Row {

    private final Speaker speaker;
    
    public SpeakerRow(Speaker speaker, Context context) {
        super(context);
        this.speaker = speaker;
    }
    
    
    @Override
    public String indexerAlphaString() {
        return speaker.indexLetter();
    }
    
    
    @Override
    public Boolean isEnabled() {
        return true;
    }
    
    
    @Override
    public View getView(View convertView) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = mInflater.inflate(R.layout.speaker_list_item, null);
            SpeakersViewHolder holder = new SpeakersViewHolder();
            holder.speakerTextView = (TextView)rowView.findViewById(R.id.title);
            holder.descriptionTextView = (TextView)rowView.findViewById(R.id.subTitle);
            holder.imageView = (DownloadableImageView)rowView.findViewById(R.id.image);
            rowView.setTag(holder);
        }
        
        SpeakersViewHolder holder = (SpeakersViewHolder)rowView.getTag();
        
        holder.speakerTextView.setText(speaker.displayName());
        holder.speakerTextView.setVisibility((speaker.displayName() == null ? View.GONE : View.VISIBLE));
        
        holder.descriptionTextView.setText(speaker.position);
        holder.descriptionTextView.setVisibility((speaker.position == null ? View.GONE : View.VISIBLE));
        
        Resource imageResource = new Resource(speaker.imageKey, Resource.Type.SpeakerImageSmall);
        holder.imageView.setUrl(imageResource.url(), imageResource.cacheFilename());
        
        return rowView;
    }
    
    
    private static class SpeakersViewHolder {
        TextView speakerTextView;
        TextView descriptionTextView;
        DownloadableImageView imageView;
    }    
}