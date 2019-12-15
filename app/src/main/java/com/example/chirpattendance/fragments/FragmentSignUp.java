package com.example.chirpattendance.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.chirpattendance.R;
import com.example.chirpattendance.activities.LoginActivity;
import com.example.chirpattendance.models.Organisation;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentSignUp extends Fragment {

    private EditText organizationName;
    private EditText customKey;
    private TextView login;
    private Button SignUp;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public FragmentSignUp() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initialize(rootView);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.getInterfaceLoginActivty().goToLoginFragment();
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyFields())
                {
                    reference.child("organization").child(organizationName.getText().toString()).
                            setValue(new Organisation(organizationName.getText().toString(), customKey.getText().toString()));
                    LoginActivity.getInterfaceLoginActivty().goToLoginFragment();

                }
                else
                {
                    Snackbar.make(SignUp, "Enter all the fields.", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });

        return rootView;
    }

    private void initialize(View rootView) {
        organizationName = rootView.findViewById(R.id.fragment_sign_up_organization_name);
        customKey = rootView.findViewById(R.id.fragment_sign_up_custom_key);
        login = rootView.findViewById(R.id.fragment_sign_up_login_text_view);
        SignUp = rootView.findViewById(R.id.fragment_sign_up_button);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    private boolean verifyFields() {
        if(organizationName.getText()!=null && organizationName.getText().length()>0
        && customKey.getText()!=null && customKey.getText().length()>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
