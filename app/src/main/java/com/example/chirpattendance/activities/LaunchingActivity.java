package com.example.chirpattendance.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;

import com.example.chirpattendance.R;
import com.example.chirpattendance.fragments.FragmentLaunchingActivity;
import com.example.chirpattendance.fragments.FragmentSplash;

public class LaunchingActivity extends AppCompatActivity {

    private FragmentManager manager;

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
        setContentView(R.layout.activity_launching);

        initializeView();
        transaction(new FragmentSplash());

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                transaction(new FragmentLaunchingActivity());
            }
        }, 4000);

    }

    private void initializeView() {
        manager = getSupportFragmentManager();
    }

    private void transaction(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.launching_activity_container, fragment)
                .commit();
    }
}
