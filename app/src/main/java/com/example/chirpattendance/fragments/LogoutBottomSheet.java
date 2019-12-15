package com.example.chirpattendance.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chirpattendance.R;
import com.example.chirpattendance.activities.LoginActivity;
import com.example.chirpattendance.activities.RoomActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutBottomSheet extends BottomSheetDialogFragment {

    private MaterialButton yesButton;
    private MaterialButton noButton;
    private SharedPreferences preferences;

    public LogoutBottomSheet() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.bottom_sheet_logout, container, false);
        initialize(rootView);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("LoginState", 0);
                editor.apply();
                startActivity(intent);
                getActivity().finish();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoomActivity.getChirpAttendance().closeBottomSheet();
            }
        });
        return rootView;
    }

    private void initialize(View rootView) {
        yesButton = rootView.findViewById(R.id.yes_logout);
        noButton = rootView.findViewById(R.id.no_logout);
        preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }
}
