package com.example.foovery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class orderFoodU extends AppCompatActivity {
    TextView textView , cost;
    Button btnClick;
    CheckBox  cb1,cb2,cb3,cb4,cb5,cb6,cb7,cb8,cb9,cb10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food_u);
        textView = findViewById(R.id.textView10);

        btnClick = findViewById(R.id.button12);

        cb1 = findViewById(R.id.checkBox1);
        cb2 = findViewById(R.id.checkBox2);
        cb3 = findViewById(R.id.checkBox3);
        cb4 = findViewById(R.id.checkBox4);
        cb5 = findViewById(R.id.checkBox5);
        cb6 = findViewById(R.id.checkBox6);
        cb7 = findViewById(R.id.checkBox7);
        cb8 = findViewById(R.id.checkBox8);
        cb9 = findViewById(R.id.checkBox9);
        cb10 = findViewById(R.id.checkBox10);

        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        final Location val1 = intent.getParcelableExtra("dest");
        String value1 = "Delivery Charges = "+value;
        textView.setText(value1);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int s1=0,s2=0,s3=0,s4=0,s5=0,s6=0,s7=0,s8=0,s9=0,s10=0;
                ArrayList<HashMap<String, Object>> list = new ArrayList<>();
                if(cb1.isChecked()){
                    s1 = s1+ Integer.parseInt(cb1.getText().toString());
                    HashMap<String, Object> temp = new HashMap<>();
                    temp.put("id","s1");
                    temp.put("qty", 5);
                    temp.put("cost", cb1.getText());
                    list.add(temp);
                }
                if(cb2.isChecked()){
                    s2 = s2+ Integer.parseInt(cb2.getText().toString());
                    HashMap<String, Object> temp1 = new HashMap<>();
                    temp1.put("id","s1");
                    temp1.put("qty", 5);
                    temp1.put("cost", cb1.getText());
                    list.add(temp1);
                }
                if(cb3.isChecked()){
                    s3 = s3+ Integer.parseInt(cb3.getText().toString());
                    HashMap<String, Object> temp2 = new HashMap<>();
                    temp2.put("id","s1");
                    temp2.put("qty", 5);
                    temp2.put("cost", cb1.getText());
                    list.add(temp2);
                }
                if(cb4.isChecked()){
                    s4 = s4+ Integer.parseInt(cb4.getText().toString());
                    HashMap<String, Object> temp2 = new HashMap<>();
                    temp2.put("id","s1");
                    temp2.put("qty", 5);
                    temp2.put("cost", cb1.getText());
                    list.add(temp2);
                }  if(cb5.isChecked()){
                    s5 = s5+ Integer.parseInt(cb5.getText().toString());
                    HashMap<String, Object> temp2 = new HashMap<>();
                    temp2.put("id","s1");
                    temp2.put("qty", 5);
                    temp2.put("cost", cb1.getText());
                    list.add(temp2);
                }  if(cb6.isChecked()){
                    s6 = s6+ Integer.parseInt(cb6.getText().toString());
                    HashMap<String, Object> temp2 = new HashMap<>();
                    temp2.put("id","s1");
                    temp2.put("qty", 5);
                    temp2.put("cost", cb1.getText());
                    list.add(temp2);
                }  if(cb7.isChecked()){
                    s7 = s7+ Integer.parseInt(cb7.getText().toString());
                    HashMap<String, Object> temp2 = new HashMap<>();
                    temp2.put("id","s1");
                    temp2.put("qty", 5);
                    temp2.put("cost", cb1.getText());
                    list.add(temp2);
                }  if(cb3.isChecked()){
                    s8 = s8+ Integer.parseInt(cb8.getText().toString());
                    HashMap<String, Object> temp2 = new HashMap<>();
                    temp2.put("id","s1");
                    temp2.put("qty", 5);
                    temp2.put("cost", cb1.getText());
                    list.add(temp2);
                }  if(cb9.isChecked()){
                    s9 = s9+ Integer.parseInt(cb9.getText().toString());
                    HashMap<String, Object> temp2 = new HashMap<>();
                    temp2.put("id","s1");
                    temp2.put("qty", 5);
                    temp2.put("cost", cb1.getText());
                    list.add(temp2);
                }  if(cb10.isChecked()){
                    s10 = s10+ Integer.parseInt(cb10.getText().toString());
                    HashMap<String, Object> temp2 = new HashMap<>();
                    temp2.put("id","s1");
                    temp2.put("qty", 5);
                    temp2.put("cost", cb1.getText());
                    list.add(temp2);
                }
                int sum=s1+s2+s3+s4+s5+s6+s7+s8+s9+s10;

                Double sumd = new Double(sum);
                String foodCost = Double.toString(sumd);
                Intent intent = getIntent();
                String value = intent.getStringExtra("key");
                String value1 = "Delivery Charges = "+value;
                textView.setText(value1);
                Double val= Double.parseDouble(value);
                final double total = sumd+val;
                final String totalCost = Double.toString(total);

                final  String disp = "Total cost = "+value+" + "+foodCost+" = "+totalCost;

                String orderId = System.currentTimeMillis()+"";
                final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference documentReference = FirebaseFirestore.getInstance()
                        .collection("orders").document(orderId);
                Map<String, Object> order = new HashMap<>();
                order.put("uid", userID);
                order.put("cost", disp);
                order.put("ord", list);
                order.put("Lat",val1.getLatitude());
                order.put("Lon",val1.getLongitude());
                documentReference.set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Order", "onSuccess:user profile is created  for" + userID);
                        Intent intent = new Intent(orderFoodU.this, phoneAuthUser.class);
                        intent.putExtra("key2",disp);
                        intent.putExtra("key1",totalCost);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("anhfb", "onFailure: " + e.toString());
                    }
                });
            }
        });
    }
}
