package ensa.mobile.ivisitmobile.beta.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Like {


    @JsonProperty("post")
    @Expose
    private Post post;

    @SerializedName("createdBy")
    @Expose
    private Account account;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;





}
