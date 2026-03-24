package com.voicemeet.voicemeetbackend.controller;

import com.voicemeet.voicemeetbackend.entity.MeetingRecording;
import com.voicemeet.voicemeetbackend.repository.RecordingRepository;

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

@RestController
@RequestMapping("/recording")
@CrossOrigin("*")
public class RecordingController {

    @Autowired
    private RecordingRepository repository;

    // ✅ FIXED: Use stable path
    private final String UPLOAD_DIR = System.getProperty("java.io.tmpdir") + "/recordings/";

    ////////////////////////////////////////////////////////
    // ✅ UPLOAD
    ////////////////////////////////////////////////////////

    @PostMapping("/upload")
    public String uploadRecording(
            @RequestParam("file") MultipartFile file,
            @RequestParam("meetingId") String meetingId
    ) {

        try {

            File dir = new File(UPLOAD_DIR);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = meetingId + "_" + System.currentTimeMillis() + ".webm";

            File savedFile = new File(UPLOAD_DIR + fileName);

            file.transferTo(savedFile);

            // ✅ Save metadata
            MeetingRecording rec = new MeetingRecording();
            rec.setMeetingId(meetingId);
            rec.setFileName(fileName);
            rec.setDate(java.time.LocalDate.now().toString());
            rec.setTime(java.time.LocalTime.now().toString());

            repository.save(rec);

            System.out.println("✅ File saved at: " + savedFile.getAbsolutePath());

            return "Recording saved successfully";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error saving recording";
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
    // ✅ SERVE RECORDING FILE
    ////////////////////////////////////////////////////////

    @GetMapping("/recordings/{fileName}")
    public ResponseEntity<Resource> getRecording(@PathVariable String fileName) {

        try {

            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();

            File file = filePath.toFile();

            System.out.println("📂 Looking for: " + file.getAbsolutePath());

            if (!file.exists()) {
                System.out.println("❌ File NOT found");
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(file.toURI());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "audio/webm")
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}