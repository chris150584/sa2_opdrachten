package be.kdg.blog;


import be.kdg.blog.model.Blog;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application extends SpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");

Blog blog = (Blog)context.getBean("respBody");

blog.getEntries();
    }
}
