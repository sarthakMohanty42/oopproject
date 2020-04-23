package com.example.foovery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class phoneAuthReceiver extends AppCompatActivity {

    EditText phoneNo,otp;
    Button btnPhone,btnOtp;
    FirebaseAuth mAuth;
    String codeSent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth_receiver);

        phoneNo = findViewById(R.id.editText9);
        otp = findViewById(R.id.editText10);
        btnPhone =findViewById(R.id.button10);
        btnOtp = findViewById(R.id.button11);
        mAuth = FirebaseAuth.getInstance();

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendVerificationCode();
                GlobalVariable.phoneRider=phoneNo.getText().toString();
            }
        });

        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignInCode();
            }
        });
    }

    public void verifySignInCode(){
        String code = otp.getText().toString();
        if(code.isEmpty()){
            otp.setError("please enter the phone number");
            otp.requestFocus();
            return;
        }
        else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            ///////////// here you can open a new activity ........
                            Toast.makeText(phoneAuthReceiver.this, "Registered", Toast.LENGTH_SHORT).show();
                            //GlobalVariable.phoneRider=phoneNo.getText().toString();
                            Intent i = new Intent(phoneAuthReceiver.this,ReceiverConfirmation.class);

                            startActivity(i);
                           /* Intent i = new Intent(phoneAuthUser.this,ReceiverConfirmation.class);
                            String phone = phoneNo.getText().toString();
                            i.putExtra("key3",phone);
                            */

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(phoneAuthReceiver.this, "invalid otp ", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }

    public void sendVerificationCode(){

        String phone = phoneNo.getText().toString();
        if(phone.isEmpty()){
            phoneNo.setError("please enter the phone number");
            phoneNo.requestFocus();
            return;
        }

        if(phone.length()<13){                                        ////////////// as we need to enter the phone number along with country code so for india +91 ....
            phoneNo.setError("please enter valid phone number");
            phoneNo.requestFocus();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;            ///////////////////// the code sent to the phone number from app via sms.....
        }
    };
}



