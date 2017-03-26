package com.example.dfrie.tweeterphile.restclient.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dfrie on 3/24/2017.
 */

public class User {

    private Long id;
    private String name;
    private String screenName;
    private String profileImageUrl;

    public static User fromJson(JSONObject jsonObject) {
        User user = new User();
        try {
            user.id = jsonObject.getLong("id");
            user.name = jsonObject.getString("name");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public static ArrayList<User> fromJsonArray(JSONArray jsonArray) {
        ArrayList<User> users = new ArrayList<User>(jsonArray.length());
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject userJson = null;
            try {
                userJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            User user = User.fromJson(userJson);
            //user.save();
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

}
