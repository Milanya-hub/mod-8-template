package hse.java.practice.chatstomp.controller;
import hse.java.practice.chatstomp.model.ChatMessage;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class ChatController {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final List<ChatMessage> history = new CopyOnWriteArrayList<>(); // потокобезопасный список

    @GetMapping("/")
    public String index() {
        return "chat";
    }

    @MessageMapping("/send") // когда придет соо вызови этот метод
    @SendTo("/topic/messages") // отправь всем кто подписан
    public ChatMessage sendMessage(@Payload ChatMessage msg) {
        String timestamp = LocalDateTime.now().format(formatter);
        ChatMessage message = new ChatMessage(msg.sender(), msg.text(), timestamp);
        history.add(message);
        return message;
    }

    @GetMapping("/history")
    @ResponseBody
    public List<ChatMessage> getHistory() {
        return history;
    }

}
