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

    // ✅ FIXED STORAGE (NOT temp)
    private final String UPLOAD_DIR = "recordings/";

    ////////////////////////////////////////////////////////
    // ✅ UPLOAD RECORDING
    ////////////////////////////////////////////////////////

    @PostMapping("/upload")
    public String uploadRecording(
            @RequestParam("file") MultipartFile file,
            @RequestParam("meetingId") String meetingId
    ) {

        try {

            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            String fileName = meetingId + "_" + System.currentTimeMillis() + ".webm";

            File savedFile = new File(UPLOAD_DIR + fileName);
            file.transferTo(savedFile);

            // ✅ GET MEETING NAME
            Meeting meeting = meetingRepository.findById(meetingId).orElse(null);
            String meetingName = (meeting != null) ? meeting.getMeetingName() : "Unknown";

            // ✅ GET PARTICIPANTS
            Set<String> participantsSet = participantService.getParticipants(meetingId);
            String participants = String.join(",", participantsSet);

            // ✅ SAVE DATA
            MeetingRecording rec = new MeetingRecording();
            rec.setMeetingId(meetingId);
            rec.setMeetingName(meetingName);
            rec.setParticipants(participants);
            rec.setFileName(fileName);
            rec.setDate(java.time.LocalDate.now().toString());
            rec.setTime(java.time.LocalTime.now().toString());

            repository.save(rec);

            System.out.println("Saved at: " + savedFile.getAbsolutePath());

            return "Saved Successfully";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    ////////////////////////////////////////////////////////
    // ✅ GET ALL RECORDINGS
    ////////////////////////////////////////////////////////

    @GetMapping("/all")
    public List<MeetingRecording> getAll() {
        return repository.findAll();
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