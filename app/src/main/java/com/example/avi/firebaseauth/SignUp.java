package com.example.avi.firebaseauth;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    static int count = 1;
    EditText editTextUserName, editTextEmail, editTextPwd1, editTextPwd2, editTextFullName;
    Button buttonSignup;
    String email, pwd1, pwd2, fullName, gender;
    RadioGroup rdGrp_gender;
    RadioButton rdButton_male, rdButton_female;
    ProgressBar progressBar;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = (EditText) findViewById(R.id.signUpAct_et_email);
        editTextPwd1 = (EditText) findViewById(R.id.signUpAct_et_pwd1);
        editTextPwd2 = (EditText) findViewById(R.id.signUpAct_et_pwd2);
        editTextFullName = (EditText) findViewById(R.id.signUpAct_et_fullname);
        buttonSignup = (Button) findViewById(R.id.btn_signup);
        rdGrp_gender = (RadioGroup) findViewById(R.id.gender_radio_group);
        rdButton_male = (RadioButton) findViewById(R.id.gender_male);
        rdButton_female = (RadioButton) findViewById(R.id.gender_female);
        buttonSignup.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_signUp);
        checkConn();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        rdGrp_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rdButton_male.isChecked()) {
                    rdButton_male.setTextColor(Color.parseColor("#338DFF"));
                    rdButton_female.setTextColor(Color.WHITE);
                    gender = "male";
                }
                if ((rdButton_female).isChecked()) {
                    rdButton_female.setTextColor(Color.parseColor("#338DFF"));
                    rdButton_male.setTextColor(Color.WHITE);
                    gender = "female";
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), LogIn.class));
        finish();
    }

    private void checkConn() {
        boolean checkConnection = new ConnectionCheck().checkConnection(this);
        if (checkConnection) {
            showConnectionDialog();
        }
    }

    private void showConnectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Not Connected")
                .setTitle("No Internet Connection");

        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.exit(0);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void registerUser() {
        email = editTextEmail.getText().toString();
        pwd1 = editTextPwd1.getText().toString();
        pwd2 = editTextPwd2.getText().toString();
        fullName = editTextFullName.getText().toString();

        if (rdGrp_gender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Please select your gender", Toast.LENGTH_SHORT).show();
            rdButton_male.setError("Please select your gender");
            rdButton_male.requestFocus();
            return;
        }
        if (fullName.isEmpty()) {
            editTextUserName.setError("Full Name Required");
            editTextUserName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextEmail.setError("E-mail ID Requied");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid E-mail address");
            editTextEmail.requestFocus();
            return;
        }
        if (pwd1.isEmpty()) {
            editTextPwd1.setError("Password Requied");
            editTextPwd1.requestFocus();
            return;
        }
        if (pwd1.length() < 6) {
            editTextPwd1.setError("Minimum length of password should be 6");
            editTextPwd1.requestFocus();
            return;
        }
        if (pwd2.isEmpty()) {
            editTextPwd2.setError("Confirmation of Password Requied");
            editTextPwd2.requestFocus();
            return;
        }
        if (pwd2.length() < 6) {
            editTextPwd2.setError("Minimum length of password should be 6");
            editTextPwd2.requestFocus();
            return;
        }
        if (!(pwd1.equals(pwd2))) {
            editTextPwd2.setError("Password must be same");
            editTextPwd2.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, pwd1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                    registerToFirestore();
                    Intent i = new Intent(getApplicationContext(), MainNavigation.class);
                    startActivity(i);
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered!\nTry to Login", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void registerToFirestore() {
        FirebaseUser user = mAuth.getCurrentUser();
        final String username = user.getDisplayName();
        count += count;
        Map<String, Object> userDataObjj = new HashMap<>();
        userDataObjj.put("user", username);
        userDataObjj.put("Full Name", fullName);
        userDataObjj.put("Gender", gender);
        userDataObjj.put("ID", count);

        db.collection("User Database")
                .add(userDataObjj)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        storeToUsersDB(username);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Error while registration", Toast.LENGTH_SHORT).show();
                count -= count;
            }
        });
    }

    private void storeToUsersDB(String username) {
        Map<String, Object> userIdPwdObjj = new HashMap<>();
        userIdPwdObjj.put("Username", username);
        userIdPwdObjj.put("Password", pwd1);

        db.collection("Users")
                .add(userIdPwdObjj)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Error while registration", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onRestart();
        //checkConn();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                boolean checkConnection = new ConnectionCheck().checkConnection(this);
                if (checkConnection) {
                    Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_LONG).show();
                } else {
                    registerUser();
                }
                break;

        }
    }

}
