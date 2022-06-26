package com.jay.propertyzone.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import com.jay.propertyzone.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = CreateAccountActivity.class.getSimpleName();

    @BindView(R.id.nameEditText) EditText mNameEditText;
    @BindView(R.id.editTextEmail) EditText mEmailEditText;

    @BindView(R.id.editTextPassword) EditText mPasswordEditText;
    @BindView(R.id.confirmPasswordEditText) EditText mConfirmPasswordEditText;
    @BindView(R.id.passwordSignUpButton) Button mCreateUserButton;
    @BindView(R.id.loginTextView) TextView mLoginTextView;

    @BindView(R.id.firebaseProgressBar) ProgressBar mSignInProgressBar;
    @BindView(R.id.loadingTextView) TextView mLoadingSignUp;

    private CountryCodePicker ccp;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mAuthProgressDialog;
    private String mName;
    CountryCodePicker phoneCcp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        createAuthProgressDialog();
        createAuthStateListener();

        mCreateUserButton.setOnClickListener(this);
        mLoginTextView.setOnClickListener(this);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);

    }

    public void createAuthProgressDialog(){
        mAuthProgressDialog= new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Registering your account....");
        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View view){
        if(view == mLoginTextView){
            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } if (view == mCreateUserButton){
            createNewUser();
        }
    }

    private void createNewUser() {
        final String name = mNameEditText.getText().toString().trim();
        final String email = mEmailEditText.getText().toString().trim();

        String country = ccp.getSelectedCountryName();

        String password = mPasswordEditText.getText().toString().trim();
        String confirmPassword = mConfirmPasswordEditText.getText().toString().trim();
        mName = mNameEditText.getText().toString().trim();

        boolean validEmail = isValidEmail(email);
        boolean validName = isValidName(name);
        boolean validPassword = isValidPassword(password, confirmPassword);

        if(password.length() < 6){
            mPasswordEditText.setError("Password Must be >= 6 Characters");
            return;
        }

        if (!validEmail || !validName || !validPassword) return;
        mAuthProgressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mAuthProgressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Authentication successful");

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            DocumentReference ref = FirebaseFirestore.getInstance().collection("users").document(user.getUid());
                            // Define new user
                            Map<String, Object> newUSer = new HashMap<>();
                            newUSer.put("name", mNameEditText.getText().toString());
                            newUSer.put("email", mEmailEditText.getText().toString());
                            newUSer.put("country", ccp.getSelectedCountryName());
                            newUSer.put("PropertyLevel", "inActiveUser");
                            newUSer.put("id",user.getUid());
                            // Save new User
                            ref.set(newUSer).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(CreateAccountActivity.this, "Successfully Registered",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountActivity.this);
                            builder.setIcon(getResources().getDrawable(R.drawable.ic_baseline_warning_amber_24));
                            builder.setTitle("Alert!!");
                            builder.setMessage(task.getException().getMessage());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            return;
                        }
                    }
                });

    }

    private void createAuthStateListener(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };

    }


    private boolean isValidEmail(String email) {
        boolean isGoodEmail = (email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if(!isGoodEmail){
            mEmailEditText.setError("Please enter a valid email address");
            return false;
        }
        return isGoodEmail;
    }

    private boolean isValidName(String name){
        if(name.equals("")){
            mNameEditText.setError("Please enter your name");
            return false;
        }
        return true;
    }



    private boolean isValidPassword(String password, String confirmPassword){
        if(password.length() < 6){
            mPasswordEditText.setError("Please create a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)){
            mPasswordEditText.setError("Passwords do not match");
            return false;
        }
        return true;
    }



}