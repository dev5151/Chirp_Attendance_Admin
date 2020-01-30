package com.example.chirpattendance.viewPager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabSwitchAdapter extends FragmentPagerAdapter {

    private final List<Fragment>fragmentList=new ArrayList<>();
    private final List<String>fragmentListTitles=new ArrayList<>();

    public TabSwitchAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public TabSwitchAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentListTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentListTitles.get(position);
    }

    public void addFragment(Fragment fragment , String Title ){
        fragmentList.add(fragment);
        fragmentListTitles.add(Title);
    }
}
