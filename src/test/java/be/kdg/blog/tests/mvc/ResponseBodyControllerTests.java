package be.kdg.blog.tests.mvc;

import be.kdg.blog.Application;
import be.kdg.blog.model.Blog;
import be.kdg.blog.model.BlogEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ResponseBodyControllerTests {
    @MockBean
    private Blog blog;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
    }

    @Test
    public void testApi() throws Exception {
        List<BlogEntry> blogEntries = new ArrayList<>();
        blogEntries.add(new BlogEntry(1, "SUBJECT1", "MESSAGE1", LocalDateTime.now()));
        blogEntries.add(new BlogEntry(2, "SUBJECT2", "MESSAGE2", LocalDateTime.now()));
        blogEntries.add(new BlogEntry(3, "SUBJECT3", "MESSAGE3", LocalDateTime.now()));

        given(this.blog.getEntries()).willReturn(blogEntries);

        this.mvc.perform(get("/api/blog/entries/2").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject", is("SUBJECT2")))
                .andExpect(jsonPath("$.message", is("MESSAGE2")));
    }
}
