package com.example.coding_hackaton_guwahati;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    private String project_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        project_id = getIntent().getStringExtra("project_id");
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc_ref = db.collection("projects").document(project_id);


        doc_ref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot)
                    {
                        if(documentSnapshot.exists())
                        {
                            Double latitude,longitude;

                            latitude = documentSnapshot.getDouble("latitude");
                            longitude = documentSnapshot.getDouble("longitude");

                            Log.d("Chirag", latitude.toString());
                            LatLng location = new LatLng(latitude,longitude);

                            mMap.addMarker(new MarkerOptions().position(location).title(getCompleteAddress(latitude,longitude)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14F));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(getApplicationContext(),"Internet Not Working Properly", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getCompleteAddress(double Latitude, double Longitutde)
    {
        String address = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try{

            List<Address> addresses = geocoder.getFromLocation(Latitude,Longitutde,1);

            if(addresses != null){

                Address returnAddress = addresses.get(0);
                StringBuilder stringBuilderreturnAddress = new StringBuilder("");

                for(int i=0;i<=returnAddress.getMaxAddressLineIndex();i++) {
                    stringBuilderreturnAddress.append(returnAddress.getAddressLine(i)).append("\n");
                }

                address = stringBuilderreturnAddress.toString();
            }

            else {
                Toast.makeText(this,"Address Not Found",Toast.LENGTH_SHORT).show();
            }

            return address;


        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }

        return address;
    }
}
