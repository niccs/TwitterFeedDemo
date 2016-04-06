package surefire.org.twitterFeed.network;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import surefire.org.twitterFeed.model.Tweet;
import surefire.org.twitterFeed.model.TwitterAuthToken;
import surefire.org.twitterFeed.parser.JSONParser;

import static surefire.org.twitterFeed.common.ApplicationSettings.setAuthToken;
import static surefire.org.twitterFeed.common.ApplicationSettings.setErrorMessage;
import static surefire.org.twitterFeed.common.Constants.TWITTER_STREAM_URL;
import static surefire.org.twitterFeed.common.Constants.TWITTER_TOKEN_URL;
import static surefire.org.twitterFeed.common.Constants.*;
/**
 * Created by HP on 3/04/2016.
 */
public class RequestProcessor {

    private Context mContext;
    private static final String TAG = RequestProcessor.class.getName();

    public RequestProcessor(Context ctx) {
        mContext = ctx;
    }

    private TwitterAuthToken getTwitterAuthToken(String twitterKeyBase64) throws UnsupportedEncodingException {
//        if (getAuthToken(mContext) != null) {
//            Log.d(TAG,"--------------------Not null");
//            return new JSONParser().convertJsonToTwitterAuthToken(getAuthToken(mContext));
//        }
        try {
            URL url = new URL(TWITTER_TOKEN_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Basic " + twitterKeyBase64);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);

            writeRequestToPost(conn, "grant_type=client_credentials");

            int responseCode = conn.getResponseCode();
            Log.d(TAG, url.toString());
            String jsonResponseAuthToken = readResponse(conn);
            setAuthToken(mContext, jsonResponseAuthToken);
            return new JSONParser().convertJsonToTwitterAuthToken(jsonResponseAuthToken);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Tweet> getTwitterFeedsForScreenName(String screenName) {
        ArrayList<Tweet> twitterTweetArrayList = null;
        try {
            String twitterUrlApiKey = URLEncoder.encode(API_KEY, "UTF-8");
            String twitterUrlApiSecret = URLEncoder.encode(API_SECRET, "UTF-8");
            String twitterKeySecret = twitterUrlApiKey + ":" + twitterUrlApiSecret;
            String twitterKeyBase64 = Base64.encodeToString(twitterKeySecret.getBytes(), Base64.NO_WRAP);
            TwitterAuthToken twitterAuthToken = getTwitterAuthToken(twitterKeyBase64);
            twitterTweetArrayList = getTwitterTweets(screenName, twitterAuthToken);
        }catch(UnsupportedEncodingException e){
            Log.e(TAG,e.getMessage());
            e.printStackTrace();
        }
        return twitterTweetArrayList;
    }

    private ArrayList<Tweet> getTwitterTweets(String screenName,
                                              TwitterAuthToken twitterAuthToken) {
        ArrayList<Tweet> twitterTweetArrayList = null;
        if (twitterAuthToken != null && twitterAuthToken.getAuthTokenType().equals("bearer")) {
            try {
                URL url = new URL(TWITTER_STREAM_URL + screenName);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Authorization", "Bearer " + twitterAuthToken.getAuthAccessToken());
                conn.setRequestProperty("Content-type", "application/json");
                // optional default is GET
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                Log.d(TAG, " " + responseCode);
                //Error handling should be handled properly. Creating the respective error JSON object and fetching exact error message from there"
                if (responseCode == 404) {
                    setErrorMessage(mContext, "Invalid Twitter User ID");
                    return null;
                } else if (responseCode == 401) {
                    setErrorMessage(mContext, "Not Authorized");
                    return null;
                }
                String jsonResponse = readResponse(conn);

                twitterTweetArrayList = new JSONParser().convertJsonToTwitterTweet(jsonResponse);

            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return twitterTweetArrayList;
    }

    private boolean writeRequestToPost(HttpURLConnection connection, String textBody) {
        try {
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            wr.write(textBody);
            wr.flush();
            wr.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private String readResponse(HttpURLConnection connection) {
        String result = null;
        InputStream inputStream;
        try {
            int statusCode = connection.getResponseCode();
            StringBuilder stringBuilder = new StringBuilder();
            if (statusCode >= 200 && statusCode < 400) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line + System.getProperty("line.separator"));
            }
            result = stringBuilder.toString();
        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
            return result;
        }
        return result;
    }


}
