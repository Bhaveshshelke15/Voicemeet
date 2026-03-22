package com.voicemeet.voicemeetbackend.controller;

import com.voicemeet.voicemeetbackend.entity.Meeting;
import com.voicemeet.voicemeetbackend.entity.MeetingParticipant;
import com.voicemeet.voicemeetbackend.repository.MeetingParticipantRepository;
import com.voicemeet.voicemeetbackend.repository.MeetingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/meeting")
@CrossOrigin(origins = "*")
public class MeetingInviteController {

    @Autowired
    private MeetingParticipantRepository participantRepo;

    @Autowired
    private MeetingRepository meetingRepo;

    // ✅ Invite user
    @PostMapping("/invite")
    public MeetingParticipant inviteUser(@RequestBody MeetingParticipant mp){

        if(participantRepo.existsByMeetingIdAndUserId(mp.getMeetingId(), mp.getUserId())){
            throw new RuntimeException("User already invited");
        }

        mp.setStatus("INVITED");

        return participantRepo.save(mp);
    }

    // ✅ Get meetings for user
    @GetMapping("/user/{userId}")
    public List<Map<String, String>> getUserMeetings(@PathVariable String userId){

        List<MeetingParticipant> participants = participantRepo.findByUserId(userId);

        List<Map<String, String>> response = new ArrayList<>();

        for(MeetingParticipant p : participants){

            Optional<Meeting> meetingOpt = meetingRepo.findById(p.getMeetingId());

            if(meetingOpt.isPresent()){
                Meeting m = meetingOpt.get();

                Map<String, String> data = new HashMap<>();
                data.put("meetingId", m.getMeetingId());
                data.put("meetingName", m.getMeetingName());
                data.put("status", p.getStatus());

                response.add(data);
            }
        }

        return response;
    }

    // 🔥🔥 ADD THIS METHOD
    @GetMapping("/all")
    public List<Meeting> getAllMeetings() {
        return meetingRepo.findAll();
    }
}