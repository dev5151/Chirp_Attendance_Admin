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

import com.example.chirpattendance.fragments.FragmentAttendeesList;
import com.example.chirpattendance.fragments.FragmentByPassKey;
import com.example.chirpattendance.fragments.FragmentDefaultersList;
import com.example.chirpattendance.fragments.FragmentRooms;
import com.example.chirpattendance.fragments.FragmentSendKey;
import com.example.chirpattendance.fragments.MeetingDetailsBottomSheet;
import com.example.chirpattendance.interfaces.ChirpAttendance;
import com.example.chirpattendance.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class RoomActivity extends AppCompatActivity {

    private FragmentManager manager;
    static ChirpAttendance chirpAttendance;
    static String organizationKey;
    static String hashedKey;
    static String organizationPassword;
    private MeetingDetailsBottomSheet meetingDetailsBottomSheet = new MeetingDetailsBottomSheet();
    public BottomNavigationView bottomNavigationView;
    static Toolbar topbar;
    private SharedPreferences sharedPreferences;

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
        setContentView(R.layout.activity_room);

        initialize();
        switchFragment(new FragmentRooms());

        Intent intent = getIntent();
        organizationKey = intent.getExtras().getString("key");
        organizationPassword = intent.getExtras().getString("password");

        topbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Snackbar snackbar = Snackbar.make(topbar, "Do You Want To Logout", Snackbar.LENGTH_SHORT);
                snackbar.setAction("Logout", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        finish();
                    }
                });
                snackbar.show();
                return true;
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.roomKey:
                        replaceFragment(new FragmentSendKey());
                        return true;

                    case R.id.attendees:
                        replaceFragment(new FragmentAttendeesList());
                        return true;

                    case R.id.defaulters:
                        replaceFragment(new FragmentDefaultersList());
                        return true;

                    case R.id.byPassKey:
                        replaceFragment(new FragmentByPassKey());
                        return true;

                }
                return false;
            }
        });

        chirpAttendance = new ChirpAttendance() {
            @Override
            public void goToSendKey(String key) {
                hashedKey = key;
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                switchFragment(new FragmentSendKey());
            }

            @Override
            public void showBottomSheet() {
                meetingDetailsBottomSheet.show(getSupportFragmentManager(), meetingDetailsBottomSheet.getTag());
            }

            @Override
            public void hideBottomSheet() {
                meetingDetailsBottomSheet.dismiss();
            }

            @Override
            public void hideBottomNavigationView() {
                bottomNavigationView.setVisibility(View.GONE);
            }

            @Override
            public void showBottomNavigationView() {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }

            @Override
            public void topBarSetText(String text) {
                topbar.setTitle(text);
            }

            @Override
            public void goToPreviousMeetingActivity(String hashedKey) {
                Intent intent = new Intent(RoomActivity.this, PreviousMeeting.class);
                intent.putExtra("Key", hashedKey);
                startActivity(intent);
            }

            @Override
            public void setBottomNavigationBarItem(int itemId) {
                View view = bottomNavigationView.findViewById(itemId);
                view.performClick();
            }
        };
    }

    private void initialize() {
        manager = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar_currrent_meeting);
        topbar = findViewById(R.id.topbar_current_meeting);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    }



    void switchFragment(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack(null)
                .commit();
    }

    void replaceFragment(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }

    public static ChirpAttendance getChirpAttendance() {
        return chirpAttendance;
    }

    public static String getOrganizationKey() {
        return organizationKey;
    }

    public static String getHashedKey() {
        return hashedKey;
    }

    public static String getOrganizationPassword() {
        return organizationPassword;
    }

}
