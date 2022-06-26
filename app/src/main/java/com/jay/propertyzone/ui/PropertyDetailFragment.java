package com.jay.propertyzone.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jay.propertyzone.R;
import com.jay.propertyzone.models.Property;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PropertyDetailFragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.propertyimage) ImageView mPropertyView;
    @BindView(R.id.propertyPrice) TextView mPropertyPrice;
    @BindView(R.id.propertyTitle) TextView mPropertyTitleView;
    @BindView(R.id.propertyLocation) TextView mPropertyLocationView;
    @BindView(R.id.propertyDescription) TextView mPropertyDescription;
    @BindView(R.id.propertyPhone) TextView mPropertyPhone;


    private Property mProperty;

    public  PropertyDetailFragment() {

    }
    public  static  PropertyDetailFragment newInstance(Property property){
        PropertyDetailFragment propertyDetailFragment = new PropertyDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("property", Parcels.wrap(property));
        propertyDetailFragment.setArguments(args);
        return  propertyDetailFragment;
    }

    @Override
    public  void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        assert  getArguments() != null;
        mProperty = Parcels.unwrap(getArguments().getParcelable("property"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_property_detail, container,false);
        ButterKnife.bind(this, view);
        mPropertyPrice.setText(mProperty.getPrice());
        mPropertyTitleView.setText(mProperty.getTitle());
        mPropertyLocationView.setText(mProperty.getLocation());
        mPropertyDescription.setText(mProperty.getDescription());
        mPropertyPhone.setText(mProperty.getPhone());
        Picasso.get().load(mProperty.getImageUri()).into(mPropertyView);
        return  view;
    }

    @Override
    public void onClick(View view) {
        if (view == mPropertyPhone) {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + mProperty.getPhone()));
            startActivity(phoneIntent);
        }

    }
}
