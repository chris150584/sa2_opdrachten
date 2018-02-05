package be.kdg.blog.controllers;

import be.kdg.blog.model.Blog;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ResponseBodyController {
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



    private Blog blog;

   // public ResponseBodyController(){

   // }

    public ResponseBodyController(Blog blog){

        this.blog = blog;}



}
