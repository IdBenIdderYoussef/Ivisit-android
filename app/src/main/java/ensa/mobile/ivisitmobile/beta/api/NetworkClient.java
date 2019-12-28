package ensa.mobile.ivisitmobile.beta.api;

import android.content.Context;

import java.io.IOException;

import ensa.mobile.ivisitmobile.beta.security.App;
import ensa.mobile.ivisitmobile.beta.security.Session;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

    public static final String BASE_URL = "http://192.168.0.103d :8080";
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

            Session session = App.getSession();

            Request request = chain.request();
            if(request.url().encodedPath().equalsIgnoreCase("v1/accounts/register")
                    || request.url().encodedPath().equalsIgnoreCase("v1/posts") )
                return chain.proceed(request);

            Request newRequest  = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer " + session.getAccessToken())
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
                    .addConverterFactory(GsonConverterFactory.create()) // Convertor library used to convert response into POJO
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

