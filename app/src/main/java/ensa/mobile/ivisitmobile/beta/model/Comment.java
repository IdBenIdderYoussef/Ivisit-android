package ensa.mobile.ivisitmobile.beta.model;


import com.fasterxml.jackson.annotation.JsonProperty;

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

public class Comment {

    @JsonProperty("content")
    private String content;
    @JsonProperty("dateCreation")
    private String dateCreation;
    @JsonProperty("username")
    private String username;



}
