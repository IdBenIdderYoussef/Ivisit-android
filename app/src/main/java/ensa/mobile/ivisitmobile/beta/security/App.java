package ensa.mobile.ivisitmobile.beta.security;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ensa.mobile.ivisitmobile.beta.security.SecurityConstants.KEY_USERNAME;
import static ensa.mobile.ivisitmobile.beta.security.SecurityConstants.KEY_access_token;
import static ensa.mobile.ivisitmobile.beta.security.SecurityConstants.PREF_NAME;
import static ensa.mobile.ivisitmobile.beta.security.SecurityConstants.PRIVATE_MODE;
import static ensa.mobile.ivisitmobile.beta.security.SessionManagement.KEY_ID;
import static ensa.mobile.ivisitmobile.beta.security.SessionManagement.KEY_ROLE;

public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }

    public static Session getSession(){
        SharedPreferences sharedPref;
        sharedPref = App.getContext().getSharedPreferences( PREF_NAME,PRIVATE_MODE);
        String token = sharedPref.getString(KEY_access_token,"");
        String username = sharedPref.getString(KEY_USERNAME,"");
        String role = sharedPref.getString(KEY_ROLE,"");
        Long id = sharedPref.getLong(KEY_ID , 0);
        return Session.builder().accessToken(token).username(username).id(id).role(role).build();
    }

}
