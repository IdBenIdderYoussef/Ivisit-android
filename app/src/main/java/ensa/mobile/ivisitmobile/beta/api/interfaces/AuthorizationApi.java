package ensa.mobile.ivisitmobile.beta.api.interfaces;

import ensa.mobile.ivisitmobile.beta.api.model.Account;
import ensa.mobile.ivisitmobile.beta.api.model.Authorization;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthorizationApi {

    @POST("/auth/login")
    Call<Authorization> authenticate(@Body Account account);

}
