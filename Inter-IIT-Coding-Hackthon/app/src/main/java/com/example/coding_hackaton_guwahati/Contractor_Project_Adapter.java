package com.example.coding_hackaton_guwahati;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


public class Contractor_Project_Adapter extends RecyclerView.Adapter<Contractor_Project_Adapter.ViewHolder>
{
    Context context;
    List<Projects> mainUploadlist;

    public Contractor_Project_Adapter(Context context, List<Projects> Templist)
    {
        this.mainUploadlist = Templist;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contractor_project_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final Projects project_details = mainUploadlist.get(position);
        holder.txtProjectName.setText("Project Name: " + project_details.getName() );
        holder.UserCount.setText("Number of Surveys Done: " + project_details.getNum_users());

        holder.parentlayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(context, Contractor_Project_Details.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Prevalent.project_id = project_details.getId();
                context.getApplicationContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {

        return mainUploadlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtProjectName, UserCount;
        public CardView parentlayout;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtProjectName = itemView.findViewById(R.id.project_name);
            UserCount = itemView.findViewById(R.id.project_survey_count);
            parentlayout = itemView.findViewById(R.id.cardview_survey_contractor);

        }
    }
}
