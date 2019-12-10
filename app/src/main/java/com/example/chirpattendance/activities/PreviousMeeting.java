package com.example.chirpattendance.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.chirpattendance.R;
import com.example.chirpattendance.adapters.DefaultersAttendeesAdapter;
import com.example.chirpattendance.interfaces.PreviousMeetingInterface;
import com.google.android.material.tabs.TabLayout;

public class PreviousMeeting extends AppCompatActivity {

    private static String roomKey;
    private ViewPager viewPager;
    static PreviousMeetingInterface previousMeetingInterface;
    private FragmentManager manager = getSupportFragmentManager();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_meeting);

        initializeView();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_previous_meeting);


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.setAdapter(new DefaultersAttendeesAdapter(getSupportFragmentManager()));
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                page.setRotationY(position * 30);
            }
        });
    }

    private void initializeView() {
        viewPager = findViewById(R.id.previous_meeting_view_pager);
    }


    public static String getRoomKey() {
        return roomKey;
    }

    public static PreviousMeetingInterface getPreviousMeetingInterface() {
        return previousMeetingInterface;
    }

}
