package com.example.dfrie.tweeterphile.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.dfrie.tweeterphile.R;
import com.example.dfrie.tweeterphile.TwitterApplication;
import com.example.dfrie.tweeterphile.restclient.TwitterClient;
import com.example.dfrie.tweeterphile.restclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * This class provides a view for the list of my tweets.
 *
 * Created by dfrie on 3/31/2017.
 */
public class MentionsTimelineFragment extends TweetsListFragment {

    private TwitterClient client;
    private long currentMaxId = 1L;
    private long currentMinId = Long.MAX_VALUE;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.client = TwitterApplication.getRestClient();
        // Get the initiai data...
        populateTimeline(0);
    }

    /**
     * Send the API request to get some tweets.
     * Get the timeline Json and fill the list view by creating Tweet objects from Json
     *
     * @param page  Pass a negative value to update the list with the latest tweets from the data sourec
     * @return
     */
    public boolean populateTimeline(int page) {
        if (page<0) {
            return updateTimeline();
        }
        final boolean[] retVal = {false};
        client.getMentionsTimeline(currentMinId, 0, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //log.d("debug", response.toString());
                // deserialize json, create models and load model data to ListView's adapter...
                ArrayList<Tweet> newTweets = Tweet.fromJsonArray(response);
                if (newTweets.size()>0) {
                    adapter.addAll(newTweets);
                    if (adapter.getCount() != 0) {
                        // Assume the result set tweets always are ordered descending...
                        currentMaxId = newTweets.get(0).getId();
                    }
                    currentMinId = newTweets.get(newTweets.size() - 1).getId();
                    retVal[0] = true;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                log.d("debug", errorResponse.toString());
                log.d("debug", throwable.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        errorResponse.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity().getApplicationContext(),
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
        client.getMentionsTimeline(0, currentMaxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //log.d("debug", response.toString());
                // deserialize json, create models and  load model data to ListView's adapter...
                ArrayList<Tweet> newTweets = Tweet.fromJsonArray(response);
                if (newTweets.size()>0) {
                    // Assume the result set tweets always are ordered descending...
                    for (int i = newTweets.size() - 1; i >= 0; i--) {
                        adapter.insert(newTweets.get(i), 0);
                    }
                    currentMaxId = newTweets.get(0).getId();
                    retVal[0] = true;
                }
                // Inform after update...
                String str = getText(R.string.up_to_date).toString();
                Toast.makeText(getActivity().getApplicationContext(),
                        String.format(str, newTweets.size()), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                log.d("debug", errorResponse.toString());
                log.d("debug", throwable.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        errorResponse.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity().getApplicationContext(),
                        throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return retVal[0];
    }

}
