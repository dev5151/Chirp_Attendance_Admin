package com.example.chirpattendance.models;

public class AttendeesDefaultersList {

    private String name;
    private String email;
    private String uniqueId;

    public AttendeesDefaultersList() {
    }

    public AttendeesDefaultersList(String name, String email, String uniqueId) {
        this.name = name;
        this.email = email;
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
