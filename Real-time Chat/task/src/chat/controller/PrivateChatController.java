package chat.controller;

import chat.db.TestDB;
import chat.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin("http://localhost:63342")
public class PrivateChatController {

    private TestDB testDB;

    @Autowired
    public void setTestDB(TestDB testDB) {
        this.testDB = testDB;
    }

    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @MessageMapping("/chat.sendPrivateMessage")
    public Message sendPrivateMessage(@Payload Message message) {
        //String receiverId = message.getReceiver().getUserChatId().toString();
        //String senderId = message.getSender().getUserChatId().toString();

        String receiverName = message.getReceiver().getName();
        //String senderName = message.getSender().getName();

        //simpMessagingTemplate.convertAndSendToUser(receiverId, "/private", message);
        //simpMessagingTemplate.convertAndSendToUser(senderId, "/private", message);

        simpMessagingTemplate.convertAndSendToUser(receiverName, "/private", message);
        //simpMessagingTemplate.convertAndSendToUser(senderName, "/private", message);

        testDB.addPrivateMessage(message);

        return message;
    }

}
