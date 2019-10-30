package ensa.mobile.ivisitmobile.beta.model;

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

public class Authorization {

    @SerializedName("user")
    @Expose
    private Account user;

    @SerializedName("token")
    @Expose
    private String token;
}
