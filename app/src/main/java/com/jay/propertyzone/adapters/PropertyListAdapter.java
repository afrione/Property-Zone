package com.jay.propertyzone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jay.propertyzone.R;
import com.jay.propertyzone.models.Property;
import com.jay.propertyzone.ui.PropertyDetailActivity;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PropertyListAdapter extends RecyclerView.Adapter<PropertyListAdapter.PropertyViewHolder>{

    private List<Property> mProperties;
    private Context mContext;


    public PropertyListAdapter(Context Context , List<Property> mProperties){
        this.mProperties = mProperties;
        this.mContext = Context;
    }



    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_list_item,parent,false);
        PropertyViewHolder viewHolder = new PropertyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        holder.bindProperty(mProperties.get(position));
    }

    @Override
    public int getItemCount() {
        return mProperties.size();
    }

    public  class PropertyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.propertyimage) ImageView mPropertyView;
        @BindView(R.id.propertyPrice) TextView mPropertyPrice;
        @BindView(R.id.propertyTitle) TextView mPropertyTitle;
        @BindView(R.id.propertyLocation) TextView mPropertyLocationView;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        public void bindProperty(Property property){

            mPropertyPrice.setText(property.getPrice());
            mPropertyTitle.setText(property.getTitle());
            mPropertyLocationView.setText(property.getLocation());
            // Add image
            Picasso.get().load(property.getImageUri()).into(mPropertyView);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, PropertyDetailActivity.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("properties", Parcels.wrap(mProperties));
            mContext.startActivity(intent);
        }

    }
}
