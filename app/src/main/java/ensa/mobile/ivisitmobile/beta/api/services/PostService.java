package ensa.mobile.ivisitmobile.beta.api.services;

import java.util.List;

import ensa.mobile.ivisitmobile.beta.api.interfaces.PostApi;
import ensa.mobile.ivisitmobile.beta.api.model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostService extends AbstractService<PostApi> {

    public PostService(Class<PostApi> aClass) {
        super(aClass);
    }


}
