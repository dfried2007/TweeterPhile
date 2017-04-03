package com.example.dfrie.tweeterphile.restclient;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.example.dfrie.tweeterphile.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import java.security.InvalidParameterException;

import static com.example.dfrie.tweeterphile.TwitterApplication.API_CONSUMER_KEY;
import static com.example.dfrie.tweeterphile.TwitterApplication.API_CONSUMER_SECRET;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {

    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_CALLBACK_URL = "oauth://mySimpleTweets";

    public static final String BASE_URL = "https://api.twitter.com/1.1";

    // endpoints...
    public static final String STATUSES_HOME_TIMELINE = "statuses/home_timeline.json";
    public static final String STATUSES_UPDATE = "statuses/update.json";
    public static final String ACCOUNT_SETTINGS = "account/settings.json";
    public static final String USERS_LOOKUP = "users/lookup.json";
    public static final String STATUSES_MENTIONS_TIMELINE = "statuses/mentions_timeline.json";

    public static final String STATUSES_USER_TIMELINE = "statuses/user_timeline.json";
    public static final String ACCOUNT_VERIFY_CREDENTIALS = "account/verify_credentials.json";

    public static final int RETURN_COUNT = 25;

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, BASE_URL, API_CONSUMER_KEY, API_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    /**
     * Set up parameters to call the Home Timeline API.
     * Note: Do not request both maxId and sinceId.  One must be 0.
     *
     * @param maxId   the max id to be returned
     * @param sinceId the id after which tweets are returned
     * @param handler
     */
    public void getHomeTimeline(long maxId, long sinceId, AsyncHttpResponseHandler handler) {
        if (maxId > 0 && sinceId > 0) {
            throw new InvalidParameterException(context.getString(R.string.timeline_bad_params));
        }
        String apiUrl = getApiUrl(STATUSES_HOME_TIMELINE);
        RequestParams params = new RequestParams();
        params.put("count", RETURN_COUNT);
        if (maxId > 0) {
            // -1 here, since max_id is inclusive...
            params.put("max_id", String.valueOf(maxId - 1));
        }
        if (sinceId > 0) {
            params.put("since_id", String.valueOf(sinceId));
        }
        getClient().get(apiUrl, params, handler);
    }

    public void getAccountSettings(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(ACCOUNT_SETTINGS);
        //RequestParams params = new RequestParams();
        getClient().get(apiUrl, null, handler);
    }

    public void getUserDetails(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(USERS_LOOKUP);
        RequestParams params = new RequestParams();
        if (screenName != null) {
            params.put("screen_name", screenName);
        }
        getClient().get(apiUrl, params, handler);
    }

    public void postTweet(String body, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(STATUSES_UPDATE);
        RequestParams params = new RequestParams();
        params.put("status", body);
        getClient().post(apiUrl, params, handler);
    }

    public void getMentionsTimeline(long maxId, long sinceId, JsonHttpResponseHandler handler) {
        if (maxId > 0 && sinceId > 0) {
            throw new InvalidParameterException(context.getString(R.string.timeline_bad_params));
        }
        String apiUrl = getApiUrl(STATUSES_MENTIONS_TIMELINE);
        RequestParams params = new RequestParams();
        params.put("count", RETURN_COUNT);
        if (maxId > 0) {
            // -1 here, since max_id is inclusive...
            params.put("max_id", String.valueOf(maxId - 1));
        }
        if (sinceId > 0) {
            params.put("since_id", String.valueOf(sinceId));
        }
        getClient().get(apiUrl, params, handler);
    }

    public void getUserTimeline(String screenName, long maxId, long sinceId,
                                AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(STATUSES_USER_TIMELINE);
        RequestParams params = new RequestParams();
        params.put("count", RETURN_COUNT);
        if (screenName != null) {
            params.put("screen_name", screenName);
        }
        getClient().get(apiUrl, params, handler);
    }

    /**
     * Currently Not being used...
     *
     * @param screenName
     * @param handler
     */
    public void getUserInfo(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl(ACCOUNT_VERIFY_CREDENTIALS);
        RequestParams params = new RequestParams();
        if (screenName != null) {
            params.put("screen_name", screenName);
        }
        getClient().get(apiUrl, params, handler);
    }

}