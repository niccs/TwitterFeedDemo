package surefire.org.twitterFeed.parser;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import surefire.org.twitterFeed.model.Tweet;
import surefire.org.twitterFeed.model.TwitterAuthToken;

/**
 * Created by HP on 3/04/2016.
 */
public class JSONParser {
    private static final String TAG = JSONParser.class.getName();

    public static ArrayList<Tweet> convertJsonToTwitterTweet(String twitterTweets) {
        Log.i(TAG,"inside convertJsonToTwitterTweet");
        ArrayList<Tweet> twitterTweetArrayList = null;
        if (twitterTweets != null && twitterTweets.length() > 0) {
            Gson gson = new Gson();
            twitterTweetArrayList =
                    gson.fromJson(twitterTweets, new TypeToken<ArrayList<Tweet>>() {
                    }.getType());
        }
        return twitterTweetArrayList;
    }

    public TwitterAuthToken convertJsonToTwitterAuthToken(String jsonAuth) {
        Log.i(TAG,"inside convertJsonToTwitterAuthToken");
        TwitterAuthToken twitterAuthToken = null;
        if (jsonAuth != null && jsonAuth.length() > 0) {
            Gson gson = new Gson();
            Log.d(TAG, jsonAuth);
            twitterAuthToken = gson.fromJson(jsonAuth, TwitterAuthToken.class);
        }
        return twitterAuthToken;
    }
}
