package ensa.mobile.ivisitmobile.beta.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.api.IvisitAPIs;
import ensa.mobile.ivisitmobile.beta.api.NetworkClient;
import ensa.mobile.ivisitmobile.beta.model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddPostStep2Activity extends AppCompatActivity {

    private Toolbar addPostToolbar2;
    private EditText addPostTitleEditText;
    private Button addPostBtn;

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

    void checkFieldsForEmptyValues(){
        String descriptionText = addPostTitleEditText.getText().toString();

        if(descriptionText.equals("")){
            addPostBtn.setEnabled(false);
        } else {
            addPostBtn.setEnabled(true);
        }
    }


    String apiKey = "AIzaSyB9iHICb2cbkBHt7-4TpmCOt6QIOb5NuWI";
    PlacesClient placesClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_step2);

        addPostToolbar2 = (Toolbar) findViewById(R.id.add_toolbar2);
        setSupportActionBar(addPostToolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle("Add your experience");

        addPostTitleEditText = findViewById(R.id.add_post_title_edit_text);
        addPostBtn = findViewById(R.id.add_post_btn);

        addPostTitleEditText.addTextChangedListener(textWatcher);

        checkFieldsForEmptyValues();

        /*if(!Places.isInitialized()){
            Places.initialize(getApplicationContext() , apiKey);
        }

        placesClient = Places.createClient(this);

        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.location_text_view);

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID , Place.Field.LAT_LNG));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();

            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });*/

    }


    public void addPost(View view){

        Retrofit retrofit = NetworkClient.getRetrofitClient();
        IvisitAPIs ivisitAPIs = retrofit.create(IvisitAPIs.class);
        Post post = Post.builder().description(getIntent().getStringExtra("text_description")).title(addPostTitleEditText.getText().toString()).build();
        Call<Post> call = ivisitAPIs.addPost(post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                 */
                if (response.body() != null) {
                    Toast.makeText(getApplicationContext() , "Post added to Base Données",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                /*
                Error callback
                */
                Toast.makeText(getApplicationContext() , "Something wrong happened",Toast.LENGTH_LONG).show();


            }
        });

    }

}
