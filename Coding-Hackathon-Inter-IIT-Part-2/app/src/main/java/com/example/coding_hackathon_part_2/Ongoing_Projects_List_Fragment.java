package com.example.coding_hackathon_part_2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class Ongoing_Projects_List_Fragment extends Fragment
{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    List<com.example.coding_hackaton_guwahati.Projects> project_list = new ArrayList<>();

    private RecyclerView.Adapter<ViewHolder> adapter;

    //Boolean loaded_once = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_ongoing__projects__list, container, false);
        recyclerView = view.findViewById(R.id.projects_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        Log.d("Lavish", getActivity().toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("projects");

        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful() )
                {
                    for(QueryDocumentSnapshot document: task.getResult())
                    {
                        Map<String,Object> data = document.getData();

                        com.example.coding_hackaton_guwahati.Projects temp = new com.example.coding_hackaton_guwahati.Projects(document.getId(), data.get("name"), data.get("report"),data.get("contractor_remarks"),data.get("description"),
                                data.get("latitude"), data.get("longitude"),data.get("num_users"),data.get("user_status"),data.get("contractor_status"),
                                data.get("time"), data.get("contract_ref"));

                        project_list.add(temp);
                        //mAdapter.notifyDataSetChanged();
                    }

                    adapter = new RecyclerView.Adapter<ViewHolder>()
                    {
                        @NonNull
                        @Override
                        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                        {
                            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.projects_layout, viewGroup, false);
                            ViewHolder holder = new ViewHolder(view);
                            return holder;
                        }

                        @Override
                        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
                        {
                            final com.example.coding_hackaton_guwahati.Projects project_details = project_list.get(position);

                            holder.txtProjectName.setText(project_details.getName());
                            holder.txtProjectDesciption.setText(project_details.getDescription());
                            holder.txtProjectSurveyCount.setText("Number of surveys done: " + project_details.getNum_users());

                            holder.parentlayout.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Intent i = new Intent(getActivity().getApplicationContext(), ConstructionDetailActivity.class);
                                    i.putExtra("projectId", project_details.getId());
                                    startActivity(i);
                                }
                            });
                        }

                        @Override
                        public int getItemCount()
                        {
                            return project_list.size();
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
        public CardView parentlayout;


        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtProjectName = itemView.findViewById(R.id.project_name_card);
            txtProjectDesciption = itemView.findViewById(R.id.project_des_card);
            txtProjectSurveyCount = itemView.findViewById(R.id.project_survey_count_card);
            parentlayout = itemView.findViewById(R.id.cardview_projects_list);
        }
    }

}
