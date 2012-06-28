package org.alpha.focus2012.rows;

import org.alpha.focus2012.Cell;
import org.alpha.focus2012.R;
import org.alpha.focus2012.Row;
import android.content.Context;
import android.view.View;
import android.widget.Button;


public class ButtonBarRow extends Row implements Cell {


    private static final class ButtonsViewHolder {
        Button button1;
        Button button2;
    }

    
    private String button1Title;
    private String button2Title;
    private View.OnClickListener button1OnClickListener;
    private View.OnClickListener button2OnClickListener;
    
    
    public ButtonBarRow(Context context) {
        super(context);
    }


    public void setButton1(String title, View.OnClickListener onClickListener) {
        this.button1Title = title;
        this.button1OnClickListener = onClickListener;
    }

    public void setButton2(String title, View.OnClickListener onClickListener) {
        this.button2Title = title;
        this.button2OnClickListener = onClickListener;
    }


    @Override
    public View getView(View convertView) {
        View rowView = convertView;
        if (rowView ==  null) {
            rowView = mInflater.inflate(R.layout.button_bar_list_item, null);
            ButtonsViewHolder holder = new ButtonsViewHolder();
            holder.button1 = (Button)rowView.findViewById(R.id.button1);
            holder.button2 = (Button)rowView.findViewById(R.id.button2);
            rowView.setTag(holder);
        }

        ButtonsViewHolder holder = (ButtonsViewHolder) rowView.getTag();
        
        holder.button1.setText(button1Title);
        holder.button1.setOnClickListener(button1OnClickListener);
        holder.button1.setEnabled(button1OnClickListener != null);
        holder.button1.setVisibility((button1Title == null ? View.GONE : View.VISIBLE));
        
        holder.button2.setText(button2Title);
        holder.button2.setOnClickListener(button2OnClickListener);
        holder.button2.setEnabled(button2OnClickListener != null);
        holder.button2.setVisibility((button2Title == null ? View.GONE : View.VISIBLE));
        
        return rowView;
    }
    
    
    @Override
    public Boolean isEnabled() {
        return false;
    }
    
}
