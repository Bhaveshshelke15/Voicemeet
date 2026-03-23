package com.voicemeet.voicemeetbackend.controller;

import com.voicemeet.voicemeetbackend.entity.MeetingRecording;
import com.voicemeet.voicemeetbackend.repository.RecordingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// ✅ NEW IMPORTS
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/recording")
@CrossOrigin("*")
public class RecordingController {

    @Autowired
    private RecordingRepository repository;

    ////////////////////////////////////////////////////////
    // UPLOAD (UNCHANGED)
    ////////////////////////////////////////////////////////

    @PostMapping("/upload")
    public String uploadRecording(
            @RequestParam("file") MultipartFile file,
            @RequestParam("meetingId") String meetingId
    ) {

        try {

            String folder = System.getProperty("user.dir") + "/recordings/";

            File dir = new File(folder);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = meetingId + "_" + System.currentTimeMillis() + ".webm";

            File savedFile = new File(folder + fileName);

            file.transferTo(savedFile);

            MeetingRecording rec = new MeetingRecording();

            rec.setMeetingId(meetingId);
            rec.setFileName(fileName);
            rec.setDate(java.time.LocalDate.now().toString());
            rec.setTime(java.time.LocalTime.now().toString());

            repository.save(rec);

            return "Recording saved";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error saving recording";
        }
    }

    ////////////////////////////////////////////////////////
    // GET ALL (UNCHANGED)
    ////////////////////////////////////////////////////////

    @GetMapping("/all")
    public List<MeetingRecording> getAll(){
        return repository.findAll();
    }

    ////////////////////////////////////////////////////////
    // 🔥 NEW: SERVE RECORDING FILE
    ////////////////////////////////////////////////////////

    @GetMapping("/recordings/{fileName}")
    public ResponseEntity<Resource> getRecording(@PathVariable String fileName) {

        try {

            String folder = System.getProperty("user.dir") + "/recordings/";
            File file = new File(folder + fileName);

            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(file.toURI());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + fileName + "\"")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}