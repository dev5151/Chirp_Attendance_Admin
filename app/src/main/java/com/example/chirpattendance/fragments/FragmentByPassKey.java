package com.example.chirpattendance.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chirpattendance.R;
import com.example.chirpattendance.activities.RoomActivity;

public class FragmentByPassKey extends Fragment {

    public FragmentByPassKey() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_by_pass_key, container, false);

        RoomActivity.getChirpAttendance().topBarSetText("By Pass Key");

        TextView byPass = rootView.findViewById(R.id.by_pass_key_text_view);
        byPass.setText(RoomActivity.getHashedKey());

        return rootView;
    }
}
