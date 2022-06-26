package com.jay.propertyzone.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jay.propertyzone.models.Property;
import com.jay.propertyzone.ui.PropertyDetailFragment;

import java.util.List;

public class ProperyPagerAdapter extends FragmentPagerAdapter {
    private List<Property> mProperties;

    public ProperyPagerAdapter(@NonNull FragmentManager fm, int behavior,List<Property> mProperties) {
        super(fm, behavior);
        this.mProperties = mProperties;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return PropertyDetailFragment.newInstance(mProperties.get(position));
    }

    @Override
    public int getCount() {
        return mProperties.size();
    }

    public CharSequence getPageTitle(int position) {
        return mProperties.get(position).getTitle();
    }
}
