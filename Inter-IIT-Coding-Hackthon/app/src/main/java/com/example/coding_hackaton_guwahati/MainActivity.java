package com.example.coding_hackaton_guwahati;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    private static final int REQUEST_USER_SIGNUP = 0;
    private static final int REQUEST_USER_HOME = 1;
    private static final int REQUEST_CONTRACTOR_SIGNUP = 3;
    private static final int REQUEST_CONTRACTOR_HOME = 4;

    TextView signupUserLink, signupContractorLink;
    EditText emailText, passwdText;
    Button loginBtn;
    SignInButton googleBtn;

    int minPassLength = 6;
    int maxPassLength = 16;
    private final static int RC_SIGN_IN = 2;

    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signupUserLink = findViewById(R.id.signup_user_link);
        signupContractorLink = findViewById(R.id.signup_contractor_link);
        emailText = findViewById(R.id.input_email);
        passwdText = findViewById(R.id.input_password);
        loginBtn = findViewById(R.id.btn_login);
        googleBtn = findViewById(R.id.google_btn);

        mAuth = FirebaseAuth.getInstance();

        googleBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signIn();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null) {
                    CollectionReference users = db.collection("users");
                    Query query = users.whereEqualTo("email", String.valueOf(firebaseAuth.getCurrentUser().getEmail()));
                    query.get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if(task.getResult().isEmpty()){
                                            Intent intent = new Intent(getApplicationContext(), ContractorHomePageActivity.class);
                                            Prevalent.contractor_email_id = String.valueOf(firebaseAuth.getCurrentUser().getEmail());

                                            startActivityForResult(intent, REQUEST_CONTRACTOR_HOME);
                                        }
                                        else{
                                            Intent intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                                            startActivityForResult(intent, REQUEST_USER_HOME);
                                        }
                                    } else {
                                        Toast.makeText(getBaseContext(), "Try again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else {
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signupUserLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserSignupActivity.class);
                startActivityForResult(intent, REQUEST_USER_SIGNUP);
            }
        });

        signupContractorLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContractorSignupActivity.class);
                startActivityForResult(intent, REQUEST_CONTRACTOR_SIGNUP);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    private void updateUI(final FirebaseUser user) {
        if(user != null){
            CollectionReference users = db.collection("users");
            Query query = users.whereEqualTo("email", String.valueOf(user.getEmail()));
            query.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(task.getResult().isEmpty()){
                                    Intent intent = new Intent(getApplicationContext(), ContractorHomePageActivity.class);
                                    Prevalent.contractor_email_id = user.getEmail();
                                    startActivityForResult(intent, REQUEST_CONTRACTOR_HOME);
                                }
                                else{
                                    Intent intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                                    startActivityForResult(intent, REQUEST_USER_HOME);
                                }
                            } else {
                                Toast.makeText(getBaseContext(), "Try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

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
        if (requestCode == REQUEST_USER_SIGNUP) {
            if (resultCode == RESULT_OK) {

            }
        }
        else if(requestCode == REQUEST_USER_HOME){
            if(resultCode == RESULT_OK){
                
            }
        }
        else if(requestCode == REQUEST_CONTRACTOR_SIGNUP){
            if(resultCode == RESULT_OK){

            }
        }
        else if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }
            catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
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

    //Required for Google Sign In
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        }
                        else {
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}
