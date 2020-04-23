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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class userSignin extends AppCompatActivity {

    EditText  emailID, password;
    TextView btnUserSignIn;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signin);

        emailID = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        btnUserSignIn = findViewById(R.id.button5);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser!= null){
                    Toast.makeText(userSignin.this,"You are logged in " , Toast.LENGTH_SHORT).show();
                    FirebaseFirestore.getInstance().collection("users")
                            .document(mFirebaseUser.getUid())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful() && task.getResult().exists()){
                                        Intent intent = new Intent(userSignin.this, homeUser.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(userSignin.this, "User account not found, please register first", Toast.LENGTH_SHORT);
                                        startActivity(new Intent(userSignin.this, userSignUp.class));
                                    }
                                }
                            });
                }
            }
        };

        btnUserSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailID.getText().toString();
                String pwd = password.getText().toString();

                if (email.isEmpty()) {
                    emailID.setError("please enter a valid email ID");
                    emailID.requestFocus();
                }
                else if (pwd.isEmpty()) {
                    password.setError("please enter a valid password");
                    password.requestFocus();
                }
                else {
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(userSignin.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListner);
    }
}
