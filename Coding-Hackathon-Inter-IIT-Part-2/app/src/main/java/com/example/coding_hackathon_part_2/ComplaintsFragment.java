package com.example.coding_hackathon_part_2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


//public class ComplaintsFragment extends Fragment {
//     @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_complaints, container, false);
//    }
//}

public class ComplaintsFragment extends Fragment
{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    List<Region_Groups> region_list = new ArrayList<>();
    FirebaseStorage storage;
    StorageReference storageReference;
    String filetype,filename;

    private RecyclerView.Adapter<ViewHolder> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_complaints, container, false);
        recyclerView = view.findViewById(R.id.region_groups_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Log.d("Lavish", getActivity().toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("groups_complaint");

        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document: task.getResult())
                    {
                        Map<String,Object> data = document.getData();
                        Region_Groups temp = new Region_Groups(document.getId(),
                                data.get("centre_latitude"), data.get("centre_longitude"),data.get("num_complaints"));
                        region_list.add(temp);
                        //mAdapter.notifyDataSetChanged();
                    }

                    adapter = new RecyclerView.Adapter<ViewHolder>()
                    {
                        @NonNull
                        @Override
                        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                        {
                            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.regions_layout, viewGroup, false);
                            ViewHolder holder = new ViewHolder(view);
                            return holder;
                        }

                        @Override
                        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
                        {
                            final Region_Groups region_details = region_list.get(position);

                            holder.txtProjectName.setText("Latitude: " + region_details.getLatitude());
                            holder.txtProjectDesciption.setText("Longitude: " + region_details.getLongitude());
                            holder.txtProjectSurveyCount.setText("Number of Complaints: " + region_details.getNum_complaints());


                            String location = getCompleteAddress(region_details.getLatitude(), region_details.getLongitude());
                            holder.locationText.setText(location);

                            holder.btn_down.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    Log.d("vakul", "billi");
                                    downloadhelper("complaints/YVAFXPBFVTAUNymPxa7Q");
//                                    downloadhelper("complaints/YVAFXPBFVTAUNymPxa7Q");
                                }
                            });
                            holder.parentlayout.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Intent i = new Intent(getActivity().getApplicationContext(), Complaint_Details.class);
                                    i.putExtra("groupid", region_details.getId());
                                    startActivity(i);
                                }
                            });
                        }

                        @Override
                        public int getItemCount()
                        {
                            return region_list.size();
                        }
                    };

                    recyclerView.setAdapter(adapter);
                }
            }
        });

        return view;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtProjectName;
        public TextView txtProjectDesciption;
        public TextView txtProjectSurveyCount;
        public TextView locationText;
        public CardView parentlayout;
        public Button btn_down;


        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtProjectName = itemView.findViewById(R.id.region_latitude);
            txtProjectDesciption = itemView.findViewById(R.id.region_longitude);
            txtProjectSurveyCount = itemView.findViewById(R.id.region_count);
            btn_down = itemView.findViewById(R.id.btn_download);
            parentlayout = itemView.findViewById(R.id.cardview_region);
            locationText = itemView.findViewById(R.id.display_location);
        }
    }

    // download function
    void downloadhelper(String path)
    {
        Log.d("vakul123", "kutta");
        StorageReference listRef = storageReference.child(path);

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        Log.d("vakul123", "pokemon");
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }

                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.


                            item.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                @Override
                                public void onSuccess(StorageMetadata storageMetadata) {
                                    // Metadata now contains the metadata for 'images/forest.jpg'
                                    filetype=storageMetadata.getContentType();
                                    filename=storageMetadata.getName();
                                    filetype=filetype.substring(filetype.lastIndexOf("/")+1);
                                    Log.d("vakul123", filename + "  " +  filetype);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Uh-oh, an error occurred!
                                }
                            });
                            File localfile =new File("doadjo");

                            File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                            try {
                                localfile = File.createTempFile(
                                        filename,  /* prefix */
                                        "." + filetype,         /* suffix */
                                        storageDir      /* directory */
                                );
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("chirag", localfile.toString());
                            item.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.d("vakul123", filename + "  " +  filetype);
                                    // Local temp file has been created
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("vakul123", "doraemon");
                        // Uh-oh, an error occurred!
                    }
                });
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
