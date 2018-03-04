package be.kdg.blog.dto.thymeleaf.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class NewBlogEntryFormDto {
    @NotNull
    @Size(min = 3)
    private String subject;

    @NotNull
    @Size(min = 3)
    private String message;

    @NotNull
    @Size(min = 1)
    private List<Long> tagIds;

    public NewBlogEntryFormDto() {
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

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }
}
