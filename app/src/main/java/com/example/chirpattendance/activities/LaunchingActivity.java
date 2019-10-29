package com.example.chirpattendance.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.chirpattendance.R;
import com.example.chirpattendance.fragments.FragmentLaunchingActivity;

public class LaunchingActivity extends AppCompatActivity {

    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);

        manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.launching_activity_container, new FragmentLaunchingActivity())
                .addToBackStack(null)
                .commit();
    }
}
