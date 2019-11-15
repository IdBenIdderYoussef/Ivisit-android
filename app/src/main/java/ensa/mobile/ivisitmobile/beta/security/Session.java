package ensa.mobile.ivisitmobile.beta.security;


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

public class Session {

    private Long id;
    private String username;
    private String accessToken;

}
