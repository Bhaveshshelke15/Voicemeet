package com.voicemeet.voicemeetbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MeetingUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long meetingId;

    private String userId;
}