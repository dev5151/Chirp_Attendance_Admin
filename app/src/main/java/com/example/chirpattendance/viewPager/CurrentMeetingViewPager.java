package com.example.chirpattendance.viewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chirpattendance.fragments.FragmentAttendeesList;
import com.example.chirpattendance.fragments.FragmentDefaultersList;
import com.example.chirpattendance.fragments.FragmentPreviousMeetingAttendeesList;
import com.example.chirpattendance.fragments.FragmentPreviousMeetingDeafaultersList;
import com.example.chirpattendance.fragments.FragmentRequestsList;
import com.example.chirpattendance.fragments.FragmentSendKey;

public class CurrentMeetingViewPager extends FragmentPagerAdapter {

    public CurrentMeetingViewPager(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position)
        {
            case 0 : return new FragmentSendKey();

            case 1 : return new FragmentAttendeesList();

            case 2 : return new FragmentDefaultersList();

            case 3 : return new FragmentRequestsList();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
