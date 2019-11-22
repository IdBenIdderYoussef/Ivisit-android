package ensa.mobile.ivisitmobile.beta.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    TextView view_email;
    TextView view_pass;
    TextView view_username;
    static Account account = new Account();
    boolean valider=false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        view_email=(TextView) findViewById(R.id.textView_email);
        view_pass=(TextView)findViewById(R.id.textView_pass);
        view_username=(TextView) findViewById(R.id.textView_usename);

        password_register = (EditText) findViewById(R.id.password_register);
        email_register = (EditText) findViewById(R.id.email_register);

        username_register = (EditText) findViewById(R.id.username_register);
        button_register = (Button) findViewById(R.id.register_register);
        final Validator Validator = new Validator();
        boolean usernamevalide;



        email_register.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(Validator.isValidEmail(s.toString())){
                    view_email.setTextColor(Color.GREEN);
                    view_email.setText("this email is valid");

                }
                else
                {view_email.setTextColor(Color.RED);
                    view_email.setText("this email is not valid");
                }

                valider=false;
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        username_register.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Retrofit retrofit = NetworkClient.getRetrofitClient();
                IvisitAPIs ivisitAPIs = retrofit.create(IvisitAPIs.class);
                Call<Account> call = ivisitAPIs.findAccountByusername(s.toString());
                boolean[] resultat = new boolean[1];

                call.enqueue(new Callback<Account>() {


                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        if (response.body() == null) {
                            view_username.setTextColor(Color.GREEN);

                            view_username.setText("this username is valid");


                        }
                        else {
                            view_username.setTextColor(Color.RED);

                            view_username.setText("this username already taken");
                        }

                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {

                    }
                });


            }


            @Override
            public void afterTextChanged(Editable s) {

            }

        });


        password_register.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Validator.isValidPassword(s.toString())){
                    view_pass.setText("this password is valid");
                    view_pass.setTextColor(Color.GREEN);
                }
                else {
                    view_pass.setTextColor(Color.RED);

                    view_pass.setText("Password must contain min 6 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}});


        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email_register.getText().toString().length()==0) view_email.setText("you must enter an email");
                if(username_register.getText().toString().length()==0) view_username.setText("you must enter a username");
                if(password_register.getText().toString().length()==0) view_pass.setText("you must enter a Password");



                if(Validator.isValidEmail(email_register.getText().toString()) && Validator.isValidPassword(password_register.getText().toString()) &&  view_username.getText().toString().equals("this username is valid")){

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
                    });}

                else {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("you must respect the formats ");

                    // Set click listener for alert dialog buttons
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch(which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    // User clicked the Yes button
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    // User clicked the No button
                                    break;
                            }
                        }
                    };

                    builder.setPositiveButton("Ok", dialogClickListener);



                    AlertDialog dialog = builder.create();
                    // Display the alert dialog on interface
                    dialog.show();
                }
            }



        });

    }
}