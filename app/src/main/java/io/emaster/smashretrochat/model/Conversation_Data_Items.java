package io.emaster.smashretrochat.model;

/**
 * Created by Administrator on 17-05-2017.
 */

public class Conversation_Data_Items {
    private String message;
    private String sender;

    public Conversation_Data_Items()
    {
    }

    public Conversation_Data_Items(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
