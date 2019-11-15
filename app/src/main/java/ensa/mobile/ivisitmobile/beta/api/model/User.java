package ensa.mobile.ivisitmobile.beta.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @JsonProperty("firstname")
    @Expose
    private String firstName;
    @JsonProperty("lastname")
    @Expose
    private String lastName;
    @JsonProperty("picture")
    @Expose
    private String picture;
    @JsonProperty("createdBy")
    @Expose
    private Account account;



}

