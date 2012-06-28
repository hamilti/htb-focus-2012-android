package org.alpha.focus2012.twitter;

import java.util.Collections;
import java.util.List;

import org.alpha.focus2012.DownloadableImageView;
import org.alpha.focus2012.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


class TwitterAdapter extends BaseAdapter {

    private static final class ViewHolder {
        DownloadableImageView imageView;
        TextView userView;
        TextView textView;
        TextView dateView;
    }


    private List<Tweet> tweets = Collections.emptyList();
    private Context context;


    public TwitterAdapter(Context context) {
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Log.d("Twitter", "getView");
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.twitter_list_item, null);
            ViewHolder holder = new ViewHolder();
            holder.imageView = (DownloadableImageView) view.findViewById(R.id.tweet_image);
            holder.userView = (TextView) view.findViewById(R.id.tweet_user);
            holder.textView = (TextView) view.findViewById(R.id.tweet_text);
            holder.dateView = (TextView) view.findViewById(R.id.tweet_date);
            view.setTag(holder);
        }
        bindView(position, view);
        return view;
    }


    private void bindView(int position, View view) {
        Tweet tweet = getItem(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.imageView.setUrl(tweet.avatar.url(), tweet.avatar.cacheFilename());
        holder.userView.setText(tweet.name);
        holder.textView.setText(tweet.text);
        holder.dateView.setText(tweet.time);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Tweet getItem(int position) {
        return tweets.get(position);
    }

    @Override
    public int getCount() {
        return tweets.size();
    }

    void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

}
