package com.voicemeet.voicemeetbackend.controller;

import com.voicemeet.voicemeetbackend.entity.Meeting;
import com.voicemeet.voicemeetbackend.entity.MeetingRecording;
import com.voicemeet.voicemeetbackend.repository.MeetingRepository;
import com.voicemeet.voicemeetbackend.repository.RecordingRepository;
import com.voicemeet.voicemeetbackend.service.ParticipantService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/recording")
@CrossOrigin("*")
public class RecordingController {

    @Autowired
    private RecordingRepository repository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private Cloudinary cloudinary;

    ////////////////////////////////////////////////////////
    // ✅ UPLOAD RECORDING (CLOUDINARY VERSION)
    ////////////////////////////////////////////////////////

    @PostMapping("/upload")
    public ResponseEntity<?> uploadRecording(
            @RequestParam("file") MultipartFile file,
            @RequestParam("meetingId") String meetingId,
            @RequestParam(value = "userId", required = false) String userId // 🔥 ADDED
    ) {

        try {
            System.out.println("🔥 Upload API HIT");
            System.out.println("Meeting ID: " + meetingId);
            System.out.println("User ID: " + userId); // 🔥 ADDED

            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            if (meetingId == null || meetingId.isEmpty()) {
                return ResponseEntity.badRequest().body("Meeting ID missing");
            }

            var uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "folder", "voicemeet_recordings"
                    )
            );

            String fileUrl = uploadResult.get("secure_url").toString();

            System.out.println("☁ Uploaded to Cloudinary: " + fileUrl);

            Meeting meeting = meetingRepository.findById(meetingId).orElse(null);
            String meetingName = (meeting != null) ? meeting.getMeetingName() : "Unknown";

            String participants = "";

            try {
                Set<String> participantsSet = participantService.getParticipants(meetingId);

                if (participantsSet != null && !participantsSet.isEmpty()) {
                    participants = String.join(",", participantsSet);
                } else {
                    participants = "admin";
                }

            } catch (Exception e) {
                System.out.println("⚠ Participant fetch failed: " + e.getMessage());
                participants = "admin";
            }

            //////////////////////////////////////////////////////
            // ✅ SAVE TO DB
            //////////////////////////////////////////////////////
            MeetingRecording rec = new MeetingRecording();
            rec.setMeetingId(meetingId);
            rec.setMeetingName(meetingName);
            rec.setParticipants(participants);
            rec.setFileUrl(fileUrl);
            rec.setDate(java.time.LocalDate.now().toString());
            rec.setTime(java.time.LocalTime.now().toString());

            rec.setUserId(userId); // 🔥 ADDED (IMPORTANT)

            repository.save(rec);

            System.out.println("✅ Saved in DB");

            return ResponseEntity.ok(fileUrl);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity
                    .status(500)
                    .body("ERROR: " + e.getMessage());
        }
    }

    ////////////////////////////////////////////////////////
    // ✅ GET ALL RECORDINGS (UNCHANGED)
    ////////////////////////////////////////////////////////

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(repository.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching recordings");
        }
    }

    ////////////////////////////////////////////////////////
    // ✅ GET USER RECORDINGS (NEW API)
    ////////////////////////////////////////////////////////

    @GetMapping("/user/{userId}") // 🔥 ADDED
    public ResponseEntity<?> getUserRecordings(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(repository.findByUserId(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching user recordings");
        }
    }



}