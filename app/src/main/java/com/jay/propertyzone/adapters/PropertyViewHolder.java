package com.jay.propertyzone.adapters;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class PropertyViewHolder extends RecyclerView.ViewHolder{

    View mView;
    Context mContext;

    public PropertyViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();

    }
}
