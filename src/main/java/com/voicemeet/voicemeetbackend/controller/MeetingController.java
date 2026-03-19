package com.voicemeet.voicemeetbackend.controller;

import com.voicemeet.voicemeetbackend.entity.Meeting;
import com.voicemeet.voicemeetbackend.entity.MeetingParticipant;
import com.voicemeet.voicemeetbackend.repository.MeetingParticipantRepository;
import com.voicemeet.voicemeetbackend.repository.MeetingRepository;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/meeting")
@CrossOrigin(origins = "http://localhost:3000")
public class MeetingController {

    private final MeetingRepository meetingRepository;
    private final MeetingParticipantRepository participantRepository;

    public MeetingController(MeetingRepository meetingRepository,
                             MeetingParticipantRepository participantRepository) {

        this.meetingRepository = meetingRepository;
        this.participantRepository = participantRepository;
    }

    // 1️⃣ ADMIN CREATE MEETING
    @PostMapping("/create")
    public Meeting createMeeting(@RequestParam String meetingName,
                                 @RequestParam String adminUsername){

        Meeting meeting = new Meeting();

        meeting.setMeetingId(UUID.randomUUID().toString());
        meeting.setMeetingName(meetingName);
        meeting.setCreatedBy(adminUsername);

        return meetingRepository.save(meeting);
    }


    // 2️⃣ ADMIN ADD USER TO MEETING
    @PostMapping("/addUser")
    public MeetingParticipant addUserToMeeting(@RequestParam String meetingId,
                                               @RequestParam String userId){

        MeetingParticipant participant = new MeetingParticipant();

        participant.setMeetingId(meetingId);
        participant.setUserId(userId);
        participant.setStatus("INVITED");

        return participantRepository.save(participant);
    }


    // 3️⃣ USER SEE INVITED MEETINGS
    @GetMapping("/userMeetings")
    public List<MeetingParticipant> getUserMeetings(@RequestParam String userId){

        return participantRepository.findByUserId(userId);

    }




}