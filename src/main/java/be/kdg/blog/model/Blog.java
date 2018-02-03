package be.kdg.blog.model;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Blog {
    private final List<BlogEntry> entries;
    private final List<Tag> tags;

    public Blog() {
        this.entries = new ArrayList<>();
        this.tags = new ArrayList<>();

        this.addTestData();
    }

    private void addTestData() {
        this.tags.add(new Tag(1, "lifestyle"));
        this.tags.add(new Tag(2, "entertainment"));
        this.tags.add(new Tag(3, "technology"));

        final String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas ultricies odio eget nulla tincidunt, non imperdiet nisl tristique. Praesent at eleifend arcu, blandit interdum diam. Maecenas est ipsum, varius a pulvinar eu, bibendum quis sem. Nam interdum maximus nisl, vel ullamcorper lacus sodales quis. Aliquam erat volutpat. Sed id tempor ante. In porta at ante molestie consequat. Quisque ullamcorper turpis neque, sit amet ultrices dolor tincidunt vel. Sed convallis dapibus iaculis. Vestibulum feugiat efficitur consectetur."
                + "Nulla commodo odio egestas enim aliquet laoreet. Curabitur eget congue enim, id pulvinar eros. Interdum et malesuada fames ac ante ipsum primis in faucibus. Nam sit amet eros congue, condimentum lacus quis, venenatis sapien. Morbi blandit lacus arcu. Phasellus sit amet orci ac diam condimentum venenatis non quis justo. Morbi tempor odio sit amet turpis dictum, eget mollis ante blandit. Nam auctor a felis quis porta. Fusce pharetra, magna blandit varius tristique, quam arcu placerat ligula, a pulvinar nulla urna at ligula. Donec vulputate dui placerat, hendrerit turpis et, porttitor velit. Nunc sodales venenatis turpis, eu tempor nibh."
                + "Quisque sit amet aliquam urna, sed iaculis nisl. Vestibulum a volutpat turpis. Suspendisse metus orci, scelerisque in tincidunt et, convallis vitae est. Cras in eros at magna tristique tempor a fringilla tortor. Phasellus fringilla malesuada efficitur. Donec vel pulvinar mi. Praesent et dui ultrices, rutrum lacus eget, tincidunt dolor. Mauris diam sapien, scelerisque ac tellus eu, posuere lobortis nisi. Nulla sagittis diam dui, ut dignissim ipsum luctus at.";

        this.addEntry(new BlogEntry(1, "Naar de bakker geweest", loremIpsum, LocalDateTime.of(2017, 9, 24, 16, 4)),
                new HashSet<>(Arrays.asList(1L, 2L)));
        this.addEntry(new BlogEntry(2, "Netflix gekeken", loremIpsum, LocalDateTime.of(2017, 9, 25, 12, 6)),
                new HashSet<>(Collections.singletonList(2L)));
        this.addEntry(new BlogEntry(3, "Android app gemaakt", loremIpsum, LocalDateTime.of(2017, 9, 26, 11, 59)),
                new HashSet<>(Collections.singletonList(3L)));
    }

    public void addEntry(BlogEntry blogEntry, Set<Long> tagIds) {
        blogEntry.setTags(tags.stream()
                .filter(tag -> tagIds.contains(tag.getId()))
                .collect(Collectors.toList()));
        this.entries.add(blogEntry);
    }

    public List<BlogEntry> getEntries() {
        return entries;
    }

    public List<Tag> getTags() {
        return tags;
    }
}
