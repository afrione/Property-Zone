package com.jay.propertyzone.ui;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jay.propertyzone.R;
import com.jay.propertyzone.models.Property;
import com.jay.propertyzone.models.model;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPropertyActivity extends AppCompatActivity {
    private Button mSubmit;
    private Button mUploadImage;

    private EditText mPropertyTitle;
    private EditText mPropertyDescription;
    private EditText mPropertyLocation;
    private EditText mPropertyPrice;
    private EditText mPropertyPhone;

    private FirebaseFirestore db;
    private CollectionReference ref;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;


    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 11;;
    private String currentPhotoPath;

    @BindView(R.id.propertyImageView) ImageView mImageLabel;
    private ProgressDialog progressDialog;

    private static final String TAG = "image creation value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Please wait...");
        mSubmit = findViewById(R.id.submit_button);
        mUploadImage = findViewById(R.id.upload_button);

        mPropertyPrice = findViewById(R.id.propertyPrice);
        mPropertyTitle = findViewById(R.id.propertyTitle);
        mPropertyLocation = findViewById(R.id.propertyLocation);
        mPropertyDescription = findViewById(R.id.propertyDescription);
        mPropertyPhone = findViewById(R.id.propertyPhone);

        mUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,2);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri != null ){
                    uploadToFirebase(imageUri);
                }else{
                    Toast.makeText(AddPropertyActivity.this,"Please select image",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            mImageLabel.setImageURI(imageUri);
        }
    }

    private void uploadToFirebase(Uri uri){
        progressDialog.show();
        StorageReference fileRef = reference.child((System.currentTimeMillis()+"."+getFileExtension(uri)));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.hide();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String imagePath = uri.toString();
                        model model = new model(uri.toString());
                        String modelId = root.push().getKey();
                        root.child(modelId).setValue(model);

                        db = FirebaseFirestore.getInstance();
                        ref = FirebaseFirestore.getInstance().collection("properties");
                        String newDocumentId = ref.document().getId();

                        Property newProperty = new Property(
                                mPropertyPrice.getText().toString(),
                                mPropertyTitle.getText().toString(),
                                mPropertyLocation.getText().toString(),
                                mPropertyDescription.getText().toString(),
                                mPropertyPhone.getText().toString(),
                                imagePath);

                        newProperty.setId(newDocumentId);
                        ref.document(newDocumentId).set(newProperty).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddPropertyActivity.this, "Property Saved", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(AddPropertyActivity.this, MainActivity.class));
                                finish();

                            }
                        });

                        Toast.makeText(AddPropertyActivity.this,"Uploaded successfully",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPropertyActivity.this,"Uploading Failed !!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


}
