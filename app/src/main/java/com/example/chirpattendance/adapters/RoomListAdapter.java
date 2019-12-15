package com.example.chirpattendance.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chirpattendance.R;
import com.example.chirpattendance.activities.RoomActivity;
import com.example.chirpattendance.models.RoomList;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
    private SharedPreferences sharedPreferences;

    public RoomListAdapter(ArrayList<RoomList> list, ArrayList<String> hashedKeyList, Context context) {
        this.list = list;
        this.hashedKeyList = hashedKeyList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_room_list, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomList roomList = list.get(position);
        long startingUnixTime = Long.parseLong(roomList.getStartingUnixTime())*1000L;
        long endingUnixTime = Long.parseLong(roomList.getEndingUnixTime())*1000L;
        holder.agenda.setText(roomList.getAgenda());
        holder.location.setText(roomList.getLocation());
        Date d1 = new Date(startingUnixTime);
        Date d2 = new Date(endingUnixTime);
        String date = new SimpleDateFormat("dd MMMM yyyy").format(d1);
        holder.date.setText(date);
        String t1 = new SimpleDateFormat("HH:mm").format(d1);
        String t2 = new SimpleDateFormat("HH:mm").format(d2);
        holder.time.setText(t1+"-"+t2);

        if(System.currentTimeMillis() < endingUnixTime)
        {
            holder.date.setTextColor(Color.parseColor("#ffffff"));
            holder.agenda.setTextColor(Color.parseColor("#ffffff"));
            holder.location.setTextColor(Color.parseColor("#ffffff"));
            holder.time.setTextColor(Color.parseColor("#ffffff"));
            holder.cardViewHolder.setBackgroundColor(Color.parseColor("#89C2F7"));
            holder.less.setTextColor(Color.parseColor("#ffffff"));
            holder.more.setTextColor(Color.parseColor("#ffffff"));
            holder.less.setBackgroundColor(Color.parseColor("#91C1E2"));
            holder.more.setBackgroundColor(Color.parseColor("#91C1E2"));
            holder.more.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_white_24dp, 0);
            holder.less.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_up_white_24dp, 0);
        }

        else
        {
            holder.date.setTextColor(Color.parseColor("#000000"));
            holder.agenda.setTextColor(Color.parseColor("#000000"));
            holder.location.setTextColor(Color.parseColor("#000000"));
            holder.time.setTextColor(Color.parseColor("#000000"));
            holder.cardViewHolder.setBackgroundColor(Color.parseColor("#f2f2f2"));
            holder.less.setTextColor(Color.parseColor("#000000"));
            holder.more.setTextColor(Color.parseColor("#000000"));
            holder.less.setBackgroundColor(Color.parseColor("#D0D0D0"));
            holder.more.setBackgroundColor(Color.parseColor("#D0D0D0"));
            holder.more.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
            holder.less.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_up_black_24dp, 0);
        }

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
        public TextView more;
        public TextView less;
        public ConstraintLayout cardViewHolder;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            agenda = itemView.findViewById(R.id.agenda_text_view_recycler_view);
            location = itemView.findViewById(R.id.location_text_view_recycler_view);
            time = itemView.findViewById(R.id.time_text_view_recycler_view);
            date = itemView.findViewById(R.id.date_text_view_recycler_view);
            more = itemView.findViewById(R.id.more_text_view);
            less = itemView.findViewById(R.id.less_text_view);
            cardViewHolder = itemView.findViewById(R.id.room_list_item_recycler);

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    location.setVisibility(View.VISIBLE);
                    less.setVisibility(View.VISIBLE);
                    more.setVisibility(View.GONE);
                }
            });

            less.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    location.setVisibility(View.GONE);
                    less.setVisibility(View.GONE);
                    more.setVisibility(View.VISIBLE);
                }
            });

           itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int Position = getAdapterPosition();
                    final String hashedKey = hashedKeyList.get(Position);
                    reference = FirebaseDatabase.getInstance().getReference();
                    reference.child("rooms").child(hashedKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue()!=null) {
                                String endingTime = dataSnapshot.child("endingUnixTime").getValue().toString();
                                if (Long.parseLong(endingTime) * 1000L < System.currentTimeMillis()) {
                                    RoomActivity.getChirpAttendance().goToMeeting(1, hashedKey);
                                }
                                else
                                {
                                    RoomActivity.getChirpAttendance().goToMeeting(2, hashedKey);
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

    }

}
