package com.example.chirpattendance.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chirpattendance.R;
import com.example.chirpattendance.activities.MeetingActivity;
import com.example.chirpattendance.adapters.AttendeesListAdapter;
import com.example.chirpattendance.models.AttendeesDefaultersList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FragmentPreviousMeetingAttendeesList extends Fragment {

    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private AttendeesListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<AttendeesDefaultersList> attendeeList;
    private ArrayList<String> uniqueNumber;
    private String hashedKey;
    private SharedPreferences sharedPreferences;
    private TextView back, csv;

    public FragmentPreviousMeetingAttendeesList() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attendees_list, container, false);
        initialize(rootView);
        getAttendeesList();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MeetingActivity.getInterfaceMeetingActivity().backPresssed();
            }
        });
        csv.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                try {
                    generateCsv();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    private void initialize(View rootView) {
        reference = FirebaseDatabase.getInstance().getReference();
        attendeeList = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.recycler_attendees_list_view);
        adapter = new AttendeesListAdapter(attendeeList);
        uniqueNumber = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());
        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        hashedKey = sharedPreferences.getString("MeetingHashedKey", null);
        back = rootView.findViewById(R.id.back);
        csv = rootView.findViewById(R.id.csv);
    }

    private void getAttendeesList() {
        reference.child("rooms").child(hashedKey).child("attendees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                {
                    uniqueNumber.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String unique = snapshot.getValue().toString();
                        uniqueNumber.add(unique);
                    }

                    reference.child("users").addValueEventListener(new ValueEventListener() {

                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (String unique : uniqueNumber) {
                                if (dataSnapshot.child(unique).exists()) {
                                    DataSnapshot snap = dataSnapshot.child(unique);
                                    AttendeesDefaultersList attendee = new AttendeesDefaultersList(snap.child("name").getValue().toString(),
                                            snap.child("email").getValue().toString(),
                                            snap.child("user_id").getValue().toString());
                                    attendeeList.add(attendee);

                                    final ArrayList<String>arrayList=new ArrayList<>();
                                    arrayList.add(attendee.getUniqueId());

                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

@RequiresApi(api = Build.VERSION_CODES.N)
private void generateCsv() throws IOException {
    FileWriter writer = null;
        writer = new FileWriter("/Users/artur/tmp/csv/sto1.csv");
        writer.write(collect);
        writer.close();
    String collect = arrayList.getUniqueId().stream().collect(Collectors.joining(","));
}
    }


