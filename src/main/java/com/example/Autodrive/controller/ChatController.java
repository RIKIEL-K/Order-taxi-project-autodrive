package com.example.Autodrive.controller;

import com.example.Autodrive.model.ChatMessage;
import com.example.Autodrive.repository.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("api")
public class ChatController {
    @Autowired
    private final ChatRepository chatRepo;


    @MessageMapping("/chat/{courseId}")
    @SendTo("/topic/messages/{courseId}")
    public ChatMessage send(@DestinationVariable String courseId, ChatMessage message) {
        message.setCourseId(courseId);
        message.setTimestamp(System.currentTimeMillis());
        ChatMessage saved = chatRepo.save(message);
        System.out.println("Nouveau message enregistré: " + saved.getContent());
        return saved;
    }

    @GetMapping("/chat/{courseId}")
    public List<ChatMessage> getMessages(@PathVariable String courseId) {
        return chatRepo.findByCourseIdOrderByTimestampAsc(courseId);
    }
}
