package com.example.coding_hackaton_guwahati;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DataFragment extends Fragment
{

    private String project_id = Prevalent.project_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_data, container, false);

        if(v != null)
        {
            final TextView project_name = v.findViewById(R.id.fragmentData_name);
            final TextView project_description = v.findViewById(R.id.fragmentData_description);
            final TextView contractor_name = v.findViewById(R.id.fragmentData_contr_name);
            final TextView project_status = v.findViewById(R.id.fragmentData_contr_status);
            final TextView project_date = v.findViewById(R.id.fragmentData_contr_time);
            final Button submit = v.findViewById(R.id.btn_submit_feedback);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity().getApplicationContext(), User_Construction_Survey.class);
                    startActivity(i);
                }
            });

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference doc_ref = db.collection("projects").document(Prevalent.project_id);

            doc_ref.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                    {
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot)
                        {
                            if(snapshot.exists())
                            {

                                project_name.setText("Project Name: " + snapshot.getString("name"));
                                project_description.setText("Project Description: " + snapshot.getString("description"));
                                DocumentReference ref2 = snapshot.getDocumentReference("contractor_ref");
                                ref2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                                {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot2)
                                    {
                                        if(snapshot2.exists()) {
                                            contractor_name.setText("Contractor Name: " + snapshot2.getString("company_name"));
                                        }

                                        else {
                                            contractor_name.setText("Contractor Name: Not Applicable");
                                        }
                                    }
                                });

                                Integer status = snapshot.getDouble("contractor_status").intValue();
                                project_status.setText("Project Status: " + status.toString() + "%");

                                Timestamp time = snapshot.getTimestamp("time");
                                Long seconds = time.getSeconds();

                                Date date = new Date(seconds*1000);
                                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                                String formattedDate = sdf.format(date);

                                project_date.setText("Project Started On: " + formattedDate.toString() );

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
        }
        return v;
    }

}
