package com.example.coding_hackathon_part_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_HOME = 1;

    EditText emailText, passwdText;
    Button loginBtn;

    FirebaseAuth mAuth;

    int minPassLength = 6;
    int maxPassLength = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailText = findViewById(R.id.input_email);
        passwdText = findViewById(R.id.input_password);
        loginBtn = findViewById(R.id.btn_login);

        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivityForResult(intent, REQUEST_HOME);
        }
    }

    public void login() {

        if(!validate()){
            return;
        }

        loginBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = emailText.getText().toString();
        String password = passwdText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            onLoginSuccess();
                            progressDialog.dismiss();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            onLoginFailed();
                            progressDialog.dismiss();
                            updateUI(null);
                        }

                    }
                });
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwdText.getText().toString();


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

        if(!valid){
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        }

        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_HOME){
            if(resultCode == RESULT_OK){

            }
        }
    }

    public void onLoginSuccess() {
        loginBtn.setEnabled(true);
        Toast.makeText(getBaseContext(), "Login successful", Toast.LENGTH_LONG).show();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginBtn.setEnabled(true);

    }
}
