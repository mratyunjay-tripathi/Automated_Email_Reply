package tech.spirit.automatedemailreply;

import java.util.Date;

public class EmailAttr {

    private String from;
    private String senderName;
    private String to;
    private String subject;
    private String content;
    private String date;

    public EmailAttr(){

    }

    public EmailAttr(String from, String senderName, String to, String subject, String content, String date) {
        this.from = from;
        this.senderName = senderName;
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
