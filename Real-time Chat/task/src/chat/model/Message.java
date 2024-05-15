package chat.model;

public class Message {
    private User sender;
    private User receiver;
    private String date;
    private String content;

    private MessageType type;

    public Message() {
    }

    //Public Message
    public Message(User sender, String date, String content, MessageType type) {
        this.sender = sender;
        this.date = date;
        this.content = content;
        this.type = type;
    }

    //Private Message
    public Message(User sender, User receiver, String date, String content, MessageType type) {
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
        this.content = content;
        this.type = type;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}


