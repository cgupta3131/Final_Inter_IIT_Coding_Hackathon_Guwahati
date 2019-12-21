package com.example.coding_hackathon_part_2;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class UserConstructionFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    List<ConstUser> project_list = new ArrayList<>();
    FirebaseStorage storage;
    StorageReference storageReference;
    String filetype,filename;
    private RecyclerView.Adapter<ViewHolder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_construction, container, false);
        recyclerView = view.findViewById(R.id.cardview_user_construction);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("user_survey");

        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();

                        ConstUser temp = new ConstUser(data.get("user_ref"),data.get("remarks"),data.get("time")
                        ,data.get("media_path"),data.get("user_status"));

                        project_list.add(temp);
                    }

                    adapter = new RecyclerView.Adapter<ViewHolder>() {
                        @NonNull
                        @Override
                        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.construction_layout, viewGroup, false);
                            ViewHolder holder = new ViewHolder(view);
                            return holder;
                        }

                        @Override
                        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                            final ConstUser project_details = project_list.get(position);

                            DocumentReference ref2 = project_details.getUserid();
                            String path = ref2.getPath();

                            String user_id_temp=path.substring(path.lastIndexOf("/")+1);

                            holder.txtProjectName.setText(user_id_temp);
                            holder.txtProjectDesciption.setText("REMARKS: " + project_details.getRemarks());

                            Timestamp time = project_details.getTime();
                            Long seconds = time.getSeconds();

                            Date date = new Date(seconds*1000);
                            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                            String formattedDate = sdf.format(date);

                            holder.txtProjectSurveyCount.setText("START DATE: " + formattedDate.toString());
                            holder.txtProjectSurveyCount2.setText("USER STATUS: " + project_details.getUser_status());
                            holder.btn_download.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    downloadhelper3();
                                }
                            });
                        }

                        @Override
                        public int getItemCount() {
                            return project_list.size();
                        }
                    };

                    recyclerView.setAdapter(adapter);
                }
            }
        });

        return view;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtProjectName;
        public TextView txtProjectDesciption;
        public TextView txtProjectSurveyCount;
        public TextView txtProjectSurveyCount2;
        public Button btn_download;
        public CardView parentlayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProjectName = itemView.findViewById(R.id.const_userid);
            txtProjectDesciption = itemView.findViewById(R.id.const_remarks);
            txtProjectSurveyCount = itemView.findViewById(R.id.const_start_date);
            txtProjectSurveyCount2 = itemView.findViewById(R.id.const_user_status);
            btn_download = itemView.findViewById(R.id.btn_download);
            parentlayout = itemView.findViewById(R.id.construction_layout);
        }
    }

    // download function
    void downloadhelper3()
    {
//        Log.d("vakul123", "kutta");
//        StorageReference listRef = storageReference.child(path);
//
//        listRef.listAll()
//                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
//                    @Override
//                    public void onSuccess(ListResult listResult) {
//                        Log.d("vakul123", "pokemon");
//                        for (StorageReference prefix : listResult.getPrefixes()) {
//                            // All the prefixes under listRef.
//                            // You may call listAll() recursively on them.
//                        }
//
//                        for (StorageReference item : listResult.getItems()) {
//                            // All the items under listRef.
//
//
//                            item.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
//                                @Override
//                                public void onSuccess(StorageMetadata storageMetadata) {
//                                    // Metadata now contains the metadata for 'images/forest.jpg'
//                                    filetype=storageMetadata.getContentType();
//                                    filename=storageMetadata.getName();
//                                    filetype=filetype.substring(filetype.lastIndexOf("/")+1);
//                                    Log.d("vakul123", filename + "  " +  filetype);
//
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception exception) {
//                                    // Uh-oh, an error occurred!
//                                }
//                            });
//                            File localfile =new File("doadjo");
//
//                            File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
//                            try {
//                                localfile = File.createTempFile(
//                                        filename,  /* prefix */
//                                        "." + filetype,         /* suffix */
//                                        storageDir      /* directory */
//                                );
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            Log.d("chirag", localfile.toString());
//                            item.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                                    Log.d("vakul123", filename + "  " +  filetype);
//                                    // Local temp file has been created
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception exception) {
//                                    // Handle any errors
//                                }
//                            });
//
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("vakul123", "doraemon");
//                        // Uh-oh, an error occurred!
//                    }
//                });
    }

}

