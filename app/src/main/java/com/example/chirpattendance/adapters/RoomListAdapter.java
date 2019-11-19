package com.example.chirpattendance.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chirpattendance.R;
import com.example.chirpattendance.activities.PreviousMeeting;
import com.example.chirpattendance.activities.RoomActivity;
import com.example.chirpattendance.models.RoomList;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {

    private ArrayList <RoomList> list ;
    private ArrayList<String> hashedKeyList;
    private DatabaseReference reference;
    private Context context;

    public RoomListAdapter(ArrayList<RoomList> list, ArrayList<String> hashedKeyList, Context context) {
        this.list = list;
        this.hashedKeyList = hashedKeyList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_room_list, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomList roomList = list.get(position);
        holder.agenda.setText(roomList.getAgenda());
        holder.location.setText(roomList.getLocation());

        long startingUnixTime = Long.parseLong(roomList.getStartingUnixTime())*1000L;
        long endingUnixTime = Long.parseLong(roomList.getEndingUnixTime())*1000L;

        Date d1 = new Date(startingUnixTime);
        Date d2 = new Date(endingUnixTime);
        String date = new SimpleDateFormat("dd MMMM yyyy").format(d1);

        holder.date.setText(date);

        String t1 = new SimpleDateFormat("HH:mm").format(d1);
        String t2 = new SimpleDateFormat("HH:mm").format(d2);

        holder.time.setText(t1+"-"+t2);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView agenda;
        public TextView location;
        public TextView date;
        public TextView time;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            agenda = (TextView) itemView.findViewById(R.id.agenda_text_view_recycler_view);
            location = (TextView) itemView.findViewById(R.id.location_text_view_recycler_view);
            time = (TextView) itemView.findViewById(R.id.time_text_view_recycler_view);
            date = (TextView) itemView.findViewById(R.id.date_text_view_recycler_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int Position = getAdapterPosition();
                    final String hashedKey = hashedKeyList.get(Position+1);
                    reference = FirebaseDatabase.getInstance().getReference();
                    reference.child("rooms").child(hashedKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue()!=null) {
                                String endingTime = dataSnapshot.child("endingUnixTime").getValue().toString();
                                if (Long.parseLong(endingTime) * 1000L < System.currentTimeMillis()) {
                                    RoomActivity.getChirpAttendance().goToPreviousMeetingActivity(hashedKey);

                                } else {
                                    RoomActivity.getChirpAttendance().goToSendKey(hashedKey);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {
                    int Position = getAdapterPosition();
                    final String hashedKey = hashedKeyList.get(Position+1);
                    reference = FirebaseDatabase.getInstance().getReference();
                    reference.child("rooms").child(hashedKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                String endingTime = dataSnapshot.child("endingUnixTime").getValue().toString();
                                if (Long.parseLong(endingTime) * 1000L < System.currentTimeMillis()) {
                                    Toast toast = Toast.makeText(context, "Meeting Already Over", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                else {
                                    Snackbar.make(view, "Cancel Meeting", Snackbar.LENGTH_SHORT)
                                            .setAction("Cancel", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    reference.child("rooms").child(hashedKey).getRef().setValue(null);
                                                    reference.child("Admin").child(RoomActivity.getOrganizationKey()).child("rooms").addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                if (snapshot.getValue().toString().equals(hashedKey)) {
                                                                    snapshot.getRef().setValue(null);
                                                                    Toast toast = Toast.makeText(context, "Meeting Cancelled", Toast.LENGTH_SHORT);
                                                                    toast.show();
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        }
                                                    });
                                                }
                                            })
                                            .show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    return true;
                }
            });
        }

    }

}
