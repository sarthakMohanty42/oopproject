package com.example.foovery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;   ///////////////////////////////////////////////////////////
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class homeReceiver extends FragmentActivity implements  OnMapReadyCallback {

    GoogleMap map;
    // SupportMapFragment mapFragment;
    //SearchView searchView;
    Handler mHandler = new Handler();
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;
    private RecyclerView recyclerView;
    private ArrayList<Order> orders;
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_receiver);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        orders = new ArrayList<>();
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        orders.clear();
                        if(task.isSuccessful() && task.getResult()!=null){
                            for(DocumentSnapshot ds:task.getResult().getDocuments()){
                                Order or = ds.toObject(Order.class);
                                or.setId(ds.getId());
                                orders.add(or);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
        /*
        searchView= findViewById(R.id.sv_location) ;
        mapFragment =  (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override

            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if(location!=null||!location.equals("")){
                    Geocoder geocoder= new Geocoder(homeReceiver.this); /////////// jo bhi address enter kia usko longitude and latitude k cordinates me convert karta hai...
                    try{
                        addressList= geocoder.getFromLocationName(location ,1);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                    assert addressList != null;
                    Address address=addressList.get(0);
                    LatLng latLng= new LatLng(address.getLatitude(),address.getLongitude());  //LatLng latLng= new LatLng(address.getLatitude(),address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                    Double lat1= address.getLatitude();  /////////////////////////// for calculating the distance
                    Double long1= address.getLongitude();
                    Location startPoint=new Location("LocationA");
                    startPoint.setLatitude(lat1);
                    startPoint.setLongitude(long1);

                    Location endPoint=new Location("locationA");
                    endPoint.setLatitude(17.4399);
                    endPoint.setLongitude(78.4983);

                    double distance=startPoint.distanceTo(endPoint);
                    distance = distance/1000;
                    String dist = "distance = "+ Double.toString(distance);
                    Toast.makeText(homeReceiver.this,dist , Toast.LENGTH_SHORT).show();


                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        mapFragment.getMapAsync(this);

*/
    }

    private void fetchLastLocation() {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task= fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLocation=location;
                    //Toast.makeText(getApplicationContext(),text:currentLocation.getLatitude()+""+currentLocation.getLongitude(),Toast.LENGTH_SHORT.show());
                    Toast.makeText(homeReceiver.this,currentLocation.getLatitude()+""+currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(homeReceiver.this);

                    Double lat1= currentLocation.getLatitude();  /////////////////////////// for calculating the distance
                    Double long1= currentLocation.getLongitude();
                    GlobalVariable.receiverLat=lat1;
                    GlobalVariable.receiverLong=long1;
                    /*Location startPoint=new Location("LocationA");
                    startPoint.setLatitude(lat1);
                    startPoint.setLongitude(long1);

                    Location endPoint=new Location("locationA");
                    endPoint.setLatitude(17.4399);
                    endPoint.setLongitude(78.4983);
                    double distance=startPoint.distanceTo(endPoint);
                    distance=distance/1000;
                    final String dist = Double.toString(distance);
*/
                    mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            Intent i = new Intent(homeReceiver.this,phoneAuthReceiver.class);

                            startActivity(i);
                        }

                    }, 10000L);
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("you are here!!");
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        googleMap.addMarker(markerOptions);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        public MyAdapter(){

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
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.id.setText("Order ID #"+orders.get(position).getUid());
            holder.amt.setText("Order amount : " + orders.get(position).getCost());
            holder.dist.setText("");
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView id, dist, amt;
            public MyViewHolder(View v){
                super(v);
                id = v.findViewById(R.id.orderID);
                amt = v.findViewById(R.id.orderAmt);
                dist = v.findViewById(R.id.orderDist);
            }
        }
    }
}





