package com.example.coding_hackathon_part_2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContractorConstructionFragment extends Fragment {

    TextView nameText, descriptionText, latitudeText, longitudeText, reportText, contractorText,
            contractorRemarksText, contractorStatusText, mediaRefText, videoRefText;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contractor_construction, container, false);

        nameText = view.findViewById(R.id.display_name);
        descriptionText = view.findViewById(R.id.display_description);
        latitudeText = view.findViewById(R.id.display_latitude);
        longitudeText = view.findViewById(R.id.display_longitude);
        reportText = view.findViewById(R.id.display_report);
        contractorText = view.findViewById(R.id.display_contractor);
        contractorRemarksText = view.findViewById(R.id.display_contractor_remarks);
        contractorStatusText = view.findViewById(R.id.display_contractor_status);
        mediaRefText = view.findViewById(R.id.display_media_ref);
        videoRefText = view.findViewById(R.id.display_video_ref);

        new FetchData().execute();

        return view;
    }

    public class FetchData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... values) {
            DocumentReference doc_ref = db.collection("projects").document(ProjectId.projectId);
            doc_ref.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot) {
                            if(snapshot.exists()) {

                                nameText.setText("Title: " + snapshot.getString("name"));
                                descriptionText.setText("Description: " + snapshot.getString("description"));
                                latitudeText.setText("Latitude: " + snapshot.getDouble("latitude").toString());
                                longitudeText.setText("Longitude: " + snapshot.getDouble("longitude").toString());
                                //reportText.setText(snapshot.getString("report"));


                                contractorRemarksText.setText("Remarks: " + snapshot.getString("contract_remarks"));
                                contractorStatusText.setText("Status: " + snapshot.getDouble("contractor_status").toString() + "%");
                                //mediaRefText.setText(snapshot.getString("description"));
                                //videoRefText.setText(snapshot.getString("description"));

                                final DocumentReference contractor = snapshot.getDocumentReference("contractor_ref");
                                contractor.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot2)
                                    {
                                        if(snapshot2.exists()) {
                                            contractorText.setText("Assigned contractor: " + snapshot2.getString("company_name"));
                                        }

                                        else {
                                            contractorText.setText("NaN");
                                        }
                                    }
                                });

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(getActivity(),"Try again", Toast.LENGTH_SHORT).show();
                        }
                    });

            return "Done";
        }
    }

}
