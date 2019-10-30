package ensa.mobile.ivisitmobile.beta.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import ensa.mobile.ivisitmobile.beta.security.App;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

    public static final String BASE_URL = "http://192.168.122.1:8080";
    public static Retrofit retrofit;
    public Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
/*
    This public static method will return Retrofit client
    anywhere in the appplication
    */


    static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            SharedPreferences sharedPref;
            sharedPref = App.getContext().getSharedPreferences("AndroidHivePref" ,Context.MODE_PRIVATE);
            String token = sharedPref.getString("access_token","");
            System.out.println("HIIIIIIIIIIIIIIIIi"+token);
            Request newRequest  = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }
    }).build();

    public static Retrofit getRetrofitClient() {
        //If condition to ensure we don't create multiple retrofit instances in a single application
        if (retrofit == null) {

            //Defining the Retrofit using Builder
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    //This is the only mandatory call on Builder object.
                    .addConverterFactory(GsonConverterFactory.create()) // Convertor library used to convert response into POJO
                    .build();
        }
        return retrofit;
    }
}

