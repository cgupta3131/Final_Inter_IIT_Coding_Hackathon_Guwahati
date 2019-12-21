package com.example.coding_hackathon_part_2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class ProjectsFragment extends Fragment {

    private EditText titleText, descriptionText, latitudeText, longitudeText;
    private Button continueBtn,uploadBtn;
    private TextView tv;

    private final int ACTIVITY_CHOOSE_FILE = 10;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Uri filepath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_projects, container, false);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        titleText = view.findViewById(R.id.input_name);
        descriptionText = view.findViewById(R.id.input_description);
        latitudeText = view.findViewById(R.id.input_latitude);
        longitudeText = view.findViewById(R.id.input_longitude);
        continueBtn = view.findViewById(R.id.btn_continue);
        uploadBtn=view.findViewById(R.id.btn_upload);
        tv=view.findViewById(R.id.textView3);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueToAssignContractor();
            }
        });

        return view;

    }
    private void chooseFile(){
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK) return;
        if(requestCode == ACTIVITY_CHOOSE_FILE)
        {
            filepath= data.getData();
            String help=filepath.getPath();
            tv.setText(help);
        }
    }


    private void continueToAssignContractor(){
        if(!validate()){
            return;
        }

        Intent intent = new Intent(getActivity(), AssignContractorActivity.class);
        String title = titleText.getText().toString();
        String description = descriptionText.getText().toString();
        String latitude = latitudeText.getText().toString();
        String longitude = longitudeText.getText().toString();
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        if(filepath!=null)
            intent.putExtra("filePath",filepath.toString());
        startActivity(intent);
    }

    private boolean validate() {
        boolean valid = true;

        String title = titleText.getText().toString();
        String latitude = latitudeText.getText().toString();
        String longitude = longitudeText.getText().toString();

        if (title.isEmpty() || title.length() < 3) {
            titleText.setError("should contain at least 3 alphanumeric characters");
            valid = false;
        } else {
            titleText.setError(null);
        }

        try {
            Double.parseDouble(latitude);
            latitudeText.setError(null);
        } catch (NumberFormatException e) {
            valid = false;
            latitudeText.setError("should be numeric");
        }

        try {
            Double.parseDouble(longitude);
            longitudeText.setError(null);
        } catch (NumberFormatException e) {
            valid = false;
            longitudeText.setError("should be numeric");
        }

        if(!valid){
            Toast.makeText(getActivity(), "Try again", Toast.LENGTH_LONG).show();
        }

        return valid;
    }

}
