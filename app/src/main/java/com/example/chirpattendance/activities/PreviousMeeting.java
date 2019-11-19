package com.example.chirpattendance.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.chirpattendance.R;
import com.example.chirpattendance.fragments.FragmentPreviousMeetingAttendeesList;
import com.example.chirpattendance.fragments.FragmentPreviousMeetingDeafaultersList;
import com.example.chirpattendance.interfaces.PreviousMeetingInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class PreviousMeeting extends AppCompatActivity {

    private static String roomKey;
    static Toolbar topbar;
    static BottomNavigationView bottomNavigationView;
    static PreviousMeetingInterface previousMeetingInterface;
    private FragmentManager manager = getSupportFragmentManager();


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(manager.getBackStackEntryCount() == 0)
        {
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_meeting);

        topbar = findViewById(R.id.topbar_previous_meeting);

        Intent intent = getIntent();
        roomKey = intent.getStringExtra("Key");

        replaceFragment(new FragmentPreviousMeetingAttendeesList());

        topbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.logout:
                        Snackbar snackbar = Snackbar.make(topbar, "Do You Want To Logout", Snackbar.LENGTH_SHORT);
                        snackbar.setAction("Logout", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                finishAffinity();
                            }
                        });
                        snackbar.show();
                }
                return true;
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation_bar_previous_meeting);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.attendees :
                        replaceFragment(new FragmentPreviousMeetingAttendeesList());
                        return true;
                    case R.id.defaulters :
                        replaceFragment(new FragmentPreviousMeetingDeafaultersList());
                        return true;
                }
                return true;
            }
        });

        previousMeetingInterface = new PreviousMeetingInterface() {
            @Override
            public void topBarSetText(String text) {
                topbar.setTitle(text);
            }
        };

    }


    public static String getRoomKey() {
        return roomKey;
    }

    public static PreviousMeetingInterface getPreviousMeetingInterface() {
        return previousMeetingInterface;
    }


    void switchFragment(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.previous_meeting_frame, fragment)
                .addToBackStack(null)
                .commit();
    }

    void replaceFragment(Fragment fragment)
    {
        manager.beginTransaction()
                .replace(R.id.previous_meeting_frame, fragment)
                .commit();
    }
}
