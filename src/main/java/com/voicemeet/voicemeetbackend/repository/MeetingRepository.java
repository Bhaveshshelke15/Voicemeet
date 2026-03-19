package com.voicemeet.voicemeetbackend.repository;

import com.voicemeet.voicemeetbackend.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MeetingRepository extends JpaRepository<Meeting,String> {
}