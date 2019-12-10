package com.example.chirpattendance.interfaces;

 public interface ChirpAttendance {
     void goToSendKey(String hashed_key);
     void showBottomSheet();
     void hideBottomSheet();
     void goToPreviousMeetingActivity(String hashedKey);
}
