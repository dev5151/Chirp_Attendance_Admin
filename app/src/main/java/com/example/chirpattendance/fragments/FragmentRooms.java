package com.example.chirpattendance.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chirpattendance.activities.LoginActivity;
import com.example.chirpattendance.activities.RoomActivity;
import com.example.chirpattendance.R;
import com.example.chirpattendance.adapters.RoomListAdapter;
import com.example.chirpattendance.models.RoomList;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentRooms extends Fragment {

    private String organizationName;
    private String password;
    private String uid;
    private ImageView createRoom;
    private SharedPreferences sharedPreferences;
    private FirebaseDatabase database;
    private ArrayList<RoomList> roomList = new ArrayList<>();
    private ArrayList<String> hashedKeyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RoomListAdapter adapter;
    private DatabaseReference reference;
    private ProgressBar progressBar;
    private TextView logout;


    public FragmentRooms() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_rooms, container, false);
        initialize(rootView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        recyclerView.setAdapter(adapter);
        adapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);
        getRoomList();


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoomActivity.getChirpAttendance().logout();
            }
        });

        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roomList.size() != 0) {
                    RoomList room = roomList.get(roomList.size() - 1);
                    if (Long.parseLong(room.getEndingUnixTime()) * 1000L < System.currentTimeMillis()) {
                        RoomActivity.getChirpAttendance().showMeetingDialogBox();
                    } else {
                        Snackbar.make(createRoom, "You are already in a meeting", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    RoomActivity.getChirpAttendance().showMeetingDialogBox();
                }
            }
        });

        return rootView;
    }

    private void initialize(View rootView) {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        recyclerView = rootView.findViewById(R.id.recycler_room_list_view);
        adapter = new RoomListAdapter(roomList, hashedKeyList, getContext());
        progressBar = rootView.findViewById(R.id.progress_bar_room);
        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", null);
        organizationName = sharedPreferences.getString("key", null);
        password = sharedPreferences.getString("password", null);
        createRoom = rootView.findViewById(R.id.create_room_button);
        logout = rootView.findViewById(R.id.fragment_rooms_logout_button);
        RoomActivity.getChirpAttendance().setOrganiztionName(organizationName);
    }


    private void getRoomList() {
        progressBar.setVisibility(View.VISIBLE);
        reference.child("organization").child(organizationName).child("admin").child(uid).child("rooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hashedKeyList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String hashedKey = postSnapshot.getValue().toString();
                    hashedKeyList.add(hashedKey);
                }

                reference.child("rooms").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        roomList.clear();
                        for (String hashKey : hashedKeyList) {
                            if (dataSnapshot.child(hashKey).exists()) {
                                DataSnapshot snap = dataSnapshot.child(hashKey);
                                RoomList room = new RoomList(snap.child("location").getValue().toString(),
                                        snap.child("agenda").getValue().toString(),
                                        snap.child("hashedKey").getValue().toString(),
                                        snap.child("endingUnixTime").getValue().toString(),
                                        snap.child("startingUnixTime").getValue().toString());
                                roomList.add(room);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
