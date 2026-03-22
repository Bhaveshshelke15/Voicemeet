package com.voicemeet.voicemeetbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MeetingParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String meetingId;   // ✅ UUID (String)
    private String userId;

    private String status;      // INVITED, JOINED
}