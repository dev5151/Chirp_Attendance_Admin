package com.example.chirpattendance.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.chirpattendance.activities.RoomActivity;
import com.example.chirpattendance.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentLogin extends Fragment {

    private EditText adminPassword;
    private EditText adminKey;
    private ImageView proceed;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    public FragmentLogin() {
    }

    @Override
    public void onStart() {
        super.onStart();
        if(preferences.getInt("LoginState", 0)==1)
        {
            Intent intent = new Intent(getActivity(), RoomActivity.class);
            intent.putExtra("key", preferences.getString("key", null));
            intent.putExtra("password", preferences.getString("password", null));
            getActivity().finish();
            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initialize(rootView);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adminPassword.getText() != null && adminPassword.getText().length() > 0
                        && adminKey.getText() != null && adminKey.getText().length() > 0) {
                    verifyKey(adminKey.getText().toString(), adminPassword.getText().toString());
                } else {
                    makeToast("Enter All Fields");
                }
            }
        });
        return rootView;
    }

    private void initialize(View rootView) {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        adminPassword = (rootView).findViewById(R.id.admin_password_edit_text);
        adminKey = (rootView).findViewById(R.id.admin_key_edit_text);
        proceed = (rootView).findViewById(R.id.proceed_button);
        preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    private void verifyKey(final String adminKeyText, final String adminPasswordText) {
        reference.child("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(adminKeyText).exists() &&
                        dataSnapshot.child(adminKeyText).child("authPassword").getValue().equals(adminPasswordText)) {
                    Intent intent = new Intent(getActivity(), RoomActivity.class);
                    intent.putExtra("key", adminKeyText);
                    intent.putExtra("password", adminPasswordText);
                    getActivity().finish();
                    editor = preferences.edit();
                    editor.putInt("LoginState", 1);
                    editor.putString("key", adminKeyText);
                    editor.putString("password", adminPasswordText);
                    editor.apply();
                    startActivity(intent);

                } else {
                    makeToast("Wrong Details Entered");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeToast(databaseError.getMessage());
            }
        });
    }

    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
                .show();
    }
}
