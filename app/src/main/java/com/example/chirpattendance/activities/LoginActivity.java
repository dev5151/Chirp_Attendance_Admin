package com.example.chirpattendance.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
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

    @Override
    public void onBackPressed() {
        if(fragmentStack.size() == 1)
        {
            finish();
        }
        else {
            fragmentStack.pop();
            fragmentTransition(fragmentStack.peek());
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
        };
    }



    public void initialize()
    {
        mFrameLayout = findViewById(R.id.login_signup_frame);
        mFragmentManager = getSupportFragmentManager();
    }

    private void fragmentTransition(Fragment fragment) {
        mFragmentManager.beginTransaction()
                .replace(R.id.login_signup_frame, fragment)
                .commit();
    }

    public static InterfaceLoginActivty getInterfaceLoginActivty() {
        return interfaceLoginActivty;
    }
}
