package com.voicemeet.voicemeetbackend.controller;

import com.voicemeet.voicemeetbackend.entity.User;
import com.voicemeet.voicemeetbackend.entity.Meeting;
import com.voicemeet.voicemeetbackend.repository.UserRepository;
import com.voicemeet.voicemeetbackend.repository.MeetingRepository;
import com.voicemeet.voicemeetbackend.dto.UserDTO;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;

    public AdminController(UserRepository userRepository,
                           MeetingRepository meetingRepository) {

        this.userRepository = userRepository;
        this.meetingRepository = meetingRepository;
    }

    //////////////////////////////////////////////////////
    // CREATE USER
    //////////////////////////////////////////////////////

    @PostMapping("/create-user")
    public User createUser(@RequestBody UserDTO dto) {

        String lastUserId = userRepository.findLastUserId();

        String newUserId;

        if (lastUserId == null) {
            newUserId = "U001";
        } else {

            int num = Integer.parseInt(lastUserId.substring(1));
            num++;

            newUserId = String.format("U%03d", num);
        }

        User user = new User();
        user.setUserId(newUserId);
        user.setName(dto.getName());
        user.setPassword(dto.getPassword());

        return userRepository.save(user);
    }

    //////////////////////////////////////////////////////
    // GET ALL USERS (EMPLOYEES)
    //////////////////////////////////////////////////////

    @GetMapping("/users")
    public List<User> getAllUsers() {

        return userRepository.findAll();

    }

    //////////////////////////////////////////////////////
    // GET ALL MEETINGS
    //////////////////////////////////////////////////////

    @GetMapping("/meetings")
    public List<Meeting> getAllMeetings(){

        return meetingRepository.findAll();

    }

    //////////////////////////////////////////////////////
    // TOTAL EMPLOYEES COUNT
    //////////////////////////////////////////////////////

    @GetMapping("/total-employees")
    public long totalEmployees(){

        return userRepository.count();

    }

    //////////////////////////////////////////////////////
    // TOTAL MEETINGS COUNT
    //////////////////////////////////////////////////////

    @GetMapping("/total-meetings")
    public long totalMeetings(){

        return meetingRepository.count();

    }



    @GetMapping("/search-user")
    public List<User> searchUser(@RequestParam String keyword){

        return userRepository.findByNameContainingIgnoreCase(keyword);

    }
}