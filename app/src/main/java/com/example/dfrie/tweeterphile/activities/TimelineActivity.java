package com.example.dfrie.tweeterphile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.example.dfrie.tweeterphile.R;
import com.example.dfrie.tweeterphile.fragments.HomeTimelineFragment;
import com.example.dfrie.tweeterphile.fragments.MentionsTimelineFragment;
import com.example.dfrie.tweeterphile.restclient.models.Tweet;
import com.example.dfrie.tweeterphile.restclient.models.User;
import com.example.dfrie.tweeterphile.utils.TwitterUtils;

import org.parceler.Parcels;

public class TimelineActivity extends AppCompatActivity {

    public static final int SEND_TWEET_REQUEST = 100;
    public static final String EXTRA_SCREEN_ID = "com.example.dfrie.tweeterphile.activities.EXTRA_SCREEN_ID";

    private HomeTimelineFragment homeTimelineFragment;

    private Tweet ghostTweet = null;
    private User ghostUser = null;
    private long nextGhost = -1000;

    private TweetsPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.include);
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

        ////  This wont work to get the fragment....
        /////homeTimelineFragment = (Fragment) findViewById(R.id.fragment_tweets_list);

        if (savedInstanceState==null) {
            homeTimelineFragment = (HomeTimelineFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_tweets_list);
        }
        //  This is done in the Fragment's onCreate now...
        //homeTimelineFragment.populateTimeline(0);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip pagerTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pagerTabStrip.setViewPager(vpPager);

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
            String body = data.getExtras().getString("body");
/*
            String _name = data.getExtras().getString("name");
            String _screenName = data.getExtras().getString("screen_name");
            String _profileImageUrl = data.getExtras().getString("profile_image_url");
*/
            User user = (User) Parcels.unwrap(data.getParcelableExtra("user"));

            // Toast the name to display temporarily on screen
            //Toast.makeText(this, user.name, Toast.LENGTH_SHORT).show();

            if (ghostUser == null) {
                ghostUser = user;
                ghostUser.id = -1L;
            }

            ghostTweet = new Tweet();
            ghostTweet.id = --nextGhost;
            ghostTweet.user = ghostUser;
            ghostTweet.body = body;
            ghostTweet.timestamp = TwitterUtils.getCurrentTwitterDate();

            homeTimelineFragment.ensureTweet(ghostTweet);
        }
    }

    public void onProfileView(MenuItem item) {
        //Toast.makeText(this, "Profile was selected...", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void showUserProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        // We know we have clicked on an ImageView here, so...
        intent.putExtra(EXTRA_SCREEN_ID, (String)view.getTag());
        startActivity(intent);
    }

    /**
     * Return the fragments in the correct order
     */
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String[] tabTitles = new String[]{getString(R.string.timeline), getString(R.string.mentions)};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // The FragmentPagerAdapter will automatically cache the Fragments after they are created.
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    homeTimelineFragment = new HomeTimelineFragment();
                    if (ghostTweet != null) {
                        homeTimelineFragment.ensureTweet(ghostTweet);
                    }
                    return homeTimelineFragment;
                case 1:
                    return new MentionsTimelineFragment();
            }
            return null;
        }
        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }


}
