package com.example.chirpattendance.models;

public class UserRoom {
    private String location;
    private String agenda;
    private String startingUnixTime;
    private String endingUnixTime;
    private String hashedKey;

    public UserRoom() {
    }

    public UserRoom(String location, String agenda, String startingUnixTime, String endingUnixTime, String hashedKey) {
        this.location = location;
        this.agenda = agenda;
        this.startingUnixTime = startingUnixTime;
        this.endingUnixTime = endingUnixTime;
        this.hashedKey = hashedKey;
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

    public String getStartingUnixTime() {
        return startingUnixTime;
    }

    public void setStartingUnixTime(String startingUnixTime) {
        this.startingUnixTime = startingUnixTime;
    }

    public String getEndingUnixTime() {
        return endingUnixTime;
    }

    public void setEndingUnixTime(String endingUnixTime) {
        this.endingUnixTime = endingUnixTime;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }
}
