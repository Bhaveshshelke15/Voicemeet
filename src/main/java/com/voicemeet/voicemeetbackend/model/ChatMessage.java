package com.voicemeet.voicemeetbackend.model;

import java.util.List;

public class ChatMessage {

    private String sender;
    private String receiver;   // for private chat
    private String meetingId;

    private String groupId;    // for group chat
    private List<String> groupMembers;

    private String message;
    private String time;

    public ChatMessage(){}

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }

    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public List<String> getGroupMembers() { return groupMembers; }
    public void setGroupMembers(List<String> groupMembers) { this.groupMembers = groupMembers; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}