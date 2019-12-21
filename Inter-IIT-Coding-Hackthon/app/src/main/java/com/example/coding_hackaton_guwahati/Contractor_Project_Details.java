package com.example.coding_hackaton_guwahati;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Contractor_Project_Details extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor__project__details);

        ContractorMapsFragment fragInfo = new ContractorMapsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.maps_fragment, fragInfo).commit();
    }
}
