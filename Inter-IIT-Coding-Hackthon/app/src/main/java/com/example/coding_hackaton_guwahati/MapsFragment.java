package com.example.coding_hackaton_guwahati;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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


public class MapsFragment extends Fragment implements OnMapReadyCallback
{
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private String project_id = "";
    public MapsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_maps, container, false);


        if(v != null)
        {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_maps);
            if(mapFragment == null)
            {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                mapFragment = SupportMapFragment.newInstance();
                ft.replace(R.id.fragment_maps, mapFragment).commit();
            }

            mapFragment.getMapAsync(this);

        }
        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc_ref = db.collection("projects").document(Prevalent.project_id);


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
                        Toast.makeText(getActivity(),"Internet Not Working Properly", Toast.LENGTH_SHORT).show();
                    }
                });

//        Log.d("Lavish", Prevalent.project_id);
//        double latitude = 22.5;
//        double longitude = 88.7;
//        LatLng latLng = new LatLng(latitude, longitude);
//        mMap.addMarker(new MarkerOptions().position(latLng).title(getCompleteAddress(latitude,longitude)));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14F));
    }

    private String getCompleteAddress(double Latitude, double Longitutde)
    {
        String address = "";

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

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
                Toast.makeText(getActivity(),"Address Not Found",Toast.LENGTH_SHORT).show();
            }

            return address;


        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }

        return address;
    }
}
