package ensa.mobile.ivisitmobile.beta.security;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

import ensa.mobile.ivisitmobile.beta.api.model.Login;

public class SessionManagement {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";


    private static final String IS_LOGIN = "IsLoggedIn";


    public static final String KEY_USERNAME = "UserName";
    public static final String KEY_ID = "id";

    public static final String KEY_access_token = "access_token";
    public static final String KEY_TOKEN_TYPE = "token_type";
    public static final String KEY_MASTER_ID = "MasterID";
    public static final String KEY_NAME = "Name";
    public static final String KEY_Access = "Name";


    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String username, String accesstoken , Long id/*, String tokentype, String masterid, String name, Integer access*/) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, username);
        // Storing name in pref
        editor.putString(KEY_access_token, accesstoken);
        editor.putLong(KEY_ID , id);

        // Storing email in pref
        /*editor.putString(KEY_TOKEN_TYPE, tokentype);

        editor.putString(KEY_MASTER_ID, masterid);
        editor.putString(KEY_TOKEN_TYPE, tokentype);
        editor.putString(KEY_NAME, name);
        editor.putInt(KEY_Access, access);*/




        // commit changes
        editor.commit();


        String user_name_new=pref.getString(KEY_USERNAME, null);

        Log.d("TAG","Pass user name :"+username+" user_name_new:"+user_name_new);

    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_access_token, pref.getString(KEY_access_token, null));

        user.put(KEY_TOKEN_TYPE, pref.getString(KEY_TOKEN_TYPE, null));
        user.put(KEY_MASTER_ID, pref.getString(KEY_MASTER_ID, null));
        user.put(KEY_access_token, pref.getString(KEY_access_token, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_Access, pref.getString(KEY_Access, null));


        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {

        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}