package com.example.chirpattendance.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.chirpattendance.fragments.FragmentPreviousMeetingAttendeesList;
import com.example.chirpattendance.fragments.FragmentPreviousMeetingDeafaultersList;

public class DefaultersAttendeesAdapter extends FragmentPagerAdapter {


    public DefaultersAttendeesAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0: return new FragmentPreviousMeetingAttendeesList();

            case 1: return new FragmentPreviousMeetingDeafaultersList();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
