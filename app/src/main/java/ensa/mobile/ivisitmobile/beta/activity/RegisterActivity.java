package ensa.mobile.ivisitmobile.beta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.api.NetworkClient;
import ensa.mobile.ivisitmobile.beta.api.interfaces.IvisitAPIs;
import ensa.mobile.ivisitmobile.beta.api.model.Account;
import retrofit2.Call;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    Button button_register;
    EditText username_register, password_register, email_register;
    static Account account = new Account();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        password_register = (EditText) findViewById(R.id.password_register);
        email_register = (EditText) findViewById(R.id.email_register);

        username_register = (EditText) findViewById(R.id.username_register);
        button_register = (Button) findViewById(R.id.register_register);


        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = NetworkClient.getRetrofitClient();
                IvisitAPIs ivisitAPIs = retrofit.create(IvisitAPIs.class);
                account = Account.builder().email(email_register.getText().toString()).username(username_register.getText().toString()).password(password_register.getText().toString()).build();
                Call<Boolean> call = ivisitAPIs.registerUser(account);

                call.enqueue(new Callback<Boolean>() {

                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.body() != null) {
                            System.out.println("success");
                            Intent compteIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(compteIntent);

                        }



                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                        System.out.println("Not Working : " + t.getMessage());


                    }
                });
            }


        });

    }
}




