package surefire.org.twitterFeed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import surefire.org.twitterFeed.adapter.TweetListAdapter;
import surefire.org.twitterFeed.model.Tweet;
import surefire.org.twitterFeed.network.RequestProcessor;

import static surefire.org.twitterFeed.common.AppUtil.encodeTobase64;
import static surefire.org.twitterFeed.common.AppUtil.isConnectedToNetwork;
import static surefire.org.twitterFeed.common.ApplicationSettings.getErrorMessage;
import static surefire.org.twitterFeed.common.ApplicationSettings.setProfileImage;

public class MainActivity extends AppCompatActivity {

    private TextView titleTweet;
    private static final String TAG = MainActivity.class.getName();
    private ListView listview;
    ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.fragment_tweet_list);
        final EditText userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        titleTweet = (TextView) findViewById(R.id.tweetsTitle);
        titleTweet.setVisibility(View.INVISIBLE);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);

        Button btnActionGo = (Button) findViewById(R.id.buttonGo);
        btnActionGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "on click of \"Go\" button for" + userNameEditText.getText());

                if (isConnectedToNetwork(MainActivity.this)) {
                    if (userNameEditText.getText() != null && userNameEditText.getText().length() != 0) {
                        new TwitterFeedAsyncTask().execute(userNameEditText.getText());
                    } else {
                        listview.setAdapter(null);
                        titleTweet.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "Please enter twitter user id", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d(TAG, "Network not Available!");
                    Toast.makeText(MainActivity.this, "Network not Available!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private class TwitterFeedAsyncTask extends AsyncTask<Object, Void, ArrayList<Tweet>> {
        ArrayList<Tweet> twitterTweets;

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Tweet> doInBackground(Object... params) {

            if (params.length > 0) {
                RequestProcessor feedRequest = new RequestProcessor(MainActivity.this);
                twitterTweets = feedRequest.getTwitterFeedsForScreenName(params[0].toString());
            }
            try {
                if (twitterTweets != null && twitterTweets.size() > 0 && twitterTweets.get(0) != null) {
                    Log.d(TAG, "Fetching the profile image" + twitterTweets);
                    String imageURL = twitterTweets.get(0).getTwitterUser().getProfileImageUrl();
                    InputStream inputStream = (InputStream) new URL(imageURL).getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    String base64ProfileImage = encodeTobase64(bitmap);
                    setProfileImage(MainActivity.this, base64ProfileImage);
                }

            } catch (IOException e) {
                Log.e(TAG,e.getMessage());
                e.printStackTrace();
            }
            return twitterTweets;
        }

        @Override
        protected void onPostExecute(ArrayList<Tweet> tweets) {
            super.onPostExecute(tweets);
            if (tweets != null && tweets.size() > 0) {
                titleTweet.setVisibility(View.VISIBLE);
                ArrayAdapter<Tweet> adapter = new TweetListAdapter(MainActivity.this, twitterTweets);
                listview.setAdapter(adapter);
            } else {
                titleTweet.setVisibility(View.INVISIBLE);
                listview.setAdapter(null);
                Toast.makeText(MainActivity.this, getErrorMessage(MainActivity.this), Toast.LENGTH_LONG).show();
            }
            pb.setVisibility(View.GONE);

        }
    }

}
