package com.example.dfrie.tweeterphile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dfrie.tweeterphile.R;
import com.example.dfrie.tweeterphile.TwitterApplication;
import com.example.dfrie.tweeterphile.restclient.TwitterClient;
import com.example.dfrie.tweeterphile.restclient.models.Settings;
import com.example.dfrie.tweeterphile.restclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;
import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by dfrie on 3/25/2017.
 */

public class TweetActivity extends AppCompatActivity {

    private TextView tvScreenName;
    private EditText etTweet;
    private Button btTweet;
    private ImageView ivProfilePic;
    private TextView tvName;
    private TextView tvChars;

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

        ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);
        tvName = (TextView) findViewById(R.id.tvName);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvChars = (TextView) findViewById(R.id.tvChars);
        etTweet = (EditText) findViewById(R.id.etTweet);
        btTweet = (Button) findViewById(R.id.btTweet);

        // GET account/settings to get screen name
        final TwitterClient client = TwitterApplication.getRestClient();
        client.getAccountSettings(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Settings settings = new Settings(response);
                tvScreenName.setText(settings.getScreenName());
                // GET users/lookup to get my user info...
                client.getUserDetails(settings.getScreenName(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response2) {
                        ArrayList<User> users = User.fromJsonArray(response2);
                        User user = users.get(0);
                        tvName.setText(user.getName());
                        Picasso.with(getContext()).load(user.getProfileImageUrl())
                                .placeholder(R.drawable.ic_twitter)
                                .resize(300,300)
                                .into(ivProfilePic);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        log.d("debug", errorResponse.toString());
                        log.d("debug", throwable.getMessage());
                        tvName.setText(throwable.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                log.d("debug", errorResponse.toString());
                log.d("debug", throwable.getMessage());
                tvScreenName.setText(throwable.getMessage());
            }
        });

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
                client.postTweet(etTweet.getText().toString(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Intent i = new Intent(TweetActivity.this, TimelineActivity.class);
                        startActivity(i);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        log.d("debug", errorResponse.toString());
                        log.d("debug", throwable.getMessage());
                        tvName.setText(throwable.getMessage());
                    }
                });
            }
        });
    }
}