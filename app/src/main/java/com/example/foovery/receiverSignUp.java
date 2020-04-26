package com.example.foovery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class receiverSignUp extends AppCompatActivity {

    public static final String TAG = "TAG";
    private static final String TAG1 = "FacebookAuthentication";
    EditText name, phoneNumber, emailID, password;
    Button btnUserSignUp , btnAlreadySignIn,btnSignOut;
    FirebaseFirestore fStore;
    String riderID;
    FirebaseAuth mFirebaseAuth;
    CallbackManager mCallbackManager;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    AccessTokenTracker mAccessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_login);
        btnSignOut= findViewById(R.id.button3);
        emailID = findViewById(R.id.editText3);
        password= findViewById(R.id.editText4);
        name=findViewById(R.id.editText6);
        phoneNumber = findViewById(R.id.editText5);
        fStore= FirebaseFirestore.getInstance();
        btnUserSignUp = findViewById(R.id.button4);
        btnAlreadySignIn = findViewById(R.id.button7);
        mFirebaseAuth = FirebaseAuth.getInstance();

        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "onSuccess" + loginResult);
                        handleFacebookToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "onError" + error);
                    }
                });
                LoginManager.getInstance().logInWithReadPermissions(receiverSignUp.this, Arrays.asList("email", "public_profile"));
            }
        });

        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    mFirebaseAuth.signOut();
                }
            }
        };

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(receiverSignUp.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnUserSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailID.getText().toString();
                String pwd = password.getText().toString();
                final String nam = name.getText().toString();
                final String phn = phoneNumber.getText().toString();
                if(email.isEmpty()){
                    emailID.setError("please enter a valid email ID");
                    emailID.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("please enter a valid password");
                    password.requestFocus();
                }
                else{
                    if(mFirebaseAuth.getCurrentUser()!=null){
                        final String riderID = mFirebaseAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("riders").document(riderID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("name", nam);
                        user.put("phone", phn);
                        user.put("email", email);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess:user profile is created  for" + riderID);
                                Intent intent = new Intent(receiverSignUp.this, homeReceiver.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.toString());
                            }
                        });

                    }
                    else {
                        mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(receiverSignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(receiverSignUp.this, "Sign Up unsuccessful ", Toast.LENGTH_SHORT).show();
                                } else {
                                    riderID = mFirebaseAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = fStore.collection("riders").document(riderID);
                                    Map<String, Object> rider = new HashMap<>();
                                    rider.put("name", nam);
                                    rider.put("phone", phn);
                                    rider.put("email", email);
                                    documentReference.set(rider).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "onSuccess:rider profile is created for rider ID " + riderID);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: " + e.toString());
                                        }
                                    });

                                    Intent intent = new Intent(receiverSignUp.this, homeReceiver.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                }
            }
        });

        btnAlreadySignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(receiverSignUp.this, receiverSignIn.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void handleFacebookToken(AccessToken token){
        Log.d(TAG1,"handleFacebookToken" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful()){
                    Log.d(TAG1,"Sign with credential successful");
                    FirebaseUser user1 = mFirebaseAuth.getCurrentUser();
                    updateUI(user1);
                }
                else{
                    Log.d(TAG1,"SignIn Unsuccessful",task.getException());
                    Toast.makeText(receiverSignUp.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
//                    updateUI(null);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI(FirebaseUser user1){
        if(user1 != null){
            final String em = user1.getEmail();
            final String na = user1.getDisplayName();
            final String ph = user1.getPhoneNumber();
            riderID = user1.getUid();
            DocumentReference documentReference=fStore.collection("riders").document(riderID);
            Map<String,Object> user =new HashMap<>();
            user.put("name",na);
            user.put("phone",ph);
            user.put("email",em);
            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG1, "onSuccess:user profile is created  for"+ riderID);
                    Intent intent = new Intent(receiverSignUp.this, homeReceiver.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(receiverSignUp.this, "Failed to create facebook account", Toast.LENGTH_SHORT).show();
                    Log.d(TAG1, "onFailure: "+e.toString());
                }
            });
        }
        else {
            Toast.makeText(receiverSignUp.this,"Sign Up unsuccessful " , Toast.LENGTH_SHORT).show();
        }
    }
}


