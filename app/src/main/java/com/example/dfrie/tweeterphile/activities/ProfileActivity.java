package com.example.dfrie.tweeterphile.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.dfrie.tweeterphile.R;
import com.example.dfrie.tweeterphile.fragments.UserProfileFragment;
import com.example.dfrie.tweeterphile.fragments.UserTimelineFragment;

public class ProfileActivity extends AppCompatActivity {

    private String screenName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        screenName = getIntent().getStringExtra(TimelineActivity.EXTRA_SCREEN_ID);
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            UserProfileFragment profileFragment = UserProfileFragment.newInstance(screenName);
            ft.replace(R.id.flProfile, profileFragment);

            UserTimelineFragment userFragment = UserTimelineFragment.newInstance(screenName);
            ft.replace(R.id.flUserTweets, userFragment);

            ft.commit();
        }
    }

}
