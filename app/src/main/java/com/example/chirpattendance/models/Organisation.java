package com.example.chirpattendance.models;
import java.util.HashMap;

public class Organisation {

    String authKey;
    String authPassword;
    HashMap<String, String> rooms = new HashMap<>();

    public Organisation() {
    }

    public Organisation(String authKey, String authPassword) {
        this.authKey = authKey;
        this.authPassword = authPassword;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getAuthPassword() {
        return authPassword;
    }

    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    public HashMap<String, String> getRooms() {
        return rooms;
    }

    public void setRooms(HashMap<String, String> rooms) {
        this.rooms = rooms;
    }
}
