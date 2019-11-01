package ensa.mobile.ivisitmobile.beta.api.interfaces;

import java.util.List;

import ensa.mobile.ivisitmobile.beta.api.model.Comment;
import ensa.mobile.ivisitmobile.beta.api.model.Post;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentApi {

    public String urlPrefix = "/v1/comments/";

    @POST(urlPrefix + "{postId}")
    Call<Void> create(@Body Comment comment, @Path("postId") Long postId);
}
