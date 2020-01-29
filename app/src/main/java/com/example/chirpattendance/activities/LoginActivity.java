package com.example.chirpattendance.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.transition.Slide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.widget.FrameLayout;

import com.example.chirpattendance.R;
import com.example.chirpattendance.fragments.FragmentLogin;
import com.example.chirpattendance.fragments.FragmentSignUp;
import com.example.chirpattendance.interfaces.InterfaceLoginActivty;

import java.util.Stack;

public class LoginActivity extends AppCompatActivity {

    private FrameLayout mFrameLayout;
    static InterfaceLoginActivty interfaceLoginActivty;
    private FragmentManager mFragmentManager;
    private Stack<Fragment> fragmentStack = new Stack<>();
    private SharedPreferences preferences;

    @Override
    public void onBackPressed() {
        if(fragmentStack.size() == 1)
        {
            finish();
        }
        else
            {
            fragmentStack.pop();
            fragmentTransition(fragmentStack.peek());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(preferences.getInt("LoginState", 0) == 1)
        {
            Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        fragmentTransition(new FragmentLogin());
        fragmentStack.push(new FragmentLogin());

        interfaceLoginActivty = new InterfaceLoginActivty() {
            @Override
            public void goToLoginFragment() {
                fragmentStack.pop();
                fragmentTransition(fragmentStack.peek());
            }

            @Override
            public void goToSignUpFragment() {
                fragmentStack.push(new FragmentSignUp());
                fragmentTransition(fragmentStack.peek());
            }

            @Override
            public void goTOGoogleAuthFragment() {

            }
        };
    }



    public void initialize()
    {
        mFrameLayout = findViewById(R.id.login_signup_frame);
        mFragmentManager = getSupportFragmentManager();
        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    }

    private void fragmentTransition(Fragment fragment) {
        fragment.setEnterTransition(new Fade());
        fragment.setExitTransition(new Fade());
        mFragmentManager.beginTransaction()
                .replace(R.id.login_signup_frame, fragment)
                .commit();
    }

    public static InterfaceLoginActivty getInterfaceLoginActivty() {
        return interfaceLoginActivty;
    }
}
