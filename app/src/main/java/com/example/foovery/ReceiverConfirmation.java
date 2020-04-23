package com.example.foovery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.nio.charset.CharsetEncoder;
import java.util.Random;

import javax.annotation.Nullable;

public class ReceiverConfirmation extends AppCompatActivity {
        Button checkOrderStatus,confirmOrder;
        EditText phoneNo;
        TextView indication;
       // FirebaseAuth fAuth;
       // FirebaseFirestore fStore;
       // String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_confirmation);

        phoneNo= findViewById(R.id.editText11);
        checkOrderStatus = findViewById(R.id.button9);
        confirmOrder= findViewById(R.id.button13);
       // indication = findViewById(R.id.textView10);
        phoneNo.setText(GlobalVariable.phoneUser);

/*
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        userID=fAuth.getCurrentUser().getUid();
        DocumentReference documentReference=fStore.collection("users").document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                phoneNo.setText(documentSnapshot.getString("phone"));
            }
        });

*/
        checkOrderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String ya= "Customer here!!!";
                    //indication.setText(ya);
                phoneNo.setText(GlobalVariable.phoneUser);



            }
        });

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting intent and PendingIntent instance


                String value=GlobalVariable.phoneUser;
                String msg = "Order confirm with OrderID: "+getAlphaNumericString(10)+" Rider's phone number: "+GlobalVariable.phoneRider ;

                if(ActivityCompat.checkSelfPermission(ReceiverConfirmation.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ReceiverConfirmation.this,new String[]
                            {Manifest.permission.SEND_SMS},1);

                }
                    else {
                   // Intent intent = new Intent(getApplicationContext(), ReceiverConfirmation.class);
                  //  PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                    //Get the SmsManager instance and call the sendTextMessage method to send message
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(value, null, msg, null, null);

                    Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ReceiverConfirmation.this, Main2Activity.class);
                    startActivity(i);
                }
            }
        });



    }
/*
    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }*/

    public static String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
