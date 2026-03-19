package com.voicemeet.voicemeetbackend.repository;

import com.voicemeet.voicemeetbackend.entity.MeetingUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingUserRepository extends JpaRepository<MeetingUser,Long> {

    List<MeetingUser> findByUserId(String userId);
}