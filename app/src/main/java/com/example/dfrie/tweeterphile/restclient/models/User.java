package com.example.dfrie.tweeterphile.restclient.models;

import com.example.dfrie.tweeterphile.restclient.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by dfrie on 3/24/2017.
 */

@Parcel
@Table(database = MyDatabase.class)
public class User {

    @PrimaryKey
    @Column
    public long id;
    @Column
    public String name;
    @Column
    public String screenName;
    @Column
    public String location;
    @Column
    public String description;
    @Column
    public String profileImageUrl;
    @Column
    public int followersCount;
    @Column
    public int friendsCount;
    @Column
    public String profileBackgroundColor;
    @Column
    public String profileBackgroundImageUrl;
    @Column
    public String profileTextColor;

    // Empty constructor needed by the Parceler library...
    public User() {
    }

    public static User fromJson(JSONObject jsonObject) {
        User user = new User();
        try {
            user.id = jsonObject.getLong("id");
            user.name = jsonObject.getString("name");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.location = jsonObject.getString("location");
            user.description = jsonObject.getString("description");
            user.followersCount = jsonObject.getInt("followers_count");
            user.friendsCount = jsonObject.getInt("friends_count");
            user.profileBackgroundColor = jsonObject.getString("profile_background_color");
            user.profileBackgroundImageUrl = jsonObject.getString("profile_background_image_url");
            user.profileTextColor = jsonObject.getString("profile_text_color");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public long getId() {
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

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public String getProfileTextColor() {
        return profileTextColor;
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
