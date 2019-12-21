package com.example.coding_hackaton_guwahati;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class UserSignupActivity extends AppCompatActivity {

    EditText firstNameText, lastNameText, emailText, passwdText, confirmPasswdText;
    Button signupButton;
    TextView loginLink;

    int minPassLength = 6;
    int maxPassLength = 16;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        firstNameText = findViewById(R.id.input_first_name);
        lastNameText = findViewById(R.id.input_last_name);
        emailText = findViewById(R.id.input_email);
        passwdText = findViewById(R.id.input_passwd);
        confirmPasswdText = findViewById(R.id.input_confirm_passwd);
        signupButton = findViewById(R.id.btn_signup);
        loginLink = findViewById(R.id.display_login);

        signupButton.setOnClickListener(new View.OnClickListener() {
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

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(UserSignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String email = emailText.getText().toString();

        CollectionReference users = db.collection("users");
        Query query = users.whereEqualTo("email", email);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty()){

                                String firstName = firstNameText.getText().toString();
                                String lastName = lastNameText.getText().toString();
                                String email = emailText.getText().toString();
                                String password = passwdText.getText().toString();

                                Map<String, Object> user = new HashMap<>();
                                user.put("first_name", firstName);
                                user.put("last_name", lastName);
                                user.put("email", email);
                                user.put("password", password);
                                user.put("credits", 0);


                                db.collection("users")
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                createUser(progressDialog);
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
                                signupButton.setEnabled(true);
                                progressDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Try again", Toast.LENGTH_LONG).show();
                            signupButton.setEnabled(true);
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    public void createUser(final ProgressDialog progressDialog){
        String email = emailText.getText().toString();
        String password = passwdText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            onSignupSuccess();
                            progressDialog.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }
                });
    }


    public void onSignupSuccess() {
        Toast.makeText(getBaseContext(), "Registration successful", Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
        setResult(RESULT_OK);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwdText.getText().toString();
        String confirmPasswd = confirmPasswdText.getText().toString();

        if (firstName.isEmpty() || firstName.length() < 3) {
            firstNameText.setError("at least 3 characters");
            valid = false;
        } else {
            firstNameText.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            lastNameText.setError("at least 3 characters");
            valid = false;
        } else {
            lastNameText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        }
        else{
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < minPassLength || password.length() > maxPassLength) {
            passwdText.setError("between " + minPassLength + " and " + maxPassLength + " alphanumeric characters");
            valid = false;
        } else {
            passwdText.setError(null);
        }

        if(!password.equals(confirmPasswd)){
            confirmPasswdText.setError("Passwords do not match");
            valid = false;
        }
        else{
            confirmPasswdText.setError(null);
        }

        if(!valid){
            Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        }

        return valid;
    }
}
