package com.example.chirpattendance.fragments;

import android.animation.Animator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.chirpattendance.R;
import com.example.chirpattendance.activities.MeetingActivity;
import com.example.chirpattendance.activities.RoomActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.nio.charset.Charset;
import io.chirp.chirpsdk.ChirpSDK;
import io.chirp.chirpsdk.models.ChirpError;

public class FragmentSendKey extends Fragment {

    String CHIRP_APP_KEY = "c345Ab86Ad8F2cB9Cb368359F";
    String CHIRP_APP_SECRET = "3fC73BD19a2eC7a4adDb2CcAef59eBA7BB4fdEaECcc668da51";
    String CHIRP_APP_CONFIG = "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";
    private String hashedKey;
    private ChirpSDK chirp;
    private TextView back;
    private ChirpError error;
    private ImageView microphone;
    private LottieAnimationView animationView;
    private SharedPreferences sharedPreferences;
    private TextView bypassKey;

    public FragmentSendKey() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_room, container, false);
        initializeViews(rootView);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MeetingActivity.getInterfaceMeetingActivity().backPresssed();
            }
        });

        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSending(hashedKey);
               /* animationView.setSpeed(2);
                animationView.setVisibility(View.VISIBLE);
                animationView.playAnimation();
                animationView.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        animationView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });*/
            }
        });

        return rootView;

    }

    private void initializeViews(View rootView) {
        sharedPreferences =  getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        hashedKey = sharedPreferences.getString("MeetingHashedKey", null);
        chirp = new ChirpSDK(getActivity(), CHIRP_APP_KEY, CHIRP_APP_SECRET);
        error = chirp.setConfig(CHIRP_APP_CONFIG);
        error = chirp.start(true, false);
        microphone = rootView.findViewById(R.id.microphone_send_key_fragment);
        // animationView = rootView.findViewById(R.id.send_room_key_animation_view);
        back = rootView.findViewById(R.id.back);
        bypassKey = rootView.findViewById(R.id.bypass_key_text_view);
        bypassKey.setText(RoomActivity.getHashedKey());
    }

    private void stopSending() {
        chirp.stop();
        try {
            chirp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSending(String hashedKey) {
        String identifier = hashedKey;
        byte[] payload = identifier.getBytes(Charset.forName("UTF-8"));
        error = chirp.send(payload);
        if (error.getCode() > 0) {
            Log.e("ChirpError: ", error.getMessage());
        } else {
            Log.v("ChirpSDK: ", "Sent " + identifier);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopSending();
    }

}
