package com.voicemeet.voicemeetbackend.signal;

import com.voicemeet.voicemeetbackend.service.ParticipantService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class SignalingController {

    private static final Logger logger =
            LoggerFactory.getLogger(SignalingController.class);

    private final SimpMessagingTemplate template;
    private final ParticipantService participantService;

    // ✅ Constructor Injection (BEST PRACTICE)
    public SignalingController(SimpMessagingTemplate template,
                               ParticipantService participantService) {
        this.template = template;
        this.participantService = participantService;
    }

    ////////////////////////////////////////////////////////
    // ✅ HANDLE SIGNAL
    ////////////////////////////////////////////////////////

    @MessageMapping("/signal")
    public void signal(SignalMessage message){

        try {

            String type = message.getType();
            String meetingId = message.getMeetingId();
            String sender = message.getSender();

            logger.info("📩 SIGNAL: {}", message);

            ////////////////////////////////////////////////////////
            // ✅ JOIN
            ////////////////////////////////////////////////////////
            if ("join".equals(type)) {
                participantService.joinMeeting(meetingId, sender);
                logger.info("👤 JOINED: {}", sender);
            }

            ////////////////////////////////////////////////////////
            // ✅ LEAVE
            ////////////////////////////////////////////////////////
            if ("leave".equals(type)) {
                participantService.leaveMeeting(meetingId, sender);
                logger.info("👋 LEFT: {}", sender);
            }

            ////////////////////////////////////////////////////////
            // ✅ FORWARD MESSAGE (UNCHANGED LOGIC)
            ////////////////////////////////////////////////////////
            template.convertAndSend(
                    "/topic/signal/" + meetingId,
                    message
            );

        } catch (Exception e) {
            logger.error("❌ Error in signaling", e);
        }
    }
}