package com.example.chirpattendance.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.chirpattendance.R;
import com.example.chirpattendance.fragments.LogoutBottomSheet;
import com.example.chirpattendance.interfaces.InterfaceMeetingActivity;
import com.example.chirpattendance.viewPager.CurrentMeetingViewPager;
import com.example.chirpattendance.viewPager.PreviousMeetingViewPager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MeetingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SharedPreferences sharedPreferences;
    private int meetingType;
    static InterfaceMeetingActivity interfaceMeetingActivity;


    @Override
    public void onBackPressed() {
       Back();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_meeting);

        initializeView();


        interfaceMeetingActivity = new InterfaceMeetingActivity() {
            @Override
            public void backPresssed() {
                Back();
            }
        };

        if(meetingType == 1)
        {
            viewPager.setAdapter(new PreviousMeetingViewPager(getSupportFragmentManager()));
        }
        else
        {
            viewPager.setAdapter(new CurrentMeetingViewPager(getSupportFragmentManager()));
        }
    }

    private void initializeView() {
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        meetingType = sharedPreferences.getInt("MeetingType", 0);
        viewPager = findViewById(R.id.previous_meeting_view_pager);
    }

    public static InterfaceMeetingActivity getInterfaceMeetingActivity() {
        return interfaceMeetingActivity;
    }

    private void Back()
    {
        Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
        startActivity(intent);
        finish();
    }
}
