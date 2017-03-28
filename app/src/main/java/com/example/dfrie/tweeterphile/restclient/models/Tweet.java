package com.example.dfrie.tweeterphile.restclient.models;



import com.example.dfrie.tweeterphile.restclient.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dfrie on 3/23/2017.
 */
@Table(database = MyDatabase.class)
public class Tweet extends BaseModel {
    @PrimaryKey
    @Column
    public Long id;
    @Column
    public String timestamp;
    @Column
    public String body;

    @ForeignKey
    public User user;

    public Tweet(){
        super();
    }

    public Tweet(JSONObject object){
        super();
        try {
            this.timestamp = object.getString("created_at");
            this.body = object.getString("text");
            this.id = object.getLong("id");
            this.user =  User.fromJson(object.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = new Tweet(tweetJson);
            //tweet.save();
            if (tweet != null) {
                tweets.add(tweet);
            }
        }
        return tweets;
    }

    public Long getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getBody() {
        return body;
    }

    public User getUser() {
        return user;
    }

}