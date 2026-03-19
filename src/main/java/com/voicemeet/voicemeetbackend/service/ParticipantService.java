package com.voicemeet.voicemeetbackend.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ParticipantService {

    private final Map<String, Set<String>> meetingParticipants = new HashMap<>();

    public void joinMeeting(String meetingId, String username) {

        meetingParticipants
                .computeIfAbsent(meetingId, k -> new HashSet<>())
                .add(username);
    }

    public void leaveMeeting(String meetingId, String username) {

        if (meetingParticipants.containsKey(meetingId)) {

            meetingParticipants.get(meetingId).remove(username);

            if (meetingParticipants.get(meetingId).isEmpty()) {
                meetingParticipants.remove(meetingId);
            }
        }
    }

    public Set<String> getParticipants(String meetingId) {

        return meetingParticipants.getOrDefault(meetingId, new HashSet<>());
    }
}