package com.audition.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.audition.common.logging.AuditionLogger;
import com.audition.service.AuditionService;
import com.audition.service.AuditionServiceInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest({AuditionController.class, AuditionService.class})
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuditionPostControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    AuditionServiceInterface auditionService;

    @MockBean
    AuditionLogger auditionLogger;

    @InjectMocks
    AuditionController auditionController;

    @Test
    public void shouldGetPosts() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/api/posts")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(auditionService, times(1)).getPosts(any());
    }

    @Test
    public void shouldGetPostsReturnBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/api/posts/-1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
        verify(auditionService, times(0)).getPosts(any());
    }

    @Test
    public void shouldGetPostByPostId() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/api/posts/2")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(auditionService, times(1)).getPostById(any());
    }

    @Test
    public void shouldGetCommentsByPostId() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/api/posts/2/comments")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(auditionService, times(1)).getCommentsByPostId(any());
    }

    @Test
    public void shouldGetComments() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/api/comments?postId=1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(auditionService, times(1)).getComments(any());
    }

}
