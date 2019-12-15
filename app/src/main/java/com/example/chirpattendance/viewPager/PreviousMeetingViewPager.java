package com.example.chirpattendance.viewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chirpattendance.fragments.FragmentPreviousMeetingAttendeesList;
import com.example.chirpattendance.fragments.FragmentPreviousMeetingDeafaultersList;

public class PreviousMeetingViewPager extends FragmentPagerAdapter {

    public PreviousMeetingViewPager(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position)
        {
            case 0 : return new FragmentPreviousMeetingAttendeesList();

            case 1 : return new FragmentPreviousMeetingDeafaultersList();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
