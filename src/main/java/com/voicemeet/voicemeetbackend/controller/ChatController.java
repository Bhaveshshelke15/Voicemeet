package com.voicemeet.voicemeetbackend.controller;

import com.voicemeet.voicemeetbackend.entity.ChatMessageEntity;
import com.voicemeet.voicemeetbackend.model.ChatMessage;
import com.voicemeet.voicemeetbackend.repository.ChatRepository;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRepository chatRepository;

    public ChatController(SimpMessagingTemplate messagingTemplate,
                          ChatRepository chatRepository) {
        this.messagingTemplate = messagingTemplate;
        this.chatRepository = chatRepository;
    }

    //////////////////////////////////////////////////
    // PRIVATE CHAT
    //////////////////////////////////////////////////

    @MessageMapping("/private-chat")
    public void privateChat(ChatMessage message){

        // SAVE MESSAGE
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setSender(message.getSender());
        entity.setReceiver(message.getReceiver());
        entity.setMessage(message.getMessage());
        entity.setTime(message.getTime());

        chatRepository.save(entity);

        // SEND TO RECEIVER
        messagingTemplate.convertAndSend(
                "/topic/private/" + message.getReceiver(),
                message
        );

        // SEND BACK TO SENDER
        messagingTemplate.convertAndSend(
                "/topic/private/" + message.getSender(),
                message
        );
    }

    //////////////////////////////////////////////////
    // LOAD CHAT HISTORY
    //////////////////////////////////////////////////

    @GetMapping("/history/{user1}/{user2}")
    public List<ChatMessageEntity> getHistory(
            @PathVariable String user1,
            @PathVariable String user2){

        List<ChatMessageEntity> list1 =
                chatRepository.findBySenderAndReceiver(user1,user2);

        List<ChatMessageEntity> list2 =
                chatRepository.findBySenderAndReceiver(user2,user1);

        list1.addAll(list2);

        return list1;
    }
}