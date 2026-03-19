package com.voicemeet.voicemeetbackend.signal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SignalingController {

    private final SimpMessagingTemplate template;

    public SignalingController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/signal")
    public void signal(SignalMessage message){

        template.convertAndSend(
                "/topic/signal/" + message.getMeetingId(),
                message
        );
    }

}