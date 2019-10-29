package com.example.chirpattendance.fragments;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chirpattendance.activities.MainActivity;
import com.example.chirpattendance.R;
import com.example.chirpattendance.adapters.RoomListAdapter;
import com.example.chirpattendance.models.RoomList;
import com.example.chirpattendance.models.UserRoom;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class FragmentRooms extends Fragment {

    private ImageView newRoom;
    private FirebaseDatabase database;
    private ArrayList<RoomList> roomList = new ArrayList<>();
    private ArrayList<String> hashedKeyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RoomListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference reference;
    private String Id;
    private GridLayoutManager gridLayoutManager;

    public FragmentRooms() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rooms, container, false);
        initialize(rootView);

        newRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MeetingDetailsBottomSheet bottomSheet = new MeetingDetailsBottomSheet();
                bottomSheet.show(getFragmentManager(), bottomSheet.getTag());
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        getRoomList();
        return rootView;
    }

    private void initialize(View rootView) {
        newRoom = rootView.findViewById(R.id.new_room_button_fragment_rooms);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        recyclerView = rootView.findViewById(R.id.recycler_room_list_view);
        adapter = new RoomListAdapter(roomList);
        layoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
    }

    private void getRoomList() {
        reference.child("Admin").child(MainActivity.getOrganizationKey()).child("rooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hashedKeyList.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    String hashedKey = postSnapshot.getValue().toString();
                    hashedKeyList.add(hashedKey);
                }

                reference.child("rooms").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        roomList.clear();
                        for( String hashKey : hashedKeyList)
                        {
                            if(dataSnapshot.child(hashKey).exists())
                            {
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
