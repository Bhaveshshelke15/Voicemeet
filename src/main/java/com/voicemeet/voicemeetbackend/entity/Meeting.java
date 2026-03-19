package com.voicemeet.voicemeetbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Meeting {

    @Id
    private String meetingId;

    private String meetingName;

    private String createdBy;

    private Boolean active = true;   // ⭐ ADD THIS
}