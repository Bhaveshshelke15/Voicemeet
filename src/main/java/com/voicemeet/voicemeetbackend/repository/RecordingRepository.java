package com.voicemeet.voicemeetbackend.repository;

import com.voicemeet.voicemeetbackend.entity.MeetingRecording;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List; // 🔥 ADD THIS IMPORT

public interface RecordingRepository
        extends JpaRepository<MeetingRecording, Long> {

    // 🔥 ADD THIS METHOD (for user-specific recordings)
    List<MeetingRecording> findByUserId(String userId);

}