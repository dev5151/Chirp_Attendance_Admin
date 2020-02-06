package com.example.chirpattendance.adapters;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chirpattendance.R;
import com.example.chirpattendance.activities.RoomActivity;
import com.example.chirpattendance.models.AttendeesDefaultersList;
import com.example.chirpattendance.models.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.ViewHolder> {

    private ArrayList<AttendeesDefaultersList> requestList;
    private static ArrayList<String> checkedList;
    private ArrayList<String> hashedKey;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public RequestListAdapter() {
    }

    public RequestListAdapter(ArrayList<AttendeesDefaultersList> requestList, ArrayList<String> hashedKey) {
        this.requestList = requestList;
        this.hashedKey = hashedKey;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_requests_list_item, parent, false);
        return new RequestListAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        AttendeesDefaultersList attendeesList1 = requestList.get(position);
        holder.name.setText(attendeesList1.getName());
        holder.email.setText(attendeesList1.getEmail());
        holder.uniqueId.setText(attendeesList1.getUniqueId());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String Studentkey = hashedKey.get(position);
                reference.child("rooms").child(RoomActivity.getHashedKey()).child("requests").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            if(snapshot.getValue().equals(Studentkey))
                            {
                                checkedList.add(Studentkey);
                                /*reference.child("rooms").child(RoomActivity.getHashedKey()).child("attendees").child(snapshot.getKey()).setValue(Studentkey);
                                reference.child("rooms").child(RoomActivity.getHashedKey()).child("requests").child(snapshot.getKey()).setValue(null);*/

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        private TextView email;
        private TextView uniqueId;
        public TextView more;
        public TextView less;
        public CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_text_view_recycler_view);
            email = itemView.findViewById(R.id.email_text_view_recycler_view);
            uniqueId = itemView.findViewById(R.id.uid_text_view_recycler_view);
            more = itemView.findViewById(R.id.more_text_view);
            less = itemView.findViewById(R.id.less_text_view);
            checkBox = itemView.findViewById(R.id.checkBox);


            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uniqueId.setVisibility(View.VISIBLE);
                    less.setVisibility(View.VISIBLE);
                    more.setVisibility(View.GONE);
                }
            });

            less.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uniqueId.setVisibility(View.GONE);
                    less.setVisibility(View.GONE);
                    more.setVisibility(View.VISIBLE);
                }
            });



        }
    }

}
