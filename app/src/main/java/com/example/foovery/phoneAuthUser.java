package com.example.foovery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class phoneAuthUser extends AppCompatActivity {

    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 0;

    EditText phoneNo,otp;
    Button btnPhone,btnOtp;
    FirebaseAuth mAuth;
    TextView codeSent;
    String message;
    String phoneNumber;
    String name;
    String id;
    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth_user);

        phoneNo = findViewById(R.id.editText9);
        otp = findViewById(R.id.editText10);
        btnPhone =findViewById(R.id.button10);
        btnOtp = findViewById(R.id.button11);

        codeSent = findViewById(R.id.textView11);
        total = findViewById(R.id.textView17);

        double amount = getIntent().getDoubleExtra("key1",0.0);
        String cost = "Total Amount = "+ amount;
        total.setText(cost);

        mAuth = FirebaseAuth.getInstance();
        id = getIntent().getStringExtra("key2");

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance()
                        .collection("orders").document(id).update("userPhone",phoneNo.getText().toString());
                codeSent.setText("You will get the OTP.. Please wait...");
            }
        });

        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    protected void sendMessage(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();
        FirebaseFirestore.getInstance().collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        dialog.dismiss();
                        if(task.isSuccessful() ){
                            DocumentSnapshot ds = task.getResult();
                            name = ds.get("name").toString();
                            dialog.show();
                            FirebaseFirestore.getInstance().collection("orders")
                                    .document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    dialog.dismiss();
                                    if(task.isSuccessful()) {
                                        DocumentSnapshot ds1 = task.getResult();
                                        Order o = ds1.toObject(Order.class);
                                        o.setId(ds1.getId());
                                        phoneNumber = o.getRiderPhone();
                                        message = "Your OTP for Order Id "+ o.getId()+ " is " + o.getOtp2()+ " . You have to deliver order to " + name + " - " + phoneNo.getText();
                                        if (phoneNumber == null || phoneNumber.length() == 0 || message == null || message.length() == 0) {
                                            return;
                                        }

                                        if (ContextCompat.checkSelfPermission(phoneAuthUser.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                                         && ContextCompat.checkSelfPermission(phoneAuthUser.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                                            SmsManager smsManager = SmsManager.getDefault();
                                            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                                            Toast.makeText(phoneAuthUser.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(phoneAuthUser.this, Main2Activity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            ActivityCompat.requestPermissions(phoneAuthUser.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},
                                                    SEND_SMS_PERMISSION_REQUEST_CODE);
                                        }
                                    }
                                }
                            });

                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}


