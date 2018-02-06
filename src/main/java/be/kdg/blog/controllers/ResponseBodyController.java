package be.kdg.blog.controllers;

import be.kdg.blog.model.Blog;
import be.kdg.blog.model.BlogEntry;
import be.kdg.blog.model.Tag;
import com.google.gson.Gson;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/* ------------------------
            DEEL1
   -----------------------    */
@Controller
public class ResponseBodyController {

    private StringBuilder stringBuilder;
    private final Blog blog;


public ResponseBodyController(Blog blog){

        this.blog = blog;}


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


/* ------------------------
            DEEL2
   -----------------------    */

    public String getBlogText(){

// Moet vervangen worden, maar voor resultaat te bekijken lukt dit



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


    /* ------------------------
            DEEL3
   -----------------------    */

    // VERDER in te vullen
    @GetMapping(value = "/api/blog/entries/{entryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getJsonText(@PathVariable int entryId)
    {

   return new Gson().toJson(this.blog.getEntries().get(--entryId));
    }


/* EXTRA */

  @RequestMapping(value = "/api/blog/entries")
  @ResponseBody
    public String getJsonPath(@RequestParam("id") int entryId) {


        try {
            return new Gson().toJson(blog.getEntries().get(entryId));
        } catch (Exception ex) {
            throw new RuntimeException();
        }

}
}

