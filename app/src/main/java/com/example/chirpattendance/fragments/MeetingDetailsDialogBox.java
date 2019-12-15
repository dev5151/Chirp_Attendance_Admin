package com.example.chirpattendance.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.chirpattendance.R;
import com.example.chirpattendance.activities.RoomActivity;
import com.example.chirpattendance.models.Admin;
import com.example.chirpattendance.models.UserRoom;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MeetingDetailsDialogBox extends DialogFragment {

    private EditText agenda;
    private EditText location;
    private EditText duration;
    private DatabaseReference reference;
    private String Id;
    private Button createButton;
    private SharedPreferences sharedPreferences;
    private String uid;
    private FirebaseAuth auth;

    public MeetingDetailsDialogBox() {
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if(window == null) return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View layout = getActivity().getLayoutInflater().inflate(R.layout.meeting_details_dialog_box, null, false);
        initializeViews(layout);
        assert layout != null;
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setView(layout);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoom();
            }
        });

        return b.create();
    }


    private void initializeViews(View rootView) {
        String key = RoomActivity.getOrganizationKey();
        agenda = rootView.findViewById(R.id.agenda_edit_text_bottom_sheet);
        location = rootView.findViewById(R.id.room_location_edit_text_bottom_sheet);
        duration = rootView.findViewById(R.id.meeting_duration_edit_text_bottom_sheet);
        reference = FirebaseDatabase.getInstance().getReference();
        createButton = rootView.findViewById(R.id.create_room_button);
        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", null);
        auth = FirebaseAuth.getInstance();
    }

    private void createRoom() {
        if(verifyFields())
        {
            pushRoomKey();
            createUserRoom(location.getText().toString(), agenda.getText().toString(), (int)Double.parseDouble(duration.getText().toString()));
            agenda.setText("");
            location.setText("");
            duration.setText("");
            RoomActivity.getChirpAttendance().dismissMeetingDialogBox();
        }
        else
        {
            makeToast(createButton, "Enter all fields");
        }
    }

    private void createUserRoom(String location, String agenda, int time) {
        String startingUnixTime = String.valueOf(System.currentTimeMillis() / 1000L);
        String endingUnixtime =   String.valueOf((System.currentTimeMillis() / 1000l)+(time*60));
        reference.child("rooms").child(Id).setValue(new UserRoom(location, agenda, startingUnixTime, endingUnixtime, Id));
    }

    private void pushRoomKey() {
        final String pushId = reference.push().getKey();
        assert pushId!= null;
        Id = pushId;
        Id = String.valueOf(generateHash(Id)).substring(0, 5);

        reference.child("organization").child(RoomActivity.getOrganizationKey()).child("admin").child(uid).child("rooms")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        while(dataSnapshot.child(Id).exists()) {
                            Id = String.valueOf(generateHash(Id)).substring(0, 5);
                        }

                        reference.child("organization").child(RoomActivity.getOrganizationKey()).child("admin").child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.child("email").exists())
                                {
                                    reference.child("organization").child(RoomActivity.getOrganizationKey()).child("admin").child(uid).setValue
                                            (new Admin(auth.getCurrentUser().getEmail(), auth.getCurrentUser().getDisplayName()));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        reference.child("organization").child(RoomActivity.getOrganizationKey()).child("admin").child(uid).child("rooms")
                                .push().setValue(Id);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    String generateHash(String text){
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(text.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return  generatedPassword;
    }


    private boolean verifyFields() {
        if(agenda.getText()!=null && agenda.getText().length()>0
            && location.getText()!=null && location.getText().length()>0
            && duration.getText()!=null && duration.getText().length()>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void makeToast(View view, String message)
    {
         Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
         toast.setGravity(Gravity.TOP, 0, 0);
         toast.show();

    }


}
