package ensa.mobile.ivisitmobile.beta.api.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private Boolean isLiked;
    private Boolean isAlreadyLiked;


    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("picture")
    @Expose
    private String picture;

    @SerializedName("likes")
    @Expose
    private List<Like> likes;

    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments;

    @SerializedName("createdBy")
    @Expose
    private Account account;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;


    @SerializedName("reports")
    @Expose
    private List<Report> reports;

}
