package ensa.mobile.ivisitmobile.beta.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.api.dto.UpdateUserDto;
import ensa.mobile.ivisitmobile.beta.api.interfaces.AccountApi;
import ensa.mobile.ivisitmobile.beta.api.services.AccountService;
import ensa.mobile.ivisitmobile.beta.security.App;
import ensa.mobile.ivisitmobile.beta.security.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileActivity extends AppCompatActivity {

    private AccountService accountService;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private Button signout;
    private SessionManagement sessionManagement;
    private Button update;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firstNameEditText = findViewById(R.id.firstname_profile);
        lastNameEditText = findViewById(R.id.lastname_profile);

        update = findViewById(R.id.update_profile);
        signout = findViewById(R.id.sign_out_btn);

        accountService = new AccountService(AccountApi.class);

        sessionManagement = new SessionManagement(this);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Sign out");
                builder.setMessage("Are you sure ?");

                // Set click listener for alert dialog buttons
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                // User clicked the Yes button
                                sessionManagement.destroySession();
                                Intent mainActivity = new Intent(ProfileActivity.this, MainActivity.class);
                                startActivity(mainActivity);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // User clicked the No button
                                break;
                        }
                    }
                };

                builder.setPositiveButton("Yes", dialogClickListener);
                builder.setNegativeButton("No",dialogClickListener);



                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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