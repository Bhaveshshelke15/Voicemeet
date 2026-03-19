package com.voicemeet.voicemeetbackend.repository;



import com.voicemeet.voicemeetbackend.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatMessageEntity,Long> {

    List<ChatMessageEntity> findBySenderAndReceiver(String sender,String receiver);

    List<ChatMessageEntity> findByReceiverAndSender(String receiver,String sender);

}