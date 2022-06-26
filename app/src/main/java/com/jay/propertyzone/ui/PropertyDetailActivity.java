package com.jay.propertyzone.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jay.propertyzone.R;
import com.jay.propertyzone.adapters.ProperyPagerAdapter;
import com.jay.propertyzone.models.Property;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PropertyDetailActivity extends AppCompatActivity {

    @BindView(R.id.viewPager) ViewPager mViewPager;
    private ProperyPagerAdapter adapterViewPager;
    List<Property> mProperty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);
        ButterKnife.bind(this);

        mProperty = Parcels.unwrap(getIntent().getParcelableExtra("properties"));
        int startingPosition = getIntent().getIntExtra("position", 0);

        adapterViewPager = new ProperyPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,mProperty);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);

    }
}
