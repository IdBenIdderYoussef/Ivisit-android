package ensa.mobile.ivisitmobile.beta.api.interfaces;

import android.database.Observable;

import java.util.List;


import ensa.mobile.ivisitmobile.beta.api.model.Post;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PostApi {

    public String urlPrefix = "/v1/posts/";

    @GET(urlPrefix)
    Call<List<Post>> findAll();

    @POST(urlPrefix)
    Call<Post> create(@Body Post post );

    @GET(urlPrefix+"{id}")
    Call<Post> get(@Path("id") Long id);


}
