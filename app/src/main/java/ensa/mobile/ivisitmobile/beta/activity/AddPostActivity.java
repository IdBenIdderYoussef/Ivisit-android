package ensa.mobile.ivisitmobile.beta.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import ensa.mobile.ivisitmobile.beta.R;

public class AddPostActivity extends AppCompatActivity {


    private ImageView selectImageBtn;
    private ImageView postImageSelected;
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);

        selectImageBtn = findViewById(R.id.selec_image_post);
        postImageSelected = findViewById(R.id.post_image_selected);
    }

    public void toNextSteep(View view){

        Intent nextSteepIntent = new Intent(AddPostActivity.this , AddPostStep2Activity.class);
        startActivity(nextSteepIntent);
    }

    public void selectImage(View view){

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
                System.out.println(imageUri);
                postImageSelected.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
