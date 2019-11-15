package ensa.mobile.ivisitmobile.beta.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.api.interfaces.IvisitAPIs;
import ensa.mobile.ivisitmobile.beta.api.NetworkClient;
import ensa.mobile.ivisitmobile.beta.api.model.Account;
import ensa.mobile.ivisitmobile.beta.api.model.Authorization;
import ensa.mobile.ivisitmobile.beta.security.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    Button b1, register;
    EditText ed1, ed2;

    TextView tx1;

    public static String token;
    public static String username;
    public static Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        b1 = (Button) findViewById(R.id.register_register);
        ed1 = (EditText) findViewById(R.id.username_register);
        ed2 = (EditText) findViewById(R.id.password_register);

        register = (Button) findViewById(R.id.Register_login);
        tx1 = (TextView) findViewById(R.id.textView);
        tx1.setVisibility(View.GONE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = NetworkClient.getRetrofitClient();
                IvisitAPIs ivisitAPIs = retrofit.create(IvisitAPIs.class);
                account = Account.builder().username(ed1.getText().toString()).password(ed2.getText().toString()).build();
                Call<Authorization> call = ivisitAPIs.authenticate(account);

                call.enqueue(new Callback<Authorization>() {
                    @Override
                    public void onResponse(Call<Authorization> call, Response<Authorization> response) {
                        /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                         */
                        if (response.body() != null) {
                            System.out.println(response.body().getUser().getUsername());
                            System.out.println(response.body().getToken());
                            SessionManagement session = new SessionManagement(LoginActivity.this);
                            System.out.println("id : ----------------------------------- " + response.body().getUser().getId());
                            session.createLoginSession(response.body().getUser().getUsername(), response.body().getToken(), response.body().getUser().getId());
                            username = response.body().getUser().getUsername();
                            token = response.body().getToken();
                            Intent compteIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(compteIntent);
                        }

                    }

                    @Override
                    public void onFailure(Call<Authorization> call, Throwable t) {

                        System.out.println("Not Working : " + t.getMessage());


                    }
                });


            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent compteIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(compteIntent);
            }
        });

    }


}
