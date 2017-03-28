package com.example.dfrie.tweeterphile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dfrie.tweeterphile.R;
import com.example.dfrie.tweeterphile.TwitterApplication;
import com.example.dfrie.tweeterphile.restclient.TwitterClient;
import com.example.dfrie.tweeterphile.restclient.models.Tweet;
import com.example.dfrie.tweeterphile.restclient.models.User;
import com.example.dfrie.tweeterphile.utils.TwitterUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class TimelineActivity extends AppCompatActivity {

    public static final int SEND_TWEET_REQUEST = 100;

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter adapter;
    private ListView lvTweets;
    private EndlessScrollListener scrollListener;

    private long currentMaxId = 1L;
    private long currentMinId = Long.MAX_VALUE;

    private User ghostUser = null;
    private long nextGhost = -1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        // Display icon in the toolbar
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // Enable the up icon...
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // diable the default toolbar title...
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        lvTweets = (ListView)findViewById(R.id.lvTweets);
        tweets = new ArrayList<Tweet>();
        adapter = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(adapter);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                return populateTimeline(page);
                // or loadNextDataFromApi(totalItemsCount);
                //return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
        client = TwitterApplication.getRestClient();
        populateTimeline(0);
    }

    //
    //

    /**
     * Send the API request to get some tweets.
     * Get the timeline Json and fill the list view by creating Tweet objects from Json
     */
    private boolean populateTimeline(int page) {
        if (page<0) {
            return updateTimeline();
        }
        final boolean[] retVal = {false};
        client.getHomeTimeline(currentMinId, 0, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //log.d("debug", response.toString());
                // deserialize json, create models and load model data to ListView's adapter...
                ArrayList<Tweet> newTweets = Tweet.fromJsonArray(response);
                adapter.addAll(newTweets);
                if (adapter.getCount()!=0) {
                    // Assume the result set tweets always are ordered descending...
                    currentMaxId = newTweets.get(0).getId();
                }
                currentMinId = newTweets.get(newTweets.size()-1).getId();
                retVal[0] = true;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                log.d("debug", errorResponse.toString());
                log.d("debug", throwable.getMessage());
                Toast.makeText(TimelineActivity.this.getApplicationContext(),
                        errorResponse.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(TimelineActivity.this.getApplicationContext(),
                        throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return retVal[0];
    }

    /**
     * Send the API request to get the most recent tweets, if any.
     * Get the timeline Json and fill the list view by creating Tweet objects from Json
     */
    private boolean updateTimeline() {
        final boolean[] retVal = {false};
        client.getHomeTimeline(0, currentMaxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //log.d("debug", response.toString());
                // deserialize json, create models and  load model data to ListView's adapter...
                ArrayList<Tweet> newTweets = Tweet.fromJsonArray(response);
                if (newTweets.size()>0) {
                    // First, clean out any ghost Tweets I may have added that match new Tweets...
                    cleanOutGhostTweets(newTweets);
                    // Assume the result set tweets always are ordered descending...
                    for (int i = newTweets.size() - 1; i >= 0; i--) {
                        tweets.add(0, newTweets.get(i));
                    }
                    adapter.notifyDataSetChanged();
                    currentMaxId = newTweets.get(0).getId();
                    retVal[0] = true;
                }
            }

            /**
             * We may have added some ghost tweets from the sub-activity, so now we must remove them...
             * Ghost tweets have ids < 0, and are found only at the top of stack.
             *
             * @param newTweets
             */
            private void cleanOutGhostTweets(ArrayList<Tweet> newTweets) {
                Tweet t, tt;
                outer: for (int i = newTweets.size() - 1; i >= 0; i--) {
                    t = newTweets.get(i);
                    for (int j = 0; j < tweets.size(); j++) {
                        tt = tweets.get(j);
                        if (tt.getId() > 0) {
                            if (j == 0) {
                                break outer;
                            }
                            break;
                        }
                        if (t.getBody().equals(tt.getBody()) &&
                                t.getUser().getScreenName().equals(tt.getUser().getScreenName())) {
                            tweets.remove(j);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                log.d("debug", errorResponse.toString());
                log.d("debug", throwable.getMessage());
                Toast.makeText(TimelineActivity.this.getApplicationContext(),
                        errorResponse.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(TimelineActivity.this.getApplicationContext(),
                        throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return retVal[0];
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        //MenuItem tweetItem = menu.findItem(R.id.action_tweet);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_tweet) {
            //Toast.makeText(this, "Tweet was selected...", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), TweetActivity.class);
            startActivityForResult(i, SEND_TWEET_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Handle the result of the sub-activity...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SEND_TWEET_REQUEST) {
            // Extract name value from result extras
            String _name = data.getExtras().getString("name");
            String _body = data.getExtras().getString("body");
            String _screenName = data.getExtras().getString("screen_name");
            String _profileImageUrl = data.getExtras().getString("profile_image_url");

            // Toast the name to display temporarily on screen
            Toast.makeText(this, _name, Toast.LENGTH_SHORT).show();

            if (ghostUser == null) {
                ghostUser = new User();
                ghostUser.id = -1L;
                ghostUser.name = _name;
                ghostUser.screenName = _screenName;
                ghostUser.profileImageUrl = _profileImageUrl;
            }

            Tweet ghostTweet = new Tweet();
            ghostTweet.id = --nextGhost;
            ghostTweet.user = ghostUser;
            ghostTweet.body = _body;
            ghostTweet.timestamp = TwitterUtils.getCurrentTwitterDate();

            tweets.add(0, ghostTweet);
            adapter.notifyDataSetChanged();
        }
    }
}
