package com.example.dfrie.tweeterphile.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dfrie.tweeterphile.R;
import com.example.dfrie.tweeterphile.activities.EndlessScrollListener;
import com.example.dfrie.tweeterphile.activities.TweetsArrayAdapter;
import com.example.dfrie.tweeterphile.restclient.models.Tweet;

import java.util.ArrayList;

/**
 * Created by dfrie on 3/30/2017.
 */
public abstract class TweetsListFragment extends Fragment {

    protected ArrayList<Tweet> tweets;
    protected TweetsArrayAdapter adapter;
    private ListView lvTweets;
    private EndlessScrollListener scrollListener;

    //  creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<Tweet>();
        adapter = new TweetsArrayAdapter(getActivity(), tweets);
    }

    //  inflation logic
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        lvTweets = (ListView)view.findViewById(R.id.lvTweets);
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
        return view;
    }

/*  Dont expose all this...
    public TweetsArrayAdapter getAdapter() {
        return adapter;
    }
*/

/*
    public void addAll(List<Tweet> newTweets) {
        adapter.addAll(newTweets);
    }

    public int getCount() {
        return adapter.getCount();
    }
*/

    public void insertAtTop(Tweet newTweet) {
        adapter.insert(newTweet, 0);
    }

    public void ensureTweet(Tweet ghostTweet) {
        Tweet t = adapter.getItem(0);
        if (!t.body.equals(ghostTweet.body) ||
                !t.user.screenName.equals(ghostTweet.user.screenName)) {
            adapter.insert(ghostTweet, 0);
        }
    }

    /**
     * Send the API request to get some tweets.
     * Get the timeline Json and fill the list view by creating Tweet objects from Json
     *
     * @param page  Pass a negative value to update the list with the latest tweets from the data sourec
     * @return
     */
    public abstract boolean populateTimeline(int page);

}
