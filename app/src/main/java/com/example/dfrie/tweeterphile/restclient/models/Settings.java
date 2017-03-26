package com.example.dfrie.tweeterphile.restclient.models;

import com.example.dfrie.tweeterphile.restclient.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dfrie on 3/25/2017.
 */
@Table(database = MyDatabase.class)
public class Settings extends BaseModel {
    @PrimaryKey
    @Column
    String screenName;

    public Settings(){
        super();
    }

    public Settings(JSONObject object){
        super();
        try {
            this.screenName = object.getString("screen_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getScreenName() {
        return screenName;
    }
}