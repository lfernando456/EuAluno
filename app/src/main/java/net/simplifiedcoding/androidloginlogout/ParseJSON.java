package net.simplifiedcoding.androidloginlogout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Belal on 9/22/2015.
 */
public class ParseJSON {
    public static String[] ids;
    public static String[] names;
    public static String[] lastnames;
    public static String[] usernames;
    public static String[] emails;

    public static final String JSON_ARRAY = "user";
    public static final String KEY_ID = "unique_id";
    public static final String KEY_LASTNAME = "firstname";
    public static final String KEY_NAME = "lastname";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
//adssadfsjafd
    public JSONArray user = null;

    private String json;

    public ParseJSON(String json){
        this.json = json;
    }

    protected void parseJSON(){
        JSONObject jsonObject=null;
        try {

            jsonObject = new JSONObject(json);
            user = jsonObject.getJSONArray(JSON_ARRAY);

            ids = new String[user.length()];
            lastnames = new String[user.length()];
            names = new String[user.length()];
            usernames = new String[user.length()];
            emails = new String[user.length()];
            for(int i=0;i<user.length();i++){
                JSONObject jo = user.getJSONObject(i);
                ids[i] = jo.getString(KEY_ID);
                lastnames[i] = jo.getString(KEY_LASTNAME);
                names[i] = jo.getString(KEY_NAME);
                usernames[i] = jo.getString(KEY_USERNAME);
                emails[i] = jo.getString(KEY_EMAIL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}