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
import android.widget.Toast;

import java.util.Random;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class orderFoodU extends AppCompatActivity {
    TextView textView ;
    double cost;
    Button btnClick;
    CheckBox  cb1,cb2,cb3,cb4,cb5,cb6,cb7,cb8,cb9,cb10;
    Location val1;
    ArrayList<HashMap<String, Object>> list;
    double value;

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
        value = intent.getDoubleExtra("key", 0.0);
        val1 = intent.getParcelableExtra("dest");
        String value1 = "Delivery Charges = "+value;
        textView.setText(value1);


        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int s1=0,s2=0,s3=0,s4=0,s5=0,s6=0,s7=0,s8=0,s9=0,s10=0;
                list = new ArrayList<>();
                if(cb1.isChecked()){
                    s1 = s1+ Integer.parseInt(cb1.getText().toString());
                    HashMap<String, Object> temp = new HashMap<>();
                    temp.put("id","Palak Paneer + Roti");
                    temp.put("qty", "Full Plate");
                    temp.put("cost", cb1.getText());
                    list.add(temp);
                }
                if(cb2.isChecked()){
                    s2 = s2+ Integer.parseInt(cb2.getText().toString());
                    HashMap<String, Object> temp1 = new HashMap<>();
                    temp1.put("id","Rajma Chawal");
                    temp1.put("qty", "Full Plate");
                    temp1.put("cost", cb2.getText());
                    list.add(temp1);
                }
                if(cb3.isChecked()){
                    s3 = s3+ Integer.parseInt(cb3.getText().toString());
                    HashMap<String, Object> temp2 = new HashMap<>();
                    temp2.put("id","Palak Paneer + Roti");
                    temp2.put("qty", "Half Plate");
                    temp2.put("cost", cb3.getText());
                    list.add(temp2);
                }
                if(cb4.isChecked()){
                    s4 = s4+ Integer.parseInt(cb4.getText().toString());
                    HashMap<String, Object> temp2 = new HashMap<>();
                    temp2.put("id","Rajma Chawal");
                    temp2.put("qty", "Half Plate");
                    temp2.put("cost", cb4.getText());
                    list.add(temp2);
                }  if(cb5.isChecked()){
                    s5 = s5+ Integer.parseInt(cb5.getText().toString());
                    HashMap<String, Object> temp2 = new HashMap<>();
                    temp2.put("id","Dal Makhani + Rice");
                    temp2.put("qty", "Half Plate");
                    temp2.put("cost", cb5.getText());
                    list.add(temp2);
                }  if(cb6.isChecked()){
                    s6 = s6+ Integer.parseInt(cb6.getText().toString());
                    HashMap<String, Object> temp2 = new HashMap<>();
                    temp2.put("id","Dal Makhani + Rice");
                    temp2.put("qty", "Full Plate");
                    temp2.put("cost", cb6.getText());
                    list.add(temp2);
                }  if(cb7.isChecked()){
                    s7 = s7+ Integer.parseInt(cb7.getText().toString());
                    HashMap<String, Object> temp2 = new HashMap<>();
                    temp2.put("id","Raita");
                    temp2.put("qty", "Half Plate");
                    temp2.put("cost", cb7.getText());
                    list.add(temp2);
                }  if(cb8.isChecked()){
                    s8 = s8+ Integer.parseInt(cb8.getText().toString());
                    HashMap<String, Object> temp2 = new HashMap<>();
                    temp2.put("id","Raita");
                    temp2.put("qty", "Full Plate");
                    temp2.put("cost", cb8.getText());
                    list.add(temp2);
                }  if(cb9.isChecked()){
                    s9 = s9+ Integer.parseInt(cb9.getText().toString());
                    HashMap<String, Object> temp2 = new HashMap<>();
                    temp2.put("id","Gajar Ka Halwa");
                    temp2.put("qty", "Half Plate");
                    temp2.put("cost", cb9.getText());
                    list.add(temp2);
                }  if(cb10.isChecked()){
                    s10 = s10+ Integer.parseInt(cb10.getText().toString());
                    HashMap<String, Object> temp2 = new HashMap<>();
                    temp2.put("id","Gajar Ka Halwa");
                    temp2.put("qty", "Full Plate");
                    temp2.put("cost", cb10.getText());
                    list.add(temp2);
                }
                int sum=s1+s2+s3+s4+s5+s6+s7+s8+s9+s10;

                value = getIntent().getDoubleExtra("key", 0.0);
                cost = sum+value;
                value = 2*value;

                final CheckBox offer = findViewById(R.id.checkBox);
                if(offer.isChecked()){
                    FirebaseFirestore.getInstance().collection("confirmed_orders")
                            .whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .whereGreaterThan("id", System.currentTimeMillis()-2592000000l)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        if(task.getResult().getDocuments().size()>20){
                                            cost=(0.8*cost);
                                            placeOrder();
                                        }
                                        else{
                                            offer.setChecked(false);
                                            Toast.makeText(orderFoodU.this, "Sorry you can't avail the offer as you have only "+task.getResult().getDocuments().size()+" orders in the last month", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Try again later", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else placeOrder();
            }
        });
    }

    public void placeOrder(){
        Random rand = new Random();
        final String ot=String.format("%04d", rand.nextInt(10000));
        final String ot2 = String.format("%04d", rand.nextInt(10000));
        final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String orderId = System.currentTimeMillis()+"";
        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("orders").document(orderId);
        final Map<String, Object> order = new HashMap<>();
        order.put("uid", userID);
        order.put("cost", cost);
        order.put("ord", list);
        order.put("Lat",val1.getLatitude());
        order.put("Lon",val1.getLongitude());
        order.put("otp",ot);
        order.put("otp2",ot2);
        order.put("userPhone",null);
        order.put("riderPhone", null);
        order.put("locDist", value);
        documentReference.set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Order", "Order successfully created" + userID);
                Intent intent = new Intent(orderFoodU.this, phoneAuthUser.class);
                intent.putExtra("key1",cost);
                intent.putExtra("key2",orderId);
                intent.putExtra("key3",ot);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("anhfb", "onFailure: " + e.toString());
            }
        });
    }
}
