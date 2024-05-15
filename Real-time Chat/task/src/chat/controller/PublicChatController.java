package chat.controller;

import chat.db.TestDB;
import chat.model.Message;
import chat.model.MessageType;
import chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin("http://localhost:63342")
public class PublicChatController
{
    TestDB testDB;

    @Autowired
    public PublicChatController(TestDB testDB) {
        this.testDB = testDB;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/chatroom/public")
    public Message sendMessage(@Payload Message message) {
        System.out.println(message);
        testDB.addMessage(message);
        return new Message(message.getSender(), message.getDate(),  message.getContent(), MessageType.CHAT);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/chatroom/public")
    public Message addUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println(message);

        User newOnlineUser = new User(message.getSender().getName());
        testDB.addOnlineUser(newOnlineUser);

        headerAccessor.getSessionAttributes().put("userId", newOnlineUser.getUserChatId());

        return new Message(newOnlineUser, message.getDate(), "joined the chat", MessageType.JOIN);
    }
}
