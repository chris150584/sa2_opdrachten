package be.kdg.blog.model;

import java.time.LocalDateTime;
import java.util.List;

public class BlogEntry {
    private long id;
    private String subject;
    private String message;
    private final LocalDateTime dateTime;
    private List<Tag> tags;

    public BlogEntry() {
        this.dateTime = LocalDateTime.now();
    }

    public BlogEntry(long id, String subject, String message, LocalDateTime dateTime) {
        this.id = id;
        this.subject = subject;
        this.message = message;
        this.dateTime = dateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
