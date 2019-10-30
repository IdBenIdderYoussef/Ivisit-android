package ensa.mobile.ivisitmobile.beta.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
    private int likes;

    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments;

    @SerializedName("createdBy")
    @Expose
    private Account account;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;


}
