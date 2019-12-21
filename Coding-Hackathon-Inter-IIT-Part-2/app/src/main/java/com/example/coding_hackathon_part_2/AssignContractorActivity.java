package com.example.coding_hackathon_part_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AssignContractorActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;

    Spinner contractorSpin;
    Button AssignContractorBtn;
    EditText titleText, descriptionText, latitudeText, longitudeText;
    TextView tv;

    ArrayList<String> contractors = new ArrayList<>();
    ArrayList<DocumentReference> contractorIds = new ArrayList<>();

    Toolbar mTopToolbar;
    Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_contractor);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mTopToolbar = findViewById(R.id.my_toolbar);
        mTopToolbar.setTitle("Name of the app");
        setSupportActionBar(mTopToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        contractorSpin = findViewById(R.id.choose_contractor);
        AssignContractorBtn = findViewById(R.id.btn_assign_contractor);
        titleText = findViewById(R.id.input_title);
        descriptionText = findViewById(R.id.input_description);
        latitudeText = findViewById(R.id.input_latitude);
        longitudeText = findViewById(R.id.input_longitude);
        tv=findViewById(R.id.textView2);

        Intent intent = getIntent();

        if(intent.getStringExtra("title") != null && !intent.getStringExtra("title").isEmpty()){
            titleText.setText(intent.getStringExtra("title"));
            titleText.setEnabled(false);
            titleText.setFocusable(false);
        }

        if(intent.getStringExtra("description") != null && !intent.getStringExtra("description").isEmpty()){
            descriptionText.setText(intent.getStringExtra("description"));
            descriptionText.setEnabled(false);
            descriptionText.setFocusable(false);
        }

        if(intent.getStringExtra("latitude") != null && !intent.getStringExtra("latitude").isEmpty()){
            latitudeText.setText(intent.getStringExtra("latitude"));
            latitudeText.setEnabled(false);
            latitudeText.setFocusable(false);
        }

        if(intent.getStringExtra("longitude") != null && !intent.getStringExtra("longitude").isEmpty()){
            longitudeText.setText(intent.getStringExtra("longitude"));
            longitudeText.setEnabled(false);
            longitudeText.setFocusable(false);
        }
        if(intent.getStringExtra("filePath") != null && !intent.getStringExtra("filePath").isEmpty()){
            filePath=Uri.parse(intent.getStringExtra("filePath"));
            tv.setText(filePath.getPath().toString());
        }



        db.collection("contractors").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                contractors.add(String.valueOf(data.get("company_name")));
                                contractorIds.add(document.getReference());
                            }
                            setContractorSpinAdapter();
                        } else {
                            Toast.makeText(getBaseContext(), "Try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        AssignContractorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_project();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_assign_contractor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK);
                this.finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        setResult(RESULT_OK);
        finish();
    }

    private void add_project(){
        if (!validate()) {
            return;
        }

        AssignContractorBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(AssignContractorActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding project...");
        progressDialog.show();


        String title = titleText.getText().toString();
        Double latitude = Double.valueOf(latitudeText.getText().toString());
        Double longitude = Double.valueOf(longitudeText.getText().toString());
        String description = descriptionText.getText().toString();

        String x="";
        if(filePath!=null) {
            StorageReference ref = storageReference.child("projects/" + UUID.randomUUID().toString());
            Log.d("madar", ref.toString());
            x=ref.toString();
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getApplicationContext(), "Uploaded Brochure", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        }


        DocumentReference contractorRef = contractorIds.get(contractorSpin.getSelectedItemPosition());

        Map<String, Object> user = new HashMap<>();
        user.put("name", title);
        user.put("description", description);
        user.put("latitude", latitude);
        user.put("longitude", longitude);
        user.put("num_users", 0);
        user.put("report", x);
        user.put("user_status", 0);
        user.put("contractor_status", 0);
        user.put("contractor_remarks", "");
        user.put("contractor_ref", contractorRef);
        user.put("time", Timestamp.now());

        db.collection("projects")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                onProjectAdded(progressDialog);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                onProjectFailed();
                                                progressDialog.dismiss();
                                            }
                                        });



    }

    public void onProjectAdded(ProgressDialog progressDialog) {
        Toast.makeText(getBaseContext(), "Project successfully added", Toast.LENGTH_LONG).show();
        AssignContractorBtn.setEnabled(true);
        setResult(RESULT_OK);
        finish();
    }

    public void onProjectFailed() {
        Toast.makeText(getBaseContext(), "Try again", Toast.LENGTH_LONG).show();
        AssignContractorBtn.setEnabled(true);
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
            Toast.makeText(getBaseContext(), "Try again", Toast.LENGTH_LONG).show();

        }

        return valid;
    }

    private void setContractorSpinAdapter(){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item_contractors, contractors){
            @Override
            public boolean isEnabled(int position){
                if(position == 0) {
                    return false;
                }
                else{
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_contractors);
        contractorSpin.setAdapter(spinnerArrayAdapter);
    }
}
