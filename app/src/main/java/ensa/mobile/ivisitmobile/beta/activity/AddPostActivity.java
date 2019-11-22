package ensa.mobile.ivisitmobile.beta.activity;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import ensa.mobile.ivisitmobile.beta.R;
import ensa.mobile.ivisitmobile.beta.security.App;

import static ensa.mobile.ivisitmobile.beta.R.drawable.transparent_bg_bordered_button_disabled;

public class AddPostActivity extends AppCompatActivity {

    Toolbar addPostToolbar;
    private ImageView selectImageBtn;
    private ImageView postImageSelected;
    private TextView userTextView;
    private Uri imageUri = null;
    private EditText descriptionEditText;
    private Button toNextStepButton;
    private boolean isDescriptionFilled = false;
    private boolean isImageFilled = false;


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
        String descriptionText = descriptionEditText.getText().toString();

        if (descriptionText.equals("")) {
            toNextStepButton.setBackground(ContextCompat.getDrawable(this , transparent_bg_bordered_button_disabled));
        } else {
            isDescriptionFilled = true;
            if(isImageFilled){
                toNextStepButton.setBackground(ContextCompat.getDrawable(this , R.drawable.transparent_bg_bordered_button));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);

        addPostToolbar = (Toolbar) findViewById(R.id.add_toolbar);
        setSupportActionBar(addPostToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
        getSupportActionBar().setTitle("Add your experience");

        descriptionEditText = findViewById(R.id.description_add_post);
        toNextStepButton = findViewById(R.id.to_next_step_btn);
        selectImageBtn = findViewById(R.id.selec_image_post);
        postImageSelected = findViewById(R.id.post_image_selected);
        userTextView = findViewById(R.id.add_post_username);
        userTextView.setText(App.getSession().getUsername());
        descriptionEditText.addTextChangedListener(textWatcher);

        checkFieldsForEmptyValues();

    }

    public void toNextSteep(View view) {

        if(isDescriptionFilled == false || isImageFilled == false){
            Toast.makeText(this , "Please pick an image and enter a description for your experience to continue",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent nextSteepIntent = new Intent(AddPostActivity.this, AddPostStep2Activity.class);
        nextSteepIntent.putExtra("text_description", descriptionEditText.getText().toString());
        nextSteepIntent.putExtra("image_uri", imageUri.toString());
        startActivity(nextSteepIntent);
    }

    public void selectImage(View view) {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                isImageFilled = true;
                if(isDescriptionFilled){
                    toNextStepButton.setBackground(ContextCompat.getDrawable(this , R.drawable.transparent_bg_bordered_button));
                }

                postImageSelected.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
