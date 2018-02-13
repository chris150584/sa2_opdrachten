package be.kdg.blog.dto.thymeleaf;

import java.time.LocalDateTime;
import java.util.List;

public class BlogEntryDto implements Comparable<BlogEntryDto> {
    private long id;
    private String subject;
    private String message;
    private LocalDateTime dateTime;
    private List<TagDto> tags;

    public BlogEntryDto() {
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

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

    @Override
    public int compareTo(BlogEntryDto o) {
        return o.dateTime.compareTo(this.dateTime);
    }
}
