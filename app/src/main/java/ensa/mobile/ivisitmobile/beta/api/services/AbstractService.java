package ensa.mobile.ivisitmobile.beta.api.services;

import ensa.mobile.ivisitmobile.beta.api.NetworkClient;
import lombok.Getter;
import lombok.Setter;
import retrofit2.Retrofit;


@Getter
@Setter
public abstract class AbstractService<A> {

    protected Retrofit retrofit;
    protected Class<A> aClass;
    protected A api;

    public AbstractService(Class<A> aClass) {

        retrofit = NetworkClient.getRetrofitClient();
        this.aClass = aClass;
        api = retrofit.create(aClass);

    }

}
