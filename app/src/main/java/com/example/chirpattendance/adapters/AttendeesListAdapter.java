package com.example.chirpattendance.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chirpattendance.R;
import com.example.chirpattendance.models.AttendeesDefaultersList;

import java.util.ArrayList;

public class AttendeesListAdapter extends RecyclerView.Adapter<AttendeesListAdapter.ViewHolder> {

    private ArrayList<AttendeesDefaultersList> attendeesList;

    public AttendeesListAdapter() {
    }

    public AttendeesListAdapter(ArrayList<AttendeesDefaultersList> attendeesList) {
        this.attendeesList = attendeesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_attendees_defaulters_list, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendeesDefaultersList attendeesList1 = attendeesList.get(position);
        holder.name.setText(attendeesList1.getName());
        holder.email.setText(attendeesList1.getEmail());
        holder.uniqueId.setText(attendeesList1.getUniqueId());
    }

    @Override
    public int getItemCount() {
        return attendeesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        private TextView email;
        private TextView uniqueId;
        public TextView more;
        public TextView less;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_text_view_recycler_view);
            email = itemView.findViewById(R.id.email_text_view_recycler_view);
            uniqueId = itemView.findViewById(R.id.uid_text_view_recycler_view);
            more = (TextView) itemView.findViewById(R.id.more_text_view);
            less = (TextView) itemView.findViewById(R.id.less_text_view);

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
