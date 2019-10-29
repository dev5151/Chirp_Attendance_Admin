package com.example.chirpattendance.interfaces;


 public interface ChirpAttendance {
     void goToSendKey(String hashed_key);
     void saveKeyPassword(String key, String password);
     void goToRooms();
}
