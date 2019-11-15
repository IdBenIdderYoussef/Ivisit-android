package ensa.mobile.ivisitmobile.beta.api.interfaces;

import ensa.mobile.ivisitmobile.beta.api.model.Report;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReportApi {

    public String urlPrefix = "/v1/reports/";

    @POST(urlPrefix + "{postId}")
    Call<Void> create(@Body Report report, @Path("postId") Long postId);
}
