package surefire.org.twitterFeed.adapter;

/**
 * Created by HP on 3/04/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import surefire.org.twitterFeed.R;
import surefire.org.twitterFeed.common.AppUtil;
import surefire.org.twitterFeed.common.ApplicationSettings;
import surefire.org.twitterFeed.model.Tweet;

public class TweetListAdapter extends ArrayAdapter<Tweet> {
    Context mContext;
    Bitmap profileImage;
    private static final String TAG = TweetListAdapter.class.getName();

    public TweetListAdapter(Activity activity, List<Tweet> tweets) {
        super(activity, 0, tweets);
        mContext = activity;

        profileImage = AppUtil.decodeBase64(ApplicationSettings.getProfileImage(mContext));

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "inside adapter ++++++++++++++++++++");
        Activity activity = (Activity) getContext();
        LayoutInflater inflater = activity.getLayoutInflater();

        // Inflate the views from XML
        View rowView = inflater.inflate(R.layout.row_page_tweet, null);
        Tweet tweet = getItem(position);


        // Set the text on the TextView
        TextView tweetUserName = (TextView) rowView.findViewById(R.id.textUserName);
        TextView tweetAttributesTextView = (TextView) rowView.findViewById(R.id.tweetAttributes);
        ImageView imgView = (ImageView) rowView.findViewById(R.id.profile_image);
        TextView tweetTextView = (TextView) rowView.findViewById(R.id.tweetText);
        imgView.setImageBitmap(profileImage);

        String tweetText = tweet.getText();
        String date = tweet.getCreatedAt();
        try {
            SimpleDateFormat createdDateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss Z yyyy", Locale.ENGLISH);
            Date dateObj = createdDateFormat.parse(date);
            createdDateFormat = new SimpleDateFormat("MMM dd,yy hh:mm a");
            date = createdDateFormat.format(dateObj);
            Log.d(TAG, dateObj.toString() + "   " + date);


        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        String screenName = tweet.getTwitterUser().getScreenName();
        String name = tweet.getTwitterUser().getName();
        tweetUserName.setText(name);
        tweetAttributesTextView.setText(" " + "@" + screenName + " " + date);
        tweetTextView.setText(tweetText);


        return rowView;

    }

}
