package com.voicemeet.voicemeetbackend.repository;


import com.voicemeet.voicemeetbackend.entity.MeetingRecording;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordingRepository
        extends JpaRepository<MeetingRecording, Long> {
}