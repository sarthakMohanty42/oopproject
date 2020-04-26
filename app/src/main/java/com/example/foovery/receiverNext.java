package com.example.foovery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.ArrayList;

public class receiverNext extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment mapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    double distance;

    private ArrayList<Marker> markers;
    private RecyclerView recyclerView;
    private ArrayList<Order> orders;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_next);
        mapFragment =  (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        orders = new ArrayList<>();
        markers = new ArrayList<>();
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        Location dest = getIntent().getParcelableExtra("dest");
        LatLng latLng = new LatLng(dest.getLatitude(), dest.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("you are here!!");
        map.addMarker(markerOptions);

        Location source = getIntent().getParcelableExtra("source");
        LatLng Lng = new LatLng(source.getLatitude(), source.getLongitude());
        MarkerOptions marker = new MarkerOptions().position(Lng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("you are here!!");
        map.addMarker(marker);

        distance = dest.distanceTo(source);
        distance=distance/1000;

        FirebaseFirestore.getInstance().collection("orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        orders.clear();
                        for(Marker m:markers)
                            m.remove();
                        markers.clear();
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                                Order or = ds.toObject(Order.class);
                                or.setId(ds.getId());
                                orders.add(or);
                                LatLng latLng = new LatLng(or.getLat(), or.getLon());
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("you are here!!");
                                markers.add(map.addMarker(markerOptions));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        public MyAdapter() {

        }

        @Override
        public int getItemCount() {
            return orders.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(getLayoutInflater().inflate(R.layout.available_order_card, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            holder.id.setText("Order ID #" + orders.get(position).getId());
            holder.amt.setText("Order amount : " + orders.get(position).getCost());
            double dis=distance + orders.get(position).getLocDist();
            holder.dist.setText("Total distance : "+ dis + " kms");
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(receiverNext.this, phoneAuthReceiver.class);
                    intent.putExtra("order", orders.get(position));
                    startActivity(intent);
                }
            });
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView id, dist, amt;
            View btn;

            public MyViewHolder(View v) {
                super(v);
                id = v.findViewById(R.id.orderID);
                amt = v.findViewById(R.id.orderAmt);
                dist = v.findViewById(R.id.orderDist);
                btn = v.findViewById(R.id.confirmBtn);
            }
        }
    }
}

