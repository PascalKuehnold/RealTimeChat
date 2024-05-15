package chat.controller;

import chat.db.TestDB;
import chat.model.Message;
import chat.model.PrivateMessageRequest;
import chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Deque;
import java.util.List;

@RestController
public class ChatController {
    TestDB testDB;

    @Autowired
    public ChatController(TestDB testDB) {
        this.testDB = testDB;
    }

    @GetMapping("/messages")
    public List<Message> getMessages() {
        return testDB.getPublicMessageList();
    }

    @GetMapping("/online-users")
    public Deque<User> getOnlineUsers() {
        return testDB.getOnlineUser();
    }

    @PostMapping("/private-messages")
    public List<Message> getPrivateMessages(@RequestBody PrivateMessageRequest request) {
        // get the user object from the userid
        //User sender = testDB.getOnlineUserById(request.getSender().getUserChatId());
        User sender = testDB.getOnlineUserByName(request.getSender().getName());
        User receiver = testDB.getOnlineUserByName(request.getReceiver().getName());
        return testDB.getPrivateMessages(sender, receiver);
    }

}
