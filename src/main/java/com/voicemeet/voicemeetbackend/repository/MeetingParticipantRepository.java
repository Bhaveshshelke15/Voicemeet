package com.voicemeet.voicemeetbackend.repository;

import com.voicemeet.voicemeetbackend.entity.MeetingParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingParticipantRepository extends JpaRepository<MeetingParticipant, Long> {

    List<MeetingParticipant> findByUserId(String userId);

    boolean existsByMeetingIdAndUserId(String meetingId, String userId);
}