package com.example.chirpattendance.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.chirpattendance.fragments.FragmentRooms;
import com.example.chirpattendance.fragments.LogoutBottomSheet;
import com.example.chirpattendance.fragments.MeetingDetailsDialogBox;
import com.example.chirpattendance.interfaces.ChirpAttendance;
import com.example.chirpattendance.R;
import com.google.firebase.auth.FirebaseAuth;


public class RoomActivity extends AppCompatActivity {

    private FragmentManager manager;
    static ChirpAttendance chirpAttendance;
    static String organizationKey;
    static String MeetingKey;
    static String uid;
    static MeetingDetailsDialogBox cdd;
    private SharedPreferences sharedPreferences;
    static LogoutBottomSheet logoutBottomSheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        initialize();
        switchFragment(new FragmentRooms());

        chirpAttendance = new ChirpAttendance() {
            @Override
            public void showMeetingDialogBox() {
                cdd=new MeetingDetailsDialogBox();
                cdd.show(getSupportFragmentManager(), "MeetingDialogFragment");
            }

            @Override
            public void goToMeeting(int i, String hashedKey) {
                MeetingKey = hashedKey;
                Intent intent = new Intent(getApplicationContext(), MeetingActivity.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("MeetingHashedKey", hashedKey);
                editor.putInt("MeetingType", i);
                editor.apply();
                startActivity(intent);
                finish();
            }

            @Override
            public void setOrganiztionName(String organiztionName) {
                organizationKey = organiztionName;
            }

            @Override
            public void setAdminUid(String adminUid) {
                uid = adminUid;
            }

            @Override
            public void dismissMeetingDialogBox() {
                cdd.dismiss();
            }

            @Override
            public void logout() {
                RoomActivity.chirpAttendance.openBottomSheet();
            }

            @Override
            public void openBottomSheet() {
                if(!logoutBottomSheet.isAdded())
                    logoutBottomSheet.show(getSupportFragmentManager(), "LogoutBottomSheet");
                else
                    RoomActivity.chirpAttendance.closeBottomSheet();
            }

            @Override
            public void closeBottomSheet() {
                logoutBottomSheet.dismiss();
            }
        };
    }
    private void initialize() {
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        manager = getSupportFragmentManager();
        logoutBottomSheet = new LogoutBottomSheet();
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
        return MeetingKey;
    }

    public static String getUid() {
        return uid;
    }
}
