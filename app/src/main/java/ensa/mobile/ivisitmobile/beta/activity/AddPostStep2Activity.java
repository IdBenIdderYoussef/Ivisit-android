package ensa.mobile.ivisitmobile.beta.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonParser;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.api.interfaces.PostApi;
import ensa.mobile.ivisitmobile.beta.api.model.Address;
import ensa.mobile.ivisitmobile.beta.api.model.Post;
import ensa.mobile.ivisitmobile.beta.api.services.PostService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ensa.mobile.ivisitmobile.beta.R.drawable.transparent_bg_bordered_button_disabled;

public class AddPostStep2Activity extends AppCompatActivity {

    private Toolbar addPostToolbar2;
    private EditText addPostTitleEditText;
    private Button addPostBtn;
    private PostService postService;
    private ProgressDialog progressDialog;

    private StorageReference storageReference;

    private String description;
    private Uri imageUri;
    private Bitmap bitmap;
    private Spinner countrySpinner;
    private Spinner citySpinner;

    List<String> countryList;
    List<String> cityList;
    JSONObject jsonObject;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkFieldsForEmptyValues();
        }
    };

    void checkFieldsForEmptyValues() {
        String descriptionText = addPostTitleEditText.getText().toString();

        if (descriptionText.equals("")) {
            addPostBtn.setBackground(ContextCompat.getDrawable(this , transparent_bg_bordered_button_disabled));
        } else {
            addPostBtn.setBackground(ContextCompat.getDrawable(this , R.drawable.transparent_bg_bordered_button));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_step2);

        addPostToolbar2 = findViewById(R.id.add_toolbar2);
        setSupportActionBar(addPostToolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("Add your experience");

        addPostTitleEditText = findViewById(R.id.add_post_title_edit_text);
        addPostBtn = findViewById(R.id.add_post_btn);

        postService = new PostService(PostApi.class);

        storageReference = FirebaseStorage.getInstance().getReference();

        addPostTitleEditText.addTextChangedListener(textWatcher);

        countrySpinner = findViewById(R.id.spinnerCountry);
        citySpinner = findViewById(R.id.spinnerCity);

        description = getIntent().getStringExtra("description");

        imageUri = Uri.parse(getIntent().getStringExtra("image_uri"));

        checkFieldsForEmptyValues();

        addPostTitleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    closeKeyboard(v);
                }
            }
        });

        JSONParser parser = new JSONParser();
        AssetManager assetManager = getAssets();
        try {
            InputStream input = assetManager.open("country.json");
            Object object = parser.parse(new InputStreamReader(input));
            jsonObject = (JSONObject) object;
            countryList = new ArrayList<>(jsonObject.keySet());
            Collections.sort(countryList);

            countrySpinner.setAdapter(new ArrayAdapter<String>(AddPostStep2Activity.this, R.layout.support_simple_spinner_dropdown_item, countryList));
            countrySpinner.setSelection(1);


            cityList = (List<String>) jsonObject.get(countryList.get(1));
            Collections.sort(cityList);
            citySpinner.setAdapter(new ArrayAdapter<String>(AddPostStep2Activity.this, R.layout.support_simple_spinner_dropdown_item, cityList));
            citySpinner.setSelection(1);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityList = (List<String>) jsonObject.get(countryList.get(position));
                citySpinner.setAdapter(new ArrayAdapter<String>(AddPostStep2Activity.this, R.layout.support_simple_spinner_dropdown_item, cityList));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }




    private void closeKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }



    public void addPost(View view) {

        if(addPostTitleEditText.getText().toString() == null || addPostTitleEditText.getText().toString().equals("")){
            Toast.makeText(this , "Please enter a description for your experience",Toast.LENGTH_SHORT).show();
            return;
        }

        UUID uuid = UUID.randomUUID();
        String randomString = uuid.toString();
        progressDialog = new ProgressDialog(AddPostStep2Activity.this);
        progressDialog.setMessage("Wait !");
        progressDialog.show();

        final StorageReference imagePath = storageReference.child("post_images").child(randomString + ".jpg");
        imagePath.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return imagePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Post post = Post.builder().description(getIntent().getStringExtra("text_description"))
                            .picture(downloadUri.toString())
                            .title(addPostTitleEditText.getText().toString())
                            .address(Address.builder().country(countrySpinner.getSelectedItem().toString()).city(citySpinner.getSelectedItem().toString()).build())
                            .build();

                    Call<Void> call = postService.getApi().create(post);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            progressDialog.dismiss();
                            Intent mainIntent = new Intent(AddPostStep2Activity.this, MainActivity.class);
                            startActivity(mainIntent);

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Something wrong happened", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(AddPostStep2Activity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


}
