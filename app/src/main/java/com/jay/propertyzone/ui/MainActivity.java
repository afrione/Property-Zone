package com.jay.propertyzone.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jay.propertyzone.Constants;
import com.jay.propertyzone.R;
import com.jay.propertyzone.adapters.PropertyListAdapter;
import com.jay.propertyzone.models.Property;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    final String TAG = "tag";

    private CollectionReference properties; // FirestoreDatabase properties collection;
    private List<Property> mPropertyList = new ArrayList<>();
    private ItemTouchHelper mSwipeDeleteSensor;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private Button addNewProperty;
    PropertyListAdapter mAdapter;
    private ProgressDialog progressDialog;


    @BindView(R.id.imageView) ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Please wait...");
        progressDialog.show();
        fetchPropertyList();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddPropertyActivity.class));
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DocumentReference df = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String PropertyLevel = documentSnapshot.getString("PropertyLevel");
                if ( PropertyLevel.equals("ActiveUser")) {
                    fab.setVisibility(View.VISIBLE);

                } else {
                   //
                }
            }
        });


    }

    private void fetchPropertyList() {
        progressDialog.show();
        properties = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_PROPERTIES);
        properties.addSnapshotListener((value, error) -> {
            progressDialog.hide();
            if (error != null) {
                Toast.makeText(getApplicationContext(), "Cannot Sync Data now.\nTry later", Toast.LENGTH_LONG).show();
                return;
            }
            if (value != null) {
                mPropertyList.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    mPropertyList.add(doc.toObject(Property.class));
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        mAdapter = new PropertyListAdapter(MainActivity.this, mPropertyList);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setVisibility(View.VISIBLE);
    }



    @Override
    public void onBackPressed() {
        finish();
    }
}
