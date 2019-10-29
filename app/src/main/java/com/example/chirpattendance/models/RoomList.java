package com.example.chirpattendance.models;

import java.util.ArrayList;

public class RoomList {

    private String location;
    private String agenda;
    private String hashedKey;
    private String endingUnixTime;
    private String startingUnixTime;


    public RoomList() {
    }

    public RoomList(String location, String agenda, String hashedKey, String endingUnixTime, String startingUnixTime) {
        this.location = location;
        this.agenda = agenda;
        this.hashedKey = hashedKey;
        this.endingUnixTime = endingUnixTime;
        this.startingUnixTime = startingUnixTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }

    public String getEndingUnixTime() {
        return endingUnixTime;
    }

    public void setEndingUnixTime(String endingUnixTime) {
        this.endingUnixTime = endingUnixTime;
    }

    public String getStartingUnixTime() {
        return startingUnixTime;
    }

    public void setStartingUnixTime(String startingUnixTime) {
        this.startingUnixTime = startingUnixTime;
    }
}
