package com.example.chirpattendance.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chirpattendance.R;
import com.example.chirpattendance.activities.MeetingActivity;
import com.example.chirpattendance.adapters.RequestListAdapter;
import com.example.chirpattendance.models.AttendeesDefaultersList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentRequestsList extends Fragment {

    private DatabaseReference reference;
    private ArrayList<String> uniqueNumber;
    private ArrayList<AttendeesDefaultersList> requestList;
    private RecyclerView recyclerView;
    private RequestListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String hashedKey;
    private SharedPreferences sharedPreferences;
    private CheckBox checkBox;
    private TextView back;

    public FragmentRequestsList() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_request_list, container, false);
        initialize(rootView);
        getRequestList();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MeetingActivity.getInterfaceMeetingActivity().backPresssed();
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    private void initialize(View rootView) {
        reference = FirebaseDatabase.getInstance().getReference();
        uniqueNumber = new ArrayList<>();
        requestList = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.recycler_attendees_list_view);
        adapter = new RequestListAdapter(requestList, uniqueNumber);
        layoutManager = new LinearLayoutManager(getContext());
        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        hashedKey = sharedPreferences.getString("MeetingHashedKey", null);
        checkBox = rootView.findViewById(R.id.checkBox);
        back = rootView.findViewById(R.id.back);
    }

    private void getRequestList() {
        reference.child("rooms").child(hashedKey).child("requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uniqueNumber.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String unique = snapshot.getValue().toString();
                    uniqueNumber.add(unique);
                }

                reference.child("users").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(String unique : uniqueNumber)
                        {
                            if(dataSnapshot.child(unique).exists())
                            {
                                DataSnapshot snap = dataSnapshot.child(unique);
                                AttendeesDefaultersList attendee = new AttendeesDefaultersList(snap.child("name").getValue().toString(),
                                        snap.child("email").getValue().toString(),
                                        snap.child("user_id").getValue().toString());
                                requestList.add(attendee);
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
