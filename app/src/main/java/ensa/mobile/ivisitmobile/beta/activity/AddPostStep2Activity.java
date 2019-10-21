package ensa.mobile.ivisitmobile.beta.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import ensa.mobile.ivisitmobile.beta.R;

public class AddPostStep2Activity extends AppCompatActivity {

    String apiKey = "AIzaSyB9iHICb2cbkBHt7-4TpmCOt6QIOb5NuWI";
    PlacesClient placesClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_step2);

        if(!Places.isInitialized()){
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
        });

    }
}
