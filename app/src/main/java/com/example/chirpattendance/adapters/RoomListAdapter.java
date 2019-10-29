package com.example.chirpattendance.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chirpattendance.R;
import com.example.chirpattendance.models.RoomList;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {

    private ArrayList <RoomList> list = new ArrayList<>();

    public RoomListAdapter(ArrayList<RoomList> list) {
        this.list = list;
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



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView agenda;
        public TextView location;
        public TextView date;
        public TextView time;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            agenda = (TextView)itemView.findViewById(R.id.agenda_text_view_recycler_view);
            location = (TextView)itemView.findViewById(R.id.location_text_view_recycler_view);
            time = (TextView)itemView.findViewById(R.id.time_text_view_recycler_view);
            date = (TextView)itemView.findViewById(R.id.date_text_view_recycler_view);
        }

        @Override
        public void onClick(View view) {

        }
    }

}
