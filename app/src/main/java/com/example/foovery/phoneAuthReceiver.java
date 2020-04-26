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

public class phoneAuthReceiver extends AppCompatActivity {

    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 0;
    EditText phoneNo,otp;
    Button btnPhone,btnOtp;
    FirebaseAuth mAuth;
    TextView confirm;
    Order order;
    String message,message2,name;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth_receiver);

        phoneNo = findViewById(R.id.editText9);
        otp = findViewById(R.id.editText12);
        btnPhone =findViewById(R.id.button10);
        btnOtp = findViewById(R.id.button11);
        mAuth = FirebaseAuth.getInstance();
        confirm= findViewById(R.id.textView13);

        order = (Order) getIntent().getSerializableExtra("order");

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore.getInstance()
                        .collection("orders").document(order.id).update("riderPhone",phoneNo.getText().toString());
                order.setRiderPhone(phoneNo.getText().toString());
                confirm.setText("Please wait for the user to confirm..");
                phoneNumber = order.getUserPhone();
                message = "For your Order ID "+ order.id +" the OTP is "+order.otp;
                if (phoneNumber == null || phoneNumber.length() == 0) {
                    Toast.makeText(phoneAuthReceiver.this, "The user has not yet entered his/her number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ContextCompat.checkSelfPermission(phoneAuthReceiver.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(phoneAuthReceiver.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                    Toast.makeText(phoneAuthReceiver.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                }
                else {
                    ActivityCompat.requestPermissions(phoneAuthReceiver.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},
                            SEND_SMS_PERMISSION_REQUEST_CODE);
                }
            }
        });

        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order.getOtp2().equals(otp.getText().toString())) {
                    final ProgressDialog dialog = new ProgressDialog(phoneAuthReceiver.this);
                    dialog.setMessage("Please wait");
                    dialog.setCancelable(false);
                    dialog.show();
                    FirebaseFirestore.getInstance().collection("riders").document(mAuth.getCurrentUser().getUid())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                DocumentSnapshot ds = task.getResult();
                                name = ds.get("name").toString();
                                message2 = "Your Order #" + order.id + " is being delivered by" + name + " - " + order.riderPhone;
                                if (phoneNumber == null || phoneNumber.length() == 0) {
                                    Toast.makeText(phoneAuthReceiver.this, "The user has not yet entered his/her number", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (ContextCompat.checkSelfPermission(phoneAuthReceiver.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(phoneNumber, null, message2, null, null);
                                    Toast.makeText(phoneAuthReceiver.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                                    order.setDid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    FirebaseFirestore.getInstance()
                                            .collection("confirmed_orders")
                                            .document(order.getId())
                                            .set(order);
                                    FirebaseFirestore.getInstance().collection("orders")
                                            .document(order.getId())
                                            .delete();
                                    Intent f = new Intent(phoneAuthReceiver.this, Main2ActivityReceiver.class);
                                    startActivity(f);
                                    finish();
                                } else {
                                    ActivityCompat.requestPermissions(phoneAuthReceiver.this, new String[]{Manifest.permission.SEND_SMS},
                                            SEND_SMS_PERMISSION_REQUEST_CODE);
                                }
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(phoneAuthReceiver.this, "Please enter correct OTP", Toast.LENGTH_SHORT).show();
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



