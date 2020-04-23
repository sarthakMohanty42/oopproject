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
import android.widget.TextView;
import android.widget.Toast;

/*import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;*/
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

import java.util.HashMap;
import java.util.Map;

public class userSignUp extends AppCompatActivity {
    public static final String TAG = "TAG";
    private static final String TAG1 = "FacebookAuthentication";
    EditText name, phoneNumber, emailID, password;
    TextView btnUserSignUp;
    Button btnAlreadySignIn;
    Button btnSignOut;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fStore;
    String userID;
    /*CallbackManager mCallbackManager;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    LoginButton loginButton;
    AccessTokenTracker mAccessTokenTracker;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        btnSignOut = findViewById(R.id.button8);
        emailID = findViewById(R.id.editText);
        password= findViewById(R.id.editText2);
        name=findViewById(R.id.editText7);
        phoneNumber = findViewById(R.id.editText8);
        fStore= FirebaseFirestore.getInstance();
        btnUserSignUp = findViewById(R.id.button5);
        btnAlreadySignIn = findViewById(R.id.button6);
        mFirebaseAuth = FirebaseAuth.getInstance();
        /*FacebookSdk.sdkInitialize(getApplicationContext());

        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        mCallbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
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

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user1 = firebaseAuth.getCurrentUser();
                if (user1 != null) {
                    updateUI(user1);
                } else {
                    updateUI(null);
                }
            }
        };

        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    mFirebaseAuth.signOut();
                }
            }
        };*/

        btnSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(userSignUp.this, MainActivity.class);
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
                    else if((email.isEmpty()&&pwd.isEmpty())||phn.isEmpty()||nam.isEmpty()){
                        Toast.makeText(userSignUp.this,"Fields are empty!" , Toast.LENGTH_SHORT).show();
                    }
                    else if(!(email.isEmpty()&&pwd.isEmpty())) {
                        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                            userID = mFirebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", nam);
                            user.put("phone", phn);
                            user.put("email", email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess:user profile is created  for" + userID);
                                    Intent intent = new Intent(userSignUp.this, homeUser.class);
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
                            mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(userSignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(userSignUp.this, "Sign Up unsuccessful ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        userID = mFirebaseAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference = fStore.collection("users").document(userID);
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("name", nam);
                                        user.put("phone", phn);
                                        user.put("email", email);
                                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "onSuccess:user profile is created  for" + userID);
                                                Intent intent = new Intent(userSignUp.this, homeUser.class);
                                                startActivity(intent);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "onFailure: " + e.toString());
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                    else{
                        Toast.makeText(userSignUp.this,"some error occured , please try again" , Toast.LENGTH_SHORT).show();
                    }
                }
        });

        btnAlreadySignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userSignUp.this, userSignin.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /*private void handleFacebookToken(AccessToken token){
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
                    Toast.makeText(userSignUp.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                    updateUI(null);
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
        final String email = emailID.getText().toString();
        String pwd = password.getText().toString();
        final String nam = name.getText().toString();
        final String phn = phoneNumber.getText().toString();
        if(user1 != null){
            userID = mFirebaseAuth.getCurrentUser().getUid();
            DocumentReference documentReference=fStore.collection("users").document(userID);
            Map<String,Object> user =new HashMap<>();
            user.put("name",nam);
            user.put("phone",phn);
            user.put("email",email);
            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG1, "onSuccess:user profile is created  for"+userID);
                    Intent intent = new Intent(userSignUp.this, homeUser.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(userSignUp.this, "Failed to create facebook account", Toast.LENGTH_SHORT).show();
                    Log.d(TAG1, "onFailure: "+e.toString());
                }
            });
        }
        else {
            Toast.makeText(userSignUp.this,"Sign Up unsuccessful " , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    protected void onStop(){
        super.onStop();

        if(mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }*/
}

