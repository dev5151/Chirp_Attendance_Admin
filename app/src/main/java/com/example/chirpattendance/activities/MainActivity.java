package com.example.chirpattendance.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;

import com.example.chirpattendance.fragments.FragmentSendKey;
import com.example.chirpattendance.interfaces.ChirpAttendance;
import com.example.chirpattendance.R;
import com.example.chirpattendance.fragments.FragmentLogin;
import com.example.chirpattendance.fragments.FragmentRooms;

public class MainActivity extends AppCompatActivity {

    private FragmentManager manager;
    static ChirpAttendance chirpAttendance;
    static String organizationKey;
    static String hashedKey;
    static String organizationPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        switchFragment(new FragmentLogin());


        chirpAttendance = new ChirpAttendance() {
            @Override
            public void goToSendKey(String key) {
                hashedKey = key;
                switchFragment(new FragmentSendKey());
            }

            @Override
            public void saveKeyPassword(String key, String password) {
                organizationKey = key;
                organizationPassword = password;
            }

            @Override
            public void goToRooms() {
                switchFragment(new FragmentRooms());
            }
        };

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
