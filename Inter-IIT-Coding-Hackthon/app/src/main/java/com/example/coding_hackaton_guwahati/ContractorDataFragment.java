package com.example.coding_hackaton_guwahati;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;



public class ContractorDataFragment extends Fragment
{

    FirebaseStorage storage;
    StorageReference storageReference;
    String filetype,filename;

    public ContractorDataFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_contractor_data, container, false);

        if(v != null)
        {

            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();

            final TextView project_name = v.findViewById(R.id.fragmentData_name);
            final TextView project_description = v.findViewById(R.id.fragmentData_description);
            final TextView project_status = v.findViewById(R.id.fragmentData_user_status);
            final TextView project_date = v.findViewById(R.id.fragmentData_contr_time);

            final EditText editText_status = v.findViewById(R.id.txt_status);
            final EditText editText_reviews = v.findViewById(R.id.txt_review);

            final Button submit = v.findViewById(R.id.submit);
            final Button download_report = v.findViewById(R.id.download_report);


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

                                Integer status = snapshot.getDouble("user_status").intValue();
                                project_status.setText("Project Status submiited by User: " + status.toString() + "%");

                                Timestamp time = snapshot.getTimestamp("time");
                                Long seconds = time.getSeconds();

                                Date date = new Date(seconds*1000);
                                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                                String formattedDate = sdf.format(date);

                                project_date.setText("Project Started On: " + formattedDate.toString() );


                                Integer status2 = snapshot.getDouble("contractor_status").intValue();
                                editText_status.setText(status2.toString());

                                editText_reviews.setText(snapshot.getString("contractor_remarks"));

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


            submit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Integer status = Integer.valueOf(editText_status.getText().toString());
                    String reviews = editText_reviews.getText().toString();

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference ref = db.collection("projects").document(Prevalent.project_id);

                    ref.update("contractor_status", status);
                    ref.update("contractor_remarks", reviews)
                            .addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void aVoid)
                                {
                                    Toast.makeText(getActivity(), "Updated Succesfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });

            download_report.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference ref = db.collection("projects").document(Prevalent.project_id);

                    ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                    {
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot)
                        {
                            String path = snapshot.getString("report");

                            String filename=path.substring(path.lastIndexOf("/")+1);
                            filename = "projects/" + filename;

                            Log.d("Dishu", filename);

                            storageReference.child(filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {
                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    startDownload(uri);
                                }
                            });
                        }
                    });
                }
            });
        }
        return v;
    }

    public long startDownload(Uri uri)
    {
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        DownloadManager mManager =  (DownloadManager) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().DOWNLOAD_SERVICE);
        DownloadManager.Request mRqRequest = new DownloadManager.Request(uri);
        mRqRequest.setDescription("This is Test File");

        String name = Timestamp.now().toString();
        name = name + ".jpg";
        mRqRequest.setDestinationInExternalFilesDir(getActivity().getApplicationContext(), storageDir.getPath(), name);

        return mManager.enqueue(mRqRequest);
    }


}
