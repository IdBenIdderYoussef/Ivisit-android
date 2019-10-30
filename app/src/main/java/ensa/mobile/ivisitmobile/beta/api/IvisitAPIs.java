package ensa.mobile.ivisitmobile.beta.api;

import java.util.List;

import ensa.mobile.ivisitmobile.beta.model.Account;
import ensa.mobile.ivisitmobile.beta.model.Authorization;
import ensa.mobile.ivisitmobile.beta.model.Comment;
import ensa.mobile.ivisitmobile.beta.model.Post;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IvisitAPIs {

    @GET("/v1/posts/")
    Call<List<Post>> getAllPost();

    @GET("/v1/comments/post_id/{post_id}")
    Call<List<Comment>> getComments(@Path("post_id") Long id);

    @GET("/v1/posts/{id}")
    Call<Post> getPost(@Path("id") Long id);


    @POST("/auth/login")
    Call<Authorization> authenticate(@Body Account account);

    @POST("/v1/posts/")
    Call<Post> addPost(@Body Post post);


}
