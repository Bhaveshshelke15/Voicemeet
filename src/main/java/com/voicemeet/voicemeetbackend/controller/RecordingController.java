package com.voicemeet.voicemeetbackend.controller;

import com.voicemeet.voicemeetbackend.entity.Meeting;
import com.voicemeet.voicemeetbackend.entity.MeetingRecording;
import com.voicemeet.voicemeetbackend.repository.MeetingRepository;
import com.voicemeet.voicemeetbackend.repository.RecordingRepository;
import com.voicemeet.voicemeetbackend.service.ParticipantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private final String UPLOAD_DIR = "recordings/";

    ////////////////////////////////////////////////////////
    // ✅ UPLOAD RECORDING (SAFE VERSION)
    ////////////////////////////////////////////////////////

    @PostMapping("/upload")
    public ResponseEntity<?> uploadRecording(
            @RequestParam("file") MultipartFile file,
            @RequestParam("meetingId") String meetingId
    ) {

        try {
            System.out.println("🔥 Upload API HIT");
            System.out.println("Meeting ID: " + meetingId);

            // ✅ VALIDATION
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            if (meetingId == null || meetingId.isEmpty()) {
                return ResponseEntity.badRequest().body("Meeting ID missing");
            }

            // ✅ CREATE DIRECTORY
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                System.out.println("Directory created: " + created);
            }

            // ✅ FILE NAME
            String fileName = meetingId + "_" + System.currentTimeMillis() + ".webm";

            File savedFile = new File(UPLOAD_DIR + fileName);
            file.transferTo(savedFile);

            System.out.println("📁 File saved at: " + savedFile.getAbsolutePath());

            // ✅ GET MEETING
            Meeting meeting = meetingRepository.findById(meetingId).orElse(null);
            String meetingName = (meeting != null) ? meeting.getMeetingName() : "Unknown";

            // ✅ SAFE PARTICIPANTS
            String participants = "";

            try {
                Set<String> participantsSet = participantService.getParticipants(meetingId);

                if (participantsSet != null && !participantsSet.isEmpty()) {
                    participants = String.join(",", participantsSet);
                } else {
                    participants = "admin"; // fallback
                }

            } catch (Exception e) {
                System.out.println("⚠ Participant fetch failed: " + e.getMessage());
                participants = "admin";
            }

            // ✅ SAVE TO DB
            MeetingRecording rec = new MeetingRecording();
            rec.setMeetingId(meetingId);
            rec.setMeetingName(meetingName);
            rec.setParticipants(participants);
            rec.setFileName(fileName);
            rec.setDate(java.time.LocalDate.now().toString());
            rec.setTime(java.time.LocalTime.now().toString());

            repository.save(rec);

            System.out.println("✅ Saved in DB");

            return ResponseEntity.ok("Saved Successfully");

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity
                    .status(500)
                    .body("ERROR: " + e.getMessage()); // 🔥 REAL ERROR
        }
    }

    ////////////////////////////////////////////////////////
    // ✅ GET ALL RECORDINGS
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
    // ✅ SERVE AUDIO FILE
    ////////////////////////////////////////////////////////

    @GetMapping("/recordings/{fileName}")
    public ResponseEntity<Resource> getRecording(@PathVariable String fileName) {

        try {
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            File file = filePath.toFile();

            System.out.println("Looking file: " + file.getAbsolutePath());

            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(file.toURI());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "audio/webm")
                    .header("Access-Control-Allow-Origin", "*")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}