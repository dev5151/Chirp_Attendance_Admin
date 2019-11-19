package com.example.chirpattendance.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.example.chirpattendance.R;
import com.example.chirpattendance.fragments.FragmentLogin;

public class LoginActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_login);

        manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.login_frame, new FragmentLogin())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            Toast toast = Toast.makeText(this, "Please connect to your Internet", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
