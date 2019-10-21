package ensa.mobile.ivisitmobile.beta.api;

import java.util.List;

import ensa.mobile.ivisitmobile.beta.model.Comment;
import ensa.mobile.ivisitmobile.beta.model.Post;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IvisitAPIs {

    @GET("/v1/posts/")
    Call<List<Post>> getAllPost();

    @GET("/v1/comments/post_id/{post_id}")
    Call<List<Comment>> getComments(@Path("post_id") Long id);
}
