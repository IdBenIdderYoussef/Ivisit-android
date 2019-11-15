package ensa.mobile.ivisitmobile.beta.api.interfaces;

import java.util.List;

import ensa.mobile.ivisitmobile.beta.api.model.Account;
import ensa.mobile.ivisitmobile.beta.api.model.Authorization;
import ensa.mobile.ivisitmobile.beta.api.model.Comment;
import ensa.mobile.ivisitmobile.beta.api.model.Like;
import ensa.mobile.ivisitmobile.beta.api.model.Post;
import ensa.mobile.ivisitmobile.beta.api.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IvisitAPIs {

    public String urlPrefix = "/v1/posts/";

    @GET("/v1/posts/")
    Call<List<Post>> getAllPost();

    @GET("/v1/posts/{id}")
    Call<Post> getPost(@Path("id") Long id);

    @POST("/v1/posts/")
    Call<Post> addPost(@Body Post post);


    @GET("/v1/comments/post_id/{post_id}")
    Call<List<Comment>> getComments(@Path("post_id") Long id);


    @POST("/v1/comments/{postId}")
    Call<Comment> addComment(@Body Comment comment,@Path("postId") Long postId);

    @POST("/auth/login")
    Call<Authorization> authenticate(@Body Account account);


    @POST("/v1/likes/{postId}")
    Call<Like> addLike(@Body Like like, @Path("postId") Long postId);

    @POST("v1/accounts/register")
    Call<Boolean> registerUser(@Body Account build);


    @POST("v1/users/update")
    Call<Boolean> update(@Body User.UserBuilder account);

    @POST("v1/accounts/findByusername")
    Call<Account> findAccountByusername(@Body String username);


}
