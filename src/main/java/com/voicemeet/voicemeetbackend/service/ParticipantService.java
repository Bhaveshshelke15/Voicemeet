package com.voicemeet.voicemeetbackend.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ParticipantService {

    private final Map<String, Set<String>> meetingParticipants = new HashMap<>();

    // ✅ JOIN
    public synchronized void joinMeeting(String meetingId, String username) {

        meetingParticipants
                .computeIfAbsent(meetingId, k -> new HashSet<>())
                .add(username);

        System.out.println("👤 Joined: " + username + " -> " + meetingParticipants.get(meetingId));
    }

    // ✅ LEAVE
    public synchronized void leaveMeeting(String meetingId, String username) {

        if (meetingParticipants.containsKey(meetingId)) {

            meetingParticipants.get(meetingId).remove(username);

            if (meetingParticipants.get(meetingId).isEmpty()) {
                meetingParticipants.remove(meetingId);
            }
        }

        System.out.println("👋 Left: " + username);
    }

    // ✅ GET PARTICIPANTS
    public synchronized Set<String> getParticipants(String meetingId) {

        Set<String> participants = meetingParticipants.get(meetingId);

        if (participants == null || participants.isEmpty()) {
            return new HashSet<>(Collections.singleton("admin"));
        }

        return participants;
    }
}