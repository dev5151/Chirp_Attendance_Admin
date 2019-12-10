package com.example.chirpattendance.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.chirpattendance.fragments.FragmentRooms;
import com.example.chirpattendance.fragments.MeetingDetailsDialogBox;
import com.example.chirpattendance.interfaces.ChirpAttendance;
import com.example.chirpattendance.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RoomActivity extends AppCompatActivity {

    private FragmentManager manager;
    static ChirpAttendance chirpAttendance;
    static String organizationKey;
    static String hashedKey;
    static String organizationPassword;
    private MeetingDetailsDialogBox meetingDetailsBottomSheet = new MeetingDetailsDialogBox();
    public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        initialize();
        switchFragment(new FragmentRooms());

        /*chirpAttendance = new ChirpAttendance() {
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
            public void goToPreviousMeetingActivity(String hashedKey) {
                Intent intent = new Intent(RoomActivity.this, PreviousMeeting.class);
                intent.putExtra("Key", hashedKey);
                startActivity(intent);
            }

        };*/
    }
    private void initialize() {
        manager = getSupportFragmentManager();
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
