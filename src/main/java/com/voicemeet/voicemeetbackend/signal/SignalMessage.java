package com.voicemeet.voicemeetbackend.signal;

public class SignalMessage {

    private String type;
    private String meetingId;
    private Object offer;
    private Object answer;
    private Object candidate;
    private String sender;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }

    public Object getOffer() { return offer; }
    public void setOffer(Object offer) { this.offer = offer; }

    public Object getAnswer() { return answer; }
    public void setAnswer(Object answer) { this.answer = answer; }

    public Object getCandidate() { return candidate; }
    public void setCandidate(Object candidate) { this.candidate = candidate; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

}