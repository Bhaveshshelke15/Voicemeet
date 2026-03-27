package com.voicemeet.voicemeetbackend.entity;

import jakarta.persistence.*;

@Entity
public class MeetingRecording {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String meetingId;

    // ✅ NEW FIELD
    private String meetingName;

    // ✅ NEW FIELD (comma separated users)
    private String participants;

    private String fileName;
    private String date;
    private String time;

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}