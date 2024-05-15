package chat.model;

public class User {
    private Integer userChatId;
    private String name;

    public User() {
    }

    public User(String name) {
        this.userChatId = (int) (Math.random() * 1000);
        this.name = name;
    }

    public Integer getUserChatId() {
        return userChatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
