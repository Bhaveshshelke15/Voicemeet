package com.voicemeet.voicemeetbackend.repository;

import com.voicemeet.voicemeetbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT user_id FROM users ORDER BY user_id DESC LIMIT 1", nativeQuery = true)
    String findLastUserId();

    Optional<User> findByUserId(String userId);
    List<User> findByNameContainingIgnoreCase(String name);

}








