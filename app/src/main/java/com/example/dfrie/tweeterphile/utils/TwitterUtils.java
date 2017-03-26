package com.example.dfrie.tweeterphile.utils;

import android.os.AsyncTask;

import com.example.dfrie.tweeterphile.TwitterApplication;
import com.example.dfrie.tweeterphile.restclient.TwitterClient;
import com.example.dfrie.tweeterphile.restclient.models.Settings;
import com.example.dfrie.tweeterphile.restclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Request;

import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by dfrie on 3/25/2017.
 */

public class TwitterUtils {

    public static final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy");

    // Wed Mar 07 01:27:09 +0000 2007
    public static final SimpleDateFormat TWITTER_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

    public static Date parseDate(String s) {
        return SDF.parse(s, new ParsePosition(0));
    }

    public static String formatDate(Date date) {
        return SDF.format(date);
    }

    public static Date parseTwitterDate(String s) {
        return TWITTER_FORMAT.parse(s, new ParsePosition(0));
    }

    public static String formatTwitterDate(String s) {
        Date date = parseDate(s);
        return TWITTER_FORMAT.format(date);
    }


    public static String getCurrentScreenName() {
        TwitterClient client = TwitterApplication.getRestClient();
        final String[] retVal = new String[1];
        client.getAccountSettings(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Settings settings = new Settings(response);
                retVal[0] = settings.getScreenName();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                log.d("debug", errorResponse.toString());
                log.d("debug", throwable.getMessage());
                retVal[0] = "";
            }
        });
        return retVal[0];
    }







    public static User getCurrentUserFrom(String screenName) {

return null;

    }
}
