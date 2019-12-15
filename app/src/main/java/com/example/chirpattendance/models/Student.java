package com.example.chirpattendance.models;

public class Student {

    String uid;
    String studentKey;

    public Student(String uid, String studentKey) {
        this.uid = uid;
        this.studentKey = studentKey;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStudentKey() {
        return studentKey;
    }

    public void setStudentKey(String studentKey) {
        this.studentKey = studentKey;
    }
}
