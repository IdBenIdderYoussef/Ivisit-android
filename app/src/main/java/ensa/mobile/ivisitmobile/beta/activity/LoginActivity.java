package ensa.mobile.ivisitmobile.beta.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.api.IvisitAPIs;
import ensa.mobile.ivisitmobile.beta.api.NetworkClient;
import ensa.mobile.ivisitmobile.beta.model.Account;
import ensa.mobile.ivisitmobile.beta.model.Authorization;
import ensa.mobile.ivisitmobile.beta.model.Post;
import ensa.mobile.ivisitmobile.beta.security.ApiService;
import ensa.mobile.ivisitmobile.beta.security.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    Button b1, b2;
    EditText ed1, ed2;

    TextView tx1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        b1 = (Button) findViewById(R.id.button);
        ed1 = (EditText) findViewById(R.id.editText);
        ed2 = (EditText) findViewById(R.id.editText2);

        b2 = (Button) findViewById(R.id.button2);
        tx1 = (TextView) findViewById(R.id.textView3);
        tx1.setVisibility(View.GONE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = NetworkClient.getRetrofitClient();
                IvisitAPIs ivisitAPIs = retrofit.create(IvisitAPIs.class);
                Call<Authorization> call = ivisitAPIs.authenticate(Account.builder().username(ed1.getText().toString()).password(ed2.getText().toString()).build());

                call.enqueue(new Callback<Authorization>() {
                    @Override
                    public void onResponse(Call<Authorization> call, Response<Authorization> response) {
                        /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                         */
                        if (response.body() != null) {
                            System.out.println(response.body().getUser().getUsername());
                            System.out.println(response.body().getToken());
                            SessionManagement session = new SessionManagement(LoginActivity.this);
                            session.createLoginSession(response.body().getUser().getUsername() , response.body().getToken());
                            System.out.println("Work");
                        }
                    }
                    @Override
                    public void onFailure(Call<Authorization> call, Throwable t) {
                /*
                Error callback
                */
                        System.out.println("Not Working : "+t.getMessage());



                    }
                });

            }
        });


    }


    void login() {

    }

}
