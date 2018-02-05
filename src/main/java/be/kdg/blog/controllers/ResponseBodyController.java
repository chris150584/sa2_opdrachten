package be.kdg.blog.controllers;

import be.kdg.blog.model.Blog;
import be.kdg.blog.model.BlogEntry;
import be.kdg.blog.model.Tag;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ResponseBodyController extends Blog {

    private StringBuilder stringBuilder;
    private Blog blog;


public ResponseBodyController(){}

    @GetMapping(value ={"/"}, produces= MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getClientText(){

        return "Hello, world !";
    }

    @GetMapping(value ={"/html"}, produces= MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getHtmlText(){

        return "<html><head><title>Hello!</title></head><body><h1>Hello, world!</h1></body></html>";
    }

    @GetMapping(value ={"/blog"}, produces= MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getBlogText(){

   blog = new ResponseBodyController(blog);

    stringBuilder = new StringBuilder();
    stringBuilder.append("<html><head><title>Hello!</title></head><body>");

        stringBuilder.append("<H1>").append("My Blog").append("</H1>");
        for (BlogEntry blogEntry:blog.getEntries()) {

            stringBuilder.append("<H2>").append(blogEntry.getSubject()).append("</H2>");

            for(Tag tags:blogEntry.getTags())
            {stringBuilder.append("<li>").append(tags.getName()).append("</li>");}

            stringBuilder.append("<p align=\"left\">").append("<span = 3>").append(blogEntry.getMessage()).append("</span>").append("</p>");
        }
        stringBuilder.append("</body></html>");
        return stringBuilder.toString();

    }

    // VERDER in te vullen
    @GetMapping(value ="{/api/blog/entries/{entryId}}", produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getJsonText(){

    return "";
    }


    // public ResponseBodyController(){

   // }

    public ResponseBodyController(Blog blog){

        this.blog = blog;}



}
