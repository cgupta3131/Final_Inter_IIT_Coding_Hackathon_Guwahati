package com.example.coding_hackaton_guwahati;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ContractorSignupActivity extends AppCompatActivity {

    EditText companyText, emailText;
    Spinner regionSpinner;
    Button signupBtn;
    TextView loginLink;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_signup);

        companyText = findViewById(R.id.input_company);
        emailText = findViewById(R.id.input_email);
        signupBtn = findViewById(R.id.btn_signup);
        loginLink = findViewById(R.id.display_login);

        regionSpinner = findViewById(R.id.choose_region);

        String[] regions = new String[]{
                "Region",
                "Andaman and Nicobar Islands",
                "Andra Pradesh",
                "Arunachal Pradesh",
                "Assam",
                "Bihar",
                "Chandigarh",
                "Chhattisgarh",
                "Dadar and Nagar Haveli",
                "Daman and Diu",
                "Delhi",
                "Goa",
                "Gujarat",
                "Haryana",
                "Himachal Pradesh",
                "Jammu and Kashmir",
                "Jharkhand",
                "Karnataka",
                "Kerala",
                "Lakshadeep",
                "Madya Pradesh",
                "Maharashtra",
                "Manipur",
                "Meghalaya",
                "Mizoram",
                "Nagaland",
                "Orissa",
                "Pondicherry",
                "Punjab",
                "Rajasthan",
                "Sikkim",
                "Tamil Nadu",
                "Telagana",
                "Tripura",
                "Uttaranchal",
                "Uttar Pradesh",
                "West Bengal",
        };
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item_region, regions){
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
                if(position == 0){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_region);
        regionSpinner.setAdapter(spinnerArrayAdapter);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {

        if (!validate()) {
            return;
        }

        signupBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ContractorSignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String email = emailText.getText().toString();

        CollectionReference users = db.collection("contractors");
        Query query = users.whereEqualTo("email", email);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty()){

                                String company = companyText.getText().toString();
                                String email = emailText.getText().toString();
                                final String password = generate_random_passwd();
                                String region = regionSpinner.getSelectedItem().toString();

                                Map<String, Object> user = new HashMap<>();
                                user.put("company_name", company);
                                user.put("email", email);
                                user.put("password", password);
                                user.put("region", region);

                                db.collection("contractors")
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                createUser(password, progressDialog);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                onSignupFailed();
                                                progressDialog.dismiss();
                                            }
                                        });
                            }
                            else{
                                Toast.makeText(getBaseContext(), "User with this email already exists", Toast.LENGTH_LONG).show();
                                signupBtn.setEnabled(true);
                                progressDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Try again", Toast.LENGTH_LONG).show();
                            signupBtn.setEnabled(true);
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    public String generate_random_passwd(){
        String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Small_chars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*_=+-/.?<>)";

        int len = 10;

        String values = Capital_chars + Small_chars + numbers + symbols;

        // Using random method
        Random rndm_method = new Random();

        String password = "";

        for (int i = 0; i < len; i++){
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            password += values.charAt(rndm_method.nextInt(values.length()));
        }
        return password;
    }

    public void createUser(final String password, final ProgressDialog progressDialog){
        String email = emailText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            new SendMailTask(password, progressDialog).execute();
                        } else {
                            // If sign in fails, display a message to the user.
                            onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private class SendMailTask extends AsyncTask<String, Void, String> {

        String password;
        ProgressDialog progressDialog;

        public SendMailTask(String password, ProgressDialog progressDialog) {
            this.password = password;
            this.progressDialog = progressDialog;
        }

        @Override
        protected String doInBackground(String... strings) {
            String email = emailText.getText().toString();
            Log.d("myEmail", email);

            GMailSender sender = new GMailSender("codingclubiitg@gmail.com","compiling");
            try {
                sender.sendMail("Login password", "Your login password is: " + this.password,
                        "codingclubiitg@gmail.com", email);
                progressDialog.dismiss();
                onSignupSuccess();
            } catch (Exception e) {
                Log.d("myEmail", "mail not sent");
                progressDialog.dismiss();
                onSignupFailed();
            }
            return null;
        }
    }

    public void onSignupSuccess() {
        Toast.makeText(getBaseContext(), "Registration successful", Toast.LENGTH_LONG).show();
        signupBtn.setEnabled(true);
        setResult(RESULT_OK);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
        signupBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String companyName = companyText.getText().toString();
        String email = emailText.getText().toString();
        String region = regionSpinner.getSelectedItem().toString();

        if (companyName.isEmpty() || companyName.length() < 3) {
            companyText.setError("at least 3 characters");
            valid = false;
        } else {
            companyText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        }
        else{
            emailText.setError(null);
        }

        if(region.isEmpty()){
            Toast.makeText(getBaseContext(), "Choose a region to continue", Toast.LENGTH_LONG).show();
            return false;
        }

        if(!valid){
            Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
        }

        return valid;
    }
}
