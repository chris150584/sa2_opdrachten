package be.kdg.blog.dto.mapper;

import be.kdg.blog.dto.thymeleaf.BlogEntryDto;
import be.kdg.blog.dto.thymeleaf.TagDto;
import be.kdg.blog.dto.thymeleaf.form.NewBlogEntryFormDto;
import be.kdg.blog.model.BlogEntry;
import be.kdg.blog.model.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public DtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    private BlogEntryDto convertToDto(BlogEntry blogEntry) {
        BlogEntryDto result = modelMapper.map(blogEntry, BlogEntryDto.class);
        Collections.sort(result.getTags());
        return result;
    }

    public List<BlogEntryDto> convertBlogEntriesToDto(List<BlogEntry> blogEntries) {
        return blogEntries
                .stream()
                .map(this::convertToDto)
                .sorted()
                .collect(Collectors.toList());
    }

    public BlogEntry convertFromDto(NewBlogEntryFormDto blogFormDto) {
        return modelMapper.map(blogFormDto, BlogEntry.class);
    }

    private TagDto convertToDto(Tag tag) {
        return modelMapper.map(tag, TagDto.class);
    }

    public List<TagDto> convertTagsToDto(List<Tag> tags) {
        return tags
                .stream()
                .map(this::convertToDto)
                .sorted()
                .collect(Collectors.toList());
    }
}
