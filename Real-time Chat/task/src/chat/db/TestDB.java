package chat.db;

import chat.model.Message;
import chat.model.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TestDB {
    private final List<Message> publicMessageList;
    private final Deque<User> onlineUser;

    private final List<Message> privateMessageList;

    public TestDB(){
        publicMessageList = new ArrayList<>();
        onlineUser = new ArrayDeque<>();
        privateMessageList = new ArrayList<>();
    }

    public void addMessage(Message message){
        publicMessageList.add(message);
    }

    public List<Message> getPublicMessageList() {
        return publicMessageList;
    }

    public void addOnlineUser(User user){
        onlineUser.add(user);

        System.out.println("User " + user.getName() + " added to online users list");
    }

    // Get messages where the sender is the user and the receiver is the receiver
    public List<Message> getPrivateMessages(User sender, User receiver){
        List<Message> tempPrivateMessageList = new ArrayList<>();

        //Integer senderId = sender.getUserChatId();
        //Integer receiverId = receiver.getUserChatId();

        String senderName = sender.getName();
        String receiverName = receiver.getName();

        for (Message message : privateMessageList) {
            if (message.getSender().getName().equals(senderName) && message.getReceiver().getName().equals(receiverName) ||
                    message.getSender().getName().equals(receiverName) && message.getReceiver().getName().equals(senderName)){
                tempPrivateMessageList.add(message);
            }
        }

//        for (Message message : privateMessageList) {
//            if (message.getSender().getUserChatId().equals(senderId) && message.getReceiver().getUserChatId().equals(receiverId) ||
//                    message.getSender().getUserChatId().equals(receiverId) && message.getReceiver().getUserChatId().equals(senderId)){
//                tempPrivateMessageList.add(message);
//            }
//        }

        return tempPrivateMessageList;
    }

    public void addPrivateMessage(Message message){
        privateMessageList.add(message);
    }



    public Deque<User> getOnlineUser() {
        return onlineUser;
    }

    public User getOnlineUserById(int userId) {
        for (User user : onlineUser) {
            if (user.getUserChatId() == userId) {
                return user;
            }
        }
        return null;
    }

    public User getOnlineUserByName(String name) {
        for (User user : onlineUser) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

}
