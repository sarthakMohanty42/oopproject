package com.example.foovery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnPlaceOrder , btnReceiveOrder;
    //public void receiveOrder (View view){
        /*EditText dollarAmount= findViewById(R.id.dollaramount);
        String dollars = dollarAmount.getText().toString();
        Double doubleDollars= 71.72*(Double.parseDouble(dollars));

        String toastText= "="+doubleDollars.toString()+"$";

        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();*/
    //   Intent intent = new Intent(this, receiverLogin.class);
    // startActivity(intent);
    //}
    //public static final String MSG = "com.example.p1.ORDER"; /// key created to be sent....
     /*public void placeOrder(View view){
        Intent i = new Intent(this, userLogin.class);
        /*EditText editText1 = findViewById(R.id.editText1);
         EditText editText2 = findViewById(R.id.editText2);
         EditText editText3 = findViewById(R.id.editText3);

         String message = "Order for "  + editText1.getText().toString()+" , "+ editText2.getText().toString() +" & "+editText3.getText().toString() +"  has been sucessfully placed" ;
        intent.putExtra(MSG,message);*/
    // startActivity(i);
    //}*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPlaceOrder =findViewById(R.id.button2);
        btnReceiveOrder= findViewById(R.id.button);

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, userSignUp.class);
                startActivity(i);
            }
        });

        btnReceiveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, receiverSignUp.class);
                startActivity(intent);
            }
        });

    }
}
