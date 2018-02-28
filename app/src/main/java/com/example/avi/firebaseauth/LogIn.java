package com.example.avi.firebaseauth;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    EditText editTextEmail, editTextPwd;
    TextView textViewSignup;
    Button buttonLogin;
    String email, pwd;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        editTextEmail = (EditText) findViewById(R.id.loginAct_et_email);
        editTextPwd = (EditText) findViewById(R.id.loginAct_et_pwd);
        textViewSignup = (TextView) findViewById(R.id.tv_signup);
        buttonLogin = (Button) findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
       progressBar = (ProgressBar) findViewById(R.id.progressBar_login);

    }

    private void checkConn() {
        boolean checkConnection = new ConnectionCheck().checkConnection(this);
        if (checkConnection) {
            showDialog();
        }
    }

    private void showDialog() {
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

    private void loginUser() {
        email = editTextEmail.getText().toString().trim();
        pwd = editTextPwd.getText().toString().trim();

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
        if (pwd.isEmpty()) {
            editTextPwd.setError("Password Requied");
            editTextPwd.requestFocus();
            return;
        }
        if (pwd.length() < 6) {
            editTextPwd.setError("Minimum length of password should be 6");
            editTextPwd.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    Intent i = new Intent(getApplicationContext(), SelectProfilePic.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } else {
                    if (!(task.getException() instanceof FirebaseAuthUserCollisionException)) {
                        Toast.makeText(getApplicationContext(), "You are not registered user\nSign Up first", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred while Logging in", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkConn();
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), SelectProfilePic.class));
        }
    }

    @Override
    protected void onResume() {
        super.onRestart();
        checkConn();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                boolean checkConnection = new ConnectionCheck().checkConnection(this);
                if (checkConnection) {
                    Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_LONG).show();
                } else {
                    loginUser();
                }
                break;

            case R.id.tv_signup:
                Intent i = new Intent(this, SignUp.class);
                startActivity(i);
                finish();
                break;
        }
    }
}

