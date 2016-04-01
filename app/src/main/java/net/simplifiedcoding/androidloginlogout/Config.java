package net.simplifiedcoding.androidloginlogout;

/**
 * Created by Belal on 11/14/2015.
 */
public class Config {
    //URL to our login.php file
    public static final String LOGIN_URL = "http://eualuno.pe.hu/login.php";
    public static final String REGISTER_URL = "http://eualuno.pe.hu/register.php";
    public static final String FORPASS_URL = "http://eualuno.pe.hu/";
    public static final String CHGPASS_URL = "http://eualuno.pe.hu/";


    public static final String KEY_NAME = "name";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_USERNAME = "username";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";
    public static final String LOGIN_FAIL = "failure";
    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    public static final String UNIQUEID_SHARED_PREF = "uid";
    public static final String NAME_SHARED_PREF = "name";
    public static final String LASTNAME_SHARED_PREF = "lastname";
    public static final String USERNAME_SHARED_PREF = "username";

}
