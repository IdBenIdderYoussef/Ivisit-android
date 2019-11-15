package ensa.mobile.ivisitmobile.beta.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.api.NetworkClient;
import ensa.mobile.ivisitmobile.beta.api.dto.UpdateUserDto;
import ensa.mobile.ivisitmobile.beta.api.interfaces.AccountApi;
import ensa.mobile.ivisitmobile.beta.api.interfaces.IvisitAPIs;
import ensa.mobile.ivisitmobile.beta.api.model.Account;
import ensa.mobile.ivisitmobile.beta.api.model.User;
import ensa.mobile.ivisitmobile.beta.api.services.AccountService;
import ensa.mobile.ivisitmobile.beta.security.App;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ProfileActivity extends AppCompatActivity {

    private AccountService accountService;
    private EditText firstNameEditText;
    private EditText lastNameEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firstNameEditText = findViewById(R.id.firstname_profile);
        lastNameEditText = findViewById(R.id.lastname_profile);

        Button update = (Button) findViewById(R.id.update_profile);

        accountService = new AccountService(AccountApi.class);

        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                System.out.println("First Name : " + firstNameEditText.getText().toString());
                System.out.println("First Name : " + lastNameEditText.getText().toString());
                UpdateUserDto userDto = UpdateUserDto.builder()
                        .id(App.getSession().getId())
                        .firstName(firstNameEditText.getText().toString())
                        .lastName(lastNameEditText.getText().toString())
                        .build();
                Call<Void> call = accountService.getApi().update(userDto);

                call.enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(ProfileActivity.this, "profile Updated", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

            }

        });
    }
}