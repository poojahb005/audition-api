package com.audition.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.audition.util.TestData;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuditionServiceTest {

    @InjectMocks
    AuditionServiceInterface auditionService = new AuditionService();

    @Mock
    private AuditionIntegrationClient auditionIntegrationClient;

    static List<AuditionPost> posts = new ArrayList<>();
    static List<AuditionComment> auditionComments = new ArrayList<>();

    @BeforeAll
    public static void setUp() {
        // data setup, total 9 posts
        posts = TestData.getPosts();
        auditionComments = TestData.getComments(posts);
    }


    @AfterAll
    static void cleanUp() {
        posts = null;
        auditionComments = null;
    }

    @Test
    void shouldGetPostsWithQueryParamNull() {
        Mockito.when(auditionIntegrationClient.getPostsByUserId(null)).thenReturn(posts);
        List<AuditionPost> response = auditionService.getPosts(null);
        verify(auditionIntegrationClient, times(1)).getPostsByUserId(any());
        assertEquals(9, response.size());
    }

    @Test
    void shouldGetPostsWithQueryParam() {
        List<AuditionPost> filteredPosts = posts.stream().filter(post -> Integer.parseInt("1") == post.getUserId())
            .collect(Collectors.toList());
        Mockito.when(auditionIntegrationClient.getPostsByUserId("1")).thenReturn(filteredPosts);
        List<AuditionPost> response = auditionService.getPosts("1");
        verify(auditionIntegrationClient, times(1)).getPostsByUserId(any());
        assertEquals(4, response.size());
    }

    @Test
    void shouldGetPostById() {
        AuditionPost auditionPost = posts.stream().filter(post -> Integer.parseInt("1") == post.getId()).findFirst()
            .get();
        Mockito.when(auditionIntegrationClient.getPostById("1")).thenReturn(auditionPost);
        AuditionPost response = auditionService.getPostById("1");
        verify(auditionIntegrationClient, times(1)).getPostById(any());
        assertEquals(auditionPost.getId(), response.getId());
    }

    @Test
    void shouldGetCommentsByPostId() {
        AuditionPost auditionPost = posts.stream().filter(post -> Integer.parseInt("6") == post.getId()).findFirst()
            .get();
        Mockito.when(auditionIntegrationClient.getCommentsByPostId("6")).thenReturn(auditionPost);
        AuditionPost response = auditionService.getCommentsByPostId("6");
        verify(auditionIntegrationClient, times(1)).getCommentsByPostId(any());
        assertEquals(2, response.getComments().size());
    }

    @Test
    void shouldGetCommentsByQueryParamNull() {
        List<AuditionComment> auctionComments = new ArrayList<>();
        auctionComments.add(new AuditionComment());
        Mockito.when(auditionIntegrationClient.getComments(null)).thenReturn(auctionComments);
        List<AuditionComment> response = auditionService.getComments(null);
        verify(auditionIntegrationClient, times(1)).getComments(any());
        assertEquals(1, response.size());
    }

    @Test
    void shouldGetCommentsByQueryParam() {
        List<AuditionComment> filteredAuditionComments = auditionComments.stream()
            .filter(comment -> Integer.parseInt("7") == comment.getPostId())
            .toList();
        Mockito.when(auditionIntegrationClient.getComments("7")).thenReturn(filteredAuditionComments);
        List<AuditionComment> response = auditionService.getComments("7");
        verify(auditionIntegrationClient, times(1)).getComments(any());
        assertEquals(2, response.size());
    }

}
