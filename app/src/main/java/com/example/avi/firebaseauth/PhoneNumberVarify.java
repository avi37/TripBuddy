package com.example.avi.firebaseauth;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class PhoneNumberVarify extends AppCompatActivity implements View.OnClickListener {

    EditText editText_phoneNo, editText_OtpField;
    Button btn_send, btn_varify;
    private String mVerificationId;

    FirebaseAuth mAuth;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_varify);
        editText_phoneNo = (EditText) findViewById(R.id.phoneAct_et_mobileNo);
        editText_OtpField = (EditText) findViewById(R.id.phoneAct_et_otpField);
        btn_send = (Button) findViewById(R.id.phoneAct_btn_sendOTP);
        btn_varify = (Button) findViewById(R.id.phoneAct_btn_varify);
        mAuth = FirebaseAuth.getInstance();

        btn_send.setOnClickListener(this);
        btn_varify.setOnClickListener(this);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                System.out.println("onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
                editText_OtpField.setText(credential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                System.out.println("onVerificationFailed\n"+e.getMessage().toString());

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                System.out.println("onCodeSent:" + verificationId);
                Toast.makeText(getApplicationContext(), "OTP Sent", Toast.LENGTH_SHORT).show();

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "signInWithCredential:success", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(getApplicationContext(), "signInWithCredential:failure" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.phoneAct_btn_sendOTP:
                validateNumber();

            case R.id.phoneAct_btn_varify:
                String code = editText_OtpField.getText().toString();
                if (code.isEmpty()) {
                    editText_OtpField.setError("Cannot be empty.");
                    return;
                }
                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
        }
    }

    private void startPhoneNumberVerification(String _number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + _number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                PhoneNumberVarify.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        Toast.makeText(getApplicationContext(), "started", Toast.LENGTH_LONG).show();
        //editText_OtpField.setText("");
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        Toast.makeText(getApplicationContext(), "Varified", Toast.LENGTH_LONG).show();
        signInWithPhoneAuthCredential(credential);
    }

    private void validateNumber() {
        String number = editText_phoneNo.getText().toString();
        if ((number.equals(""))) {
            editText_phoneNo.setError("Please enter your number");
            editText_phoneNo.requestFocus();
            return;
        } else {
            if (!(isValidMobile(number))) {
                editText_phoneNo.setError("Enter a valid number");
                editText_phoneNo.requestFocus();
                return;
            } else {
                startPhoneNumberVerification(number);
            }
        }
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
