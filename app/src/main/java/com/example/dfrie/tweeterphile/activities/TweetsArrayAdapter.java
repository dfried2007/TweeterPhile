package com.example.dfrie.tweeterphile.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dfrie.tweeterphile.R;
import com.example.dfrie.tweeterphile.restclient.models.Tweet;
import com.example.dfrie.tweeterphile.utils.TwitterUtils;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import static com.example.dfrie.tweeterphile.R.id.imageView;
import static com.example.dfrie.tweeterphile.R.id.tvBody;
import static com.example.dfrie.tweeterphile.R.id.tvComment;
import static com.example.dfrie.tweeterphile.R.id.tvTimestamp;
import static com.example.dfrie.tweeterphile.R.id.tvTimestamp2;
import static com.example.dfrie.tweeterphile.R.id.tvUserName;

/**
 * Created by dfrie on 3/24/2017.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private final Context context;

    // View lookup cache
    private static class ViewHolder {
        public ImageView imageView ;
        public TextView tvBody;
        public TextView tvUserName;
        public TextView tvScreenName;
        public TextView tvTimestamp;
        public TextView tvTimestamp2;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.item_tweet_view, tweets);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Tweet tweet = getItem(position);
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet_view, parent, false);
            viewHolder.imageView = (ImageView)convertView.findViewById(imageView);
            viewHolder.tvBody = (TextView)convertView.findViewById(tvBody);
            viewHolder.tvUserName = (TextView)convertView.findViewById(tvUserName);
            viewHolder.tvScreenName = (TextView)convertView.findViewById(tvComment);
            viewHolder.tvTimestamp = (TextView)convertView.findViewById(tvTimestamp);
            viewHolder.tvTimestamp2 = (TextView)convertView.findViewById(tvTimestamp2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvUserName.setText(tweet.getUser().getName());
        viewHolder.tvScreenName.setText("(@" + tweet.getUser().getScreenName() + ")");

        int pos = tweet.getTimestamp().indexOf(':') + 3;
        viewHolder.tvTimestamp.setText(tweet.getTimestamp().substring(0,10));
        viewHolder.tvTimestamp2.setText(tweet.getTimestamp().substring(10,pos));

        Date d = TwitterUtils.parseTwitterDate(tweet.getTimestamp());
        Date now = new Date();
        if (d != null && now.getTime()-d.getTime() < DateUtils.WEEK_IN_MILLIS) {
            CharSequence chars = DateUtils.getRelativeDateTimeString(
                    context, d.getTime(),
                    DateUtils.MINUTE_IN_MILLIS, // The resolution. This will display only minutes
                    DateUtils.WEEK_IN_MILLIS, // The maximum resolution at which the time will switch
                    0); // Eventual flags
            viewHolder.tvTimestamp.setText(chars);
            viewHolder.tvTimestamp2.setVisibility(View.GONE);
        } else {
            viewHolder.tvTimestamp2.setVisibility(View.VISIBLE);
        }

        // This is for the click through to a profile...
        viewHolder.imageView.setTag(tweet.getUser().getScreenName());

        viewHolder.imageView.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .into(viewHolder.imageView);
        return convertView;
    }
}
