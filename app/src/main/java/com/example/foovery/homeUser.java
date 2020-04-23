package com.example.foovery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;   ///////////////////////////////////////////////////////////
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import org.w3c.dom.Text;

public class homeUser extends FragmentActivity implements  OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;
    Button btnOrder;
    Location currentLocation, destination;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        checkGPS();
        findViewById(R.id.button16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentLocation==null){
                    Toast.makeText(homeUser.this, "Please select Current Location", Toast.LENGTH_SHORT).show();
                    return;
                }
                Location source=new Location("locationA");
                source.setLatitude(17.4399);
                source.setLongitude(78.4983);
                double distance=currentLocation.distanceTo(source);
                distance=distance/1000;
                final String dist = Double.toString(distance);
                Toast.makeText(homeUser.this,dist , Toast.LENGTH_SHORT).show();
                Intent o = new Intent(homeUser.this, orderFoodU.class);
                o.putExtra("key", dist);
                o.putExtra("dest",currentLocation);
                startActivity(o);
            }
        });
        btnOrder = findViewById(R.id.button15);
        searchView= findViewById(R.id.sv_location) ;
        mapFragment =  (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if(location!=null||!location.equals("")){
                    Geocoder geocoder= new Geocoder(homeUser.this); /////////// jo bhi address enter kia usko longitude and latitude k cordinates me convert karta hai...
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

                    destination=new Location("LocationA");
                    destination.setLatitude(address.getLatitude());
                    destination.setLongitude(address.getLongitude());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(destination==null){
                    Toast.makeText(homeUser.this, "Please select the delivery address", Toast.LENGTH_SHORT).show();
                    return;
                }
                Location source=new Location("locationA");
                source.setLatitude(17.4399);
                source.setLongitude(78.4983);
                double distance=destination.distanceTo(source);
                distance=distance/1000;
                final String dist = Double.toString(distance);
                Toast.makeText(homeUser.this,dist , Toast.LENGTH_SHORT).show();
                Intent o = new Intent(homeUser.this, orderFoodU.class);
                o.putExtra("key", dist);
                o.putExtra("dest",destination);
                startActivity(o);
            }
        });
    }

    private void checkGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(homeUser.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    setupGPS();
                } else {
                    ActivityCompat.requestPermissions(homeUser.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            608);
                }
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(homeUser.this,
                                607);
                    } catch (IntentSender.SendIntentException sendEx) {
                        Log.d("Loc", sendEx.toString());
                    }
                }
            }
        });
    }

    private void setupGPS() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d("Loc", "loc callback");
                if (locationResult == null)
                    return;
                if (locationResult.getLastLocation() != null) {
                    currentLocation = locationResult.getLastLocation();
                    setCurrentLocMarker();
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                }
            }
        };
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setCurrentLocMarker();
    }

    private void setCurrentLocMarker(){
        if(currentLocation!=null && map!=null){
            Log.d("loc", "inside location marker");
            LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker()).title("you are here!!");
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
            map.addMarker(markerOptions);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 607) {
            if (resultCode == RESULT_OK)
                checkGPS();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    fetchLastLocation();
                    checkGPS();
                }
                break;
        }
    }
}
