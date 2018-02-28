package com.example.avi.firebaseauth;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SelectProfilePic extends AppCompatActivity implements View.OnClickListener {

    private static final int CHOOSE_IMAGE = 101;
    ImageView profileImage;
    EditText editTextUserName;
    TextView textView_skip;
    Button buttonSave;
    Bitmap profileImageBitmap;
    ProgressBar progressbar_saveButton, progressbar_loadProfileImage;
    Uri uriProfileImage;
    String profileImageUrl;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_profile_pic);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        profileImage = (ImageView) findViewById(R.id.img_selectImage);
        editTextUserName = (EditText) findViewById(R.id.et_selectUserName);
        buttonSave = (Button) findViewById(R.id.btn_saveProfile);
        progressbar_saveButton = (ProgressBar) findViewById(R.id.progressBar_SelectProfile);
        progressbar_loadProfileImage = (ProgressBar) findViewById(R.id.progressBar_loadImage);
        textView_skip = (TextView) findViewById(R.id.tv_selectProfilePic_skip);
        profileImage.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        textView_skip.setOnClickListener(this);

        loadUserInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), LogIn.class));
        }
    }

    private void checkConn() {
        boolean checkConnection = new ConnectionCheck().checkConnection(this);
        if (checkConnection) {
        }
    }

    private void loadUserInfo() {
        progressbar_loadProfileImage.setVisibility(View.VISIBLE);
        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(profileImage);
            }
            else {
                profileImage.setImageResource(R.mipmap.ic_select_dp);
            }
            if (user.getDisplayName() != null) {
                editTextUserName.setText(user.getDisplayName());
                editTextUserName.setSelection(user.getDisplayName().toString().length());
            }
            progressbar_loadProfileImage.setVisibility(View.GONE);
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select profile picture"), CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                profileImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                profileImage.setImageBitmap(profileImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profileImages/" + user.getDisplayName().toString() + ".jpg");
        if (uriProfileImage != null) {
            profileImageRef.putFile(uriProfileImage).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "Profile Image Uploaded", Toast.LENGTH_SHORT).show();
                            profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        checkConn();
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_selectImage:
                showImageChooser();
                break;

            case R.id.btn_saveProfile:
                checkConn();
                saveUserInfo();
                break;

            case R.id.tv_selectProfilePic_skip:
                Intent i = new Intent(getApplicationContext(), MainNavigation.class);
                startActivity(i);
        }
    }

    private void saveUserInfo() {
        String displayName = editTextUserName.getText().toString();
        if (displayName.isEmpty()) {
            editTextUserName.setError("Username Requied");
            editTextUserName.requestFocus();
            return;
        }
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && profileImageUrl != null) {
            progressbar_saveButton.setVisibility(View.VISIBLE);
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressbar_saveButton.setVisibility(View.GONE);
                                Toast.makeText(getApplication(), "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        storeProfileImage(profileImageBitmap);
        Intent i = new Intent(getApplicationContext(), MainNavigation.class);
        startActivity(i);
        finish();
    }

    private void storeProfileImage(Bitmap bitmapImage) {
        uploadImageToFirebaseStorage();
        try {
            OutputStream output;
            File filepath = Environment.getExternalStorageDirectory();

            // Create a new folder in SD Card
            File dir = new File(filepath.getAbsolutePath()
                    + "/TripBuddy/Profile Image");
            dir.mkdirs();

            // Create a name for the saved image
            File file = new File(dir, user.getDisplayName().toString() +".png");

            // Show a toast message on successful save
            Toast.makeText(getApplicationContext(), "Image Saved to SD Card",
                    Toast.LENGTH_SHORT).show();
            try {

                output = new FileOutputStream(file);

                // Compress into png format image from 0% - 100%
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, output);
                output.flush();
                output.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (
                Exception e
                )

        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }


    }
}