package com.example.chirpattendance.interfaces;

 public interface ChirpAttendance {
     void showMeetingDialogBox();
     void goToMeeting(int i, String hashedKey);
     void setOrganiztionName(String organiztionName);
     void setAdminUid(String adminUid);
     void dismissMeetingDialogBox();
     void logout();
     void openBottomSheet();
     void closeBottomSheet();
}
