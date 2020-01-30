package com.example.chirpattendance.activities;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.chirpattendance.R;
import com.example.chirpattendance.fragments.FragmentAttendeesList;
import com.example.chirpattendance.fragments.FragmentDefaultersList;
import com.example.chirpattendance.fragments.FragmentRequestsList;
import com.example.chirpattendance.fragments.FragmentRooms;
import com.example.chirpattendance.viewPager.TabSwitchAdapter;

public class TabSwitch extends AppCompatActivity {

    private  com.google.android.material.tabs.TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_switch);

        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);

        TabSwitchAdapter adapter=new TabSwitchAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentAttendeesList(),"ATTENDEES");
        adapter.addFragment(new FragmentDefaultersList(),"DEFAULTERS");
        adapter.addFragment(new FragmentRequestsList(),"REQUESTS");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
