package com.example.foovery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class My_orders_users extends AppCompatActivity {

    private ArrayList<Order> orders;
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_users);
        orders = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrdersAdapter();
        recyclerView.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("orders")
                .whereEqualTo("uid", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        orders.clear();
                        if(task.isSuccessful() && task.getResult()!=null){
                            for(DocumentSnapshot ds : task.getResult().getDocuments()){
                                Order or = ds.toObject(Order.class);
                                or.setId(ds.getId());
                                orders.add(or);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderRow>{

        @NonNull
        @Override
        public OrderRow onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OrderRow(getLayoutInflater().inflate(R.layout.order_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull OrderRow holder, final int position) {
            holder.id.setText("Order ID #" + orders.get(position).getId());
            holder.amt.setText("Order amount : " + orders.get(position).getCost());
            //holder.dist.setText("");
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(My_orders_users.this, phoneAuthUser.class);
                    intent.putExtra("key1",orders.get(position).getCost());
                    intent.putExtra("key2",orders.get(position).getId());
                    intent.putExtra("key3",orders.get(position).getOtp());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }

        public class OrderRow extends RecyclerView.ViewHolder{
            TextView id, dist, amt;
            View btn;
            OrderRow(View v){
                super(v);
                id = v.findViewById(R.id.orderID);
                amt = v.findViewById(R.id.orderAmt);
                dist = v.findViewById(R.id.orderDist);
                btn = v.findViewById(R.id.confirmBtn);
            }
        }
    }
}
