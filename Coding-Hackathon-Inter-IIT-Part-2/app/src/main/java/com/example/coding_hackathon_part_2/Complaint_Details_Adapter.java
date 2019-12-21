package com.example.coding_hackathon_part_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Complaint_Details_Adapter extends RecyclerView.Adapter<Complaint_Details_Adapter.ViewHolder>
{
    Context context;
    List<complaintDetails> mainUploadlist;

    public Complaint_Details_Adapter(Context context, List<complaintDetails> Templist)
    {
        this.mainUploadlist = Templist;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.complaint_details_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        complaintDetails itemdetails = mainUploadlist.get(position);
        holder.txtProductName.setText(itemdetails.getRemarks());

        DocumentReference ref2 = itemdetails.getUserid();
        String path = ref2.getPath();

        String user_id_temp=path.substring(path.lastIndexOf("/")+1);

        holder.txtProductCategory.setText("User ID: " + user_id_temp);

        //holder.locationText.setText();
    }

    @Override
    public int getItemCount() {

        return mainUploadlist.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtProductName, txtProductCategory, locationText, latitudeText, longitudeText;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.complaint_remarks);
            txtProductCategory = itemView.findViewById(R.id.complaint_user_id);

            locationText = itemView.findViewById(R.id.display_location);
            latitudeText = itemView.findViewById(R.id.display_latitude);
            longitudeText = itemView.findViewById(R.id.display_longitude);
        }
    }
}