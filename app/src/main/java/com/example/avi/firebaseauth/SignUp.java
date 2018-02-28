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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    EditText editTextUserName, editTextEmail, editTextPwd1, editTextPwd2;
    Button buttonSignup;
    String userName, email, pwd1, pwd2;
    RadioGroup rdGrp_gender;
    RadioButton rdButton_male, rdButton_female;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextUserName = (EditText) findViewById(R.id.signUpAct_et_fullname);
        editTextEmail = (EditText) findViewById(R.id.signUpAct_et_email);
        editTextPwd1 = (EditText) findViewById(R.id.signUpAct_et_pwd1);
        editTextPwd2 = (EditText) findViewById(R.id.signUpAct_et_pwd2);
        buttonSignup = (Button) findViewById(R.id.btn_signup);
        rdGrp_gender = (RadioGroup) findViewById(R.id.gender_radio_group);
        rdButton_male = (RadioButton) findViewById(R.id.gender_male);
        rdButton_female = (RadioButton) findViewById(R.id.gender_female);
        buttonSignup.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_signUp);
        checkConn();
        mAuth = FirebaseAuth.getInstance();

        rdGrp_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rdButton_male.isChecked()) {
                    rdButton_male.setTextColor(Color.parseColor("#338DFF"));
                    rdButton_female.setTextColor(Color.WHITE);
                }
                if ((rdButton_female).isChecked()) {
                    rdButton_female.setTextColor(Color.parseColor("#338DFF"));
                    rdButton_male.setTextColor(Color.WHITE);
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
        userName = editTextUserName.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        pwd1 = editTextPwd1.getText().toString().trim();
        pwd2 = editTextPwd2.getText().toString().trim();

        if(rdGrp_gender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Please select your gender", Toast.LENGTH_SHORT).show();
            rdButton_male.setError("Please select your gender");
            rdButton_male.requestFocus();
            return;
        }
        if (userName.isEmpty()) {
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
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    Intent i = new Intent(getApplicationContext(), SelectProfilePic.class);
                    startActivity(i);
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered\nTry to Login", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onRestart();
        checkConn();
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
