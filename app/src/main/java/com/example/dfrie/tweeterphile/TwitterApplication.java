package com.example.dfrie.tweeterphile;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.example.dfrie.tweeterphile.restclient.TwitterClient;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TwitterClient client = TwitterApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class TwitterApplication extends Application {

	public static final String API_KEY_FILE = "api_key.properties";
    public static final String API_KEY_NAME = "twitter_consumer_key";
    public static String API_CONSUMER_KEY;
    public static final String API_SECRET_NAME = "twitter_consumer_secret";
    public static String API_CONSUMER_SECRET;

	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();

		FlowManager.init(new FlowConfig.Builder(this).build());
		FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

		TwitterApplication.context = this;

        try {
            InputStream is = getBaseContext().getAssets().open(API_KEY_FILE);
            Properties props = new Properties();
            props.load(is);
            API_CONSUMER_KEY = props.getProperty(API_KEY_NAME);
            API_CONSUMER_SECRET = props.getProperty(API_SECRET_NAME);
        } catch (FileNotFoundException e) {
            Toast.makeText(this, API_KEY_FILE + getString(R.string.file_not_found), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, TwitterApplication.context);
	}
}