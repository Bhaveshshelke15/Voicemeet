package com.voicemeet.voicemeetbackend.controller;

import com.voicemeet.voicemeetbackend.service.ParticipantService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/meeting")
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping("/{meetingId}/participants")
    public Set<String> getParticipants(@PathVariable String meetingId) {

        return participantService.getParticipants(meetingId);
    }
}