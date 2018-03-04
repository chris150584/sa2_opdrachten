package be.kdg.blog.dto.thymeleaf;

public class TagDto implements Comparable<TagDto> {
    private long id;
    private String name;

    public TagDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(TagDto o) {
        return this.name.compareTo(o.name);
    }
}
