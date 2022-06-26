package com.jay.propertyzone.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jay.propertyzone.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText mNewPassword;
    private EditText mConfirmPassword;
    private Button mUpdatePassword;
    private ProgressDialog mAuthProgressDialog;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mNewPassword = findViewById(R.id.newPassword);
        mConfirmPassword = findViewById(R.id.confirmPassword);

        user = FirebaseAuth.getInstance().getCurrentUser();

        mUpdatePassword = findViewById(R.id.updatePassword);
        mUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNewPassword.getText().toString().isEmpty()){
                    mNewPassword.setError("Required field");

                    return;
                }

                if (mConfirmPassword.getText().toString().isEmpty()){
                    mConfirmPassword.setError("Required field");

                    return;
                }

                if (!mNewPassword.getText().toString().equals(mConfirmPassword.getText().toString())){
                    mNewPassword.setError("Password Don't Match");

                    return;
                }

                mAuthProgressDialog.show();
                user.updatePassword(mNewPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        mAuthProgressDialog.hide();
                        Toast.makeText(ChangePasswordActivity.this, "Password Updated SuccessFully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mAuthProgressDialog.hide();
                        Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        PasswordProgressDialog();
    }

    public void PasswordProgressDialog(){
        mAuthProgressDialog= new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Updating Password....");
        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public void onBackPressed() {
        Intent premiumIntent = new Intent(ChangePasswordActivity.this, ProfileActivity.class);
        startActivity(premiumIntent);

    }
}