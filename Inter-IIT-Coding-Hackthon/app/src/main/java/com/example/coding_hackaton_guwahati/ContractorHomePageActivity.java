package com.example.coding_hackaton_guwahati;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContractorHomePageActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    List<Projects> project_list = new ArrayList<>();
    Toolbar mTopToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_home_page);

        mTopToolbar = findViewById(R.id.my_toolbar);
        mTopToolbar.setTitle("Name of the app");
        setSupportActionBar(mTopToolbar);

        recyclerView = findViewById(R.id.contractor_project_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart()
    {
        super.onStart();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("contractors");

        ref.whereEqualTo("email", Prevalent.contractor_email_id)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
        {
            @Override
            public void onSuccess(QuerySnapshot snapshots)
            {
                String contractor_id = "";
                if(snapshots.getDocuments().isEmpty() == false)
                {
                    contractor_id = snapshots.getDocuments().get(0).getId();
                    Prevalent.contractor_id = contractor_id;
                }

                FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                CollectionReference ref2 = db2.collection("projects");
                //ref2 is for the projects and have to get only those matching with the contractor id

                ref2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots)
                    {
                        for(DocumentSnapshot sp2: snapshots.getDocuments())
                        {
                            String temp = sp2.getDocumentReference("contractor_ref").getPath();
                            String check = "contractors/" + Prevalent.contractor_id;

                            if(temp.equals(check))
                            {
                                Projects temp2 = new Projects(sp2.getId(), sp2.get("name"), sp2.get("report"),sp2.get("contractor_remarks"),sp2.get("description"),
                                                                    sp2.get("latitude"), sp2.get("longitude"),sp2.get("num_users"),sp2.get("user_status"),sp2.get("contractor_status"),
                                                                    sp2.get("time"), sp2.get("contract_ref"));
                                project_list.add(temp2);
                            }
                        }

                        RecyclerView.Adapter adapter = new Contractor_Project_Adapter(getApplicationContext(),project_list);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void logout(){
        Prevalent.contractor_email_id = "";
        FirebaseAuth.getInstance().signOut();
        setResult(RESULT_OK);
        finish();
    }

}
