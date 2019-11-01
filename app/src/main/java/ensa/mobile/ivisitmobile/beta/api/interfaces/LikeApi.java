package ensa.mobile.ivisitmobile.beta.api.interfaces;

import ensa.mobile.ivisitmobile.beta.api.model.Comment;
import ensa.mobile.ivisitmobile.beta.api.model.Like;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LikeApi {


    public String urlPrefix = "/v1/likes/";

    @POST(urlPrefix + "{postId}")
    Call<Like> create(@Body Like like, @Path("postId") Long postId);

    @POST(urlPrefix + "{accountUsername}/{postId}")
    Call<Void> delete(@Path("accountUsername") String username, @Path("postId") Long postId);



}
