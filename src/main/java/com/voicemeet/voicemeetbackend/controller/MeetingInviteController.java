package com.voicemeet.voicemeetbackend.controller;

import com.voicemeet.voicemeetbackend.entity.MeetingParticipant;
import com.voicemeet.voicemeetbackend.repository.MeetingParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meeting")
@CrossOrigin
public class MeetingInviteController {

    @Autowired
    private MeetingParticipantRepository repo;

    // Invite user
    @PostMapping("/invite")
    public MeetingParticipant inviteUser(@RequestBody MeetingParticipant mp){
        return repo.save(mp);
    }

    // Get meetings for user
    @GetMapping("/user/{userId}")
    public List<MeetingParticipant> getUserMeetings(@PathVariable String userId){
        return repo.findByUserId(userId);
    }
}