package com.example.chirpattendance.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.chirpattendance.R;
import com.example.chirpattendance.activities.LoginActivity;
import com.example.chirpattendance.adapters.ImageSliderAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class FragmentLaunchingActivity extends Fragment {
    int counter = 0;
    private ViewPager viewPager;
    private Timer timer;
    private int[] images = {R.drawable.ic_undraw_exams, R.drawable.ic_undraw_community, R.drawable.ic_undraw_conference};
    private ImageView enter;

    public FragmentLaunchingActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_launching, container, false);

        viewPager = rootView.findViewById(R.id.image_slider_view_pager);
        ImageSliderAdapter adapter = new ImageSliderAdapter(getContext(), images);
        viewPager.setAdapter(adapter);
        enter = rootView.findViewById(R.id.custom_button_launching_fragment);

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if(counter == images.length-1) {
                    counter = 0;
                }
                viewPager.setCurrentItem(counter++, true);
            }
        };

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        },500, 2500);
        return rootView;
    }
}
