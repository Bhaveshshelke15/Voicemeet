package com.voicemeet.voicemeetbackend.controller;





import com.voicemeet.voicemeetbackend.entity.MeetingRecording;
import com.voicemeet.voicemeetbackend.repository.RecordingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/recording")
@CrossOrigin("*")
public class RecordingController {

    @Autowired
    private RecordingRepository repository;



    @PostMapping("/upload")
    public String uploadRecording(
            @RequestParam("file") MultipartFile file,
            @RequestParam("meetingId") String meetingId
    ) {

        try {

            String folder = "C:/recordings/";

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

    @GetMapping("/all")
    public List<MeetingRecording> getAll(){

        return repository.findAll();

    }
}