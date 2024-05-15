package chat.listener;

import chat.db.TestDB;
import chat.model.Message;
import chat.model.MessageType;
import chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
@CrossOrigin("http://localhost:63342")
public class WebSocketEventListener {

    private TestDB TestDB;
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public void setTestDB(chat.db.TestDB testDB) {
        TestDB = testDB;
    }

    @Autowired
    public void setMessagingTemplate(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void connect(SessionConnectedEvent event) {
        System.out.println("Received a new web socket connection");
    }

    @EventListener
    public void disconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        int userId = (int) headerAccessor.getSessionAttributes().get("userId");
        User user = TestDB.getOnlineUserById(userId);

        if(user != null) {

            Message chatMessage = new Message();
            chatMessage.setType(MessageType.LEAVE);
            chatMessage.setSender(user);

            TestDB.getOnlineUser().remove(user);

            messagingTemplate.convertAndSend("/chatroom/public", chatMessage);
        }

        System.out.println("Received a new web socket disconnection");
    }
}
