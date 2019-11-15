package ensa.mobile.ivisitmobile.beta.api.interfaces;

import ensa.mobile.ivisitmobile.beta.api.dto.UpdateUserDto;
import ensa.mobile.ivisitmobile.beta.api.model.Comment;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccountApi {

    public String urlPrefix = "/v1/users/";

    @POST(urlPrefix + "update")
    Call<Void> update(@Body UpdateUserDto userDto);

}
