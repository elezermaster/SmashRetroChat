package io.emaster.smashretrochat.model;

/**
 * Created by elezermaster on 24/10/2017.
 */

public class Messages {
    String message = "";
    Boolean seen;// String seen;
    Long time;// String time;
    String type = "";
    String from = "";


    public Messages(){}

    public Messages(String message, Boolean seen, Long time, String type, String from) {
        this.message = message;
        this.seen = seen;
        this.type = type;
        this.time = time;
        this.from = from;

    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {

        return from;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSeen() {
        return seen;
    }

    public String getType() {
        return type;
    }

    public Long getTime() {
        return time;
    }
}
