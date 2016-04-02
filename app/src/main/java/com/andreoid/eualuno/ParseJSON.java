package com.andreoid.eualuno;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ParseJSON {
    public static String[] ids;
    public static String[] names;
    public static String[] lastnames;
    public static String[] usernames;
    public static String[] emails;

    public static final String JSON_ARRAY = "user";

    public static final String KEY_NAME = "firstname";

//adssadfsjafd
    public JSONArray user = null;

    private String json;

    public ParseJSON(String json){
        this.json = json;
    }

    protected void parseJSON(){
        JSONObject jsonObject=null;
        try {
            System.out.println(json);
            jsonObject = new JSONObject(json);
            user = jsonObject.getJSONArray(JSON_ARRAY);

            ids = new String[user.length()];
            lastnames = new String[user.length()];
            names = new String[user.length()];
            usernames = new String[user.length()];
            emails = new String[user.length()];
            for(int i=0;i<user.length();i++){
                JSONObject jo = user.getJSONObject(i);
                ids[i] = jo.getString(Config.KEY_ID);
                names[i] = jo.getString(KEY_NAME);
                lastnames[i] = jo.getString(Config.KEY_LASTNAME);
                usernames[i] = jo.getString(Config.KEY_USERNAME);
                emails[i] = jo.getString(Config.KEY_EMAIL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}