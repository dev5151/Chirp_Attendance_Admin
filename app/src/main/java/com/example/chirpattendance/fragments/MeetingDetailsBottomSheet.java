package com.example.chirpattendance.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.chirpattendance.R;
import com.example.chirpattendance.activities.MainActivity;
import com.example.chirpattendance.models.UserRoom;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MeetingDetailsBottomSheet extends BottomSheetDialogFragment {

    private ImageView proceed;
    private EditText agenda;
    private EditText location;
    private EditText duration;
    private DatabaseReference reference;
    private String Id;

    public MeetingDetailsBottomSheet() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.bottom_sheet, container, false);

        initializeViews(rootView);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoom();
            }
        });

        return rootView;
    }

    private void initializeViews(View rootView) {
        proceed = rootView.findViewById(R.id.bottom_sheet_proceed_button);
        agenda = rootView.findViewById(R.id.agenda_edit_text_bottom_sheet);
        location = rootView.findViewById(R.id.room_location_edit_text_bottom_sheet);
        duration = rootView.findViewById(R.id.meeting_duration_edit_text_bottom_sheet);
        reference = FirebaseDatabase.getInstance().getReference();
    }

    private void createRoom() {
        if(verifyFields())
        {
            pushRoomKey();
            createUserRoom(location.getText().toString(), agenda.getText().toString(), Integer.parseInt(duration.getText().toString()));
            MeetingDetailsBottomSheet bottomSheet = new MeetingDetailsBottomSheet();
            bottomSheet.dismiss();
            MainActivity.getChirpAttendance().goToSendKey(Id);
        }
        else
        {
            makeToast(proceed, "Enter All Fields");
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
        reference.child("Admin").child(MainActivity.getOrganizationKey()).child("rooms")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        while(dataSnapshot.child(Id).exists()) {
                            Id = String.valueOf(generateHash(Id)).substring(0, 5);
                        }
                        reference.child("Admin").child(MainActivity.getOrganizationKey()).child("rooms")
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
