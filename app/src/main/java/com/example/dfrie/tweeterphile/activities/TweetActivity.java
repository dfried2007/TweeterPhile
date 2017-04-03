package com.example.dfrie.tweeterphile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dfrie.tweeterphile.R;
import com.example.dfrie.tweeterphile.TwitterApplication;
import com.example.dfrie.tweeterphile.fragments.UserProfileFragment;
import com.example.dfrie.tweeterphile.restclient.TwitterClient;
import com.example.dfrie.tweeterphile.restclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by dfrie on 3/25/2017.
 */

public class TweetActivity extends AppCompatActivity {

    private EditText etTweet;
    private Button btTweet;
    private TextView tvChars;
    private TextView tvErrorMsg;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        //Toast.makeText(this.getApplicationContext(), "...in TweetActivity...", Toast.LENGTH_SHORT).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Display icon in the toolbar
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // Enable the up icon...
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // diable the default toolbar title...
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            UserProfileFragment profileFragment = UserProfileFragment.newInstance(null);
            ft.replace(R.id.flProfile, profileFragment);
            ft.commit();
        }

        tvChars = (TextView) findViewById(R.id.tvChars);
        etTweet = (EditText) findViewById(R.id.etTweet);
        btTweet = (Button) findViewById(R.id.btTweet);
        tvErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvChars.setText(s.length() + " " + getString(R.string.characters));
            }
        });

        btTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etTweet.getText().toString();
                if (str.length() == 0) {
                    tvErrorMsg.setText(R.string.enter_some_text);
                    return;
                }
                TwitterClient client = TwitterApplication.getRestClient();
                client.postTweet(str, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // Prepare data intent
                        Intent data = new Intent();
                        // Pass relevant data back as a result
                        data.putExtra("body", etTweet.getText().toString());
/*
                        data.putExtra("name", tvName.getText().toString());
                        data.putExtra("screen_name", tvScreenName.getText().toString());
                        data.putExtra("profile_image_url", profileImageUrl);
*/
                        data.putExtra("user", Parcels.wrap(user));

                        // Activity finished ok, return the data
                        setResult(RESULT_OK, data); // set result code and bundle data for response
                        // closes the activity, pass data to parent...
                        finish();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        log.d("debug", errorResponse.toString());
                        log.d("debug", throwable.getMessage());
                        tvErrorMsg.setText(throwable.getMessage());
                    }
                });
            }
        });
    }
}