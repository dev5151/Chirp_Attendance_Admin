package com.example.chirpattendance.interfaces;

 public interface ChirpAttendance {
     void goToSendKey(String hashed_key);
     void showBottomSheet();
     void hideBottomSheet();
     void hideBottomNavigationView();
     void showBottomNavigationView();
     void topBarSetText(String text);
     void goToPreviousMeetingActivity(String hashedKey);
     void setBottomNavigationBarItem(int itemId);
}
