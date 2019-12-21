package com.example.coding_hackaton_guwahati;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Project_Details extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project__details);

        MapsFragment fragInfo = new MapsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.maps_fragment, fragInfo).commit();
    }
}
