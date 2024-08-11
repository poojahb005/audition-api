package com.audition.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionCommentBase;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class AuditionIntegrationClientTest {

    @InjectMocks
    static AuditionIntegrationClient auditionIntegrationClient = new AuditionIntegrationClient();

    @Mock
    private RestTemplate restTemplate;

    static List<AuditionPost> posts = new ArrayList<>();
    static List<AuditionComment> auditionComments = new ArrayList<>();

    @BeforeAll
    public static void setUp() {
        ReflectionTestUtils.setField(auditionIntegrationClient, "baseurl", "baseurl");
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
    void shouldGetPostsForQueryParamNull() {
        // mock posts end point
        ResponseEntity<List<AuditionPost>> responseEntity = new ResponseEntity<>(posts,
            HttpStatus.OK);
        Mockito.when(restTemplate.exchange("baseurl/posts", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<AuditionPost>>() {
                }))
            .thenReturn(responseEntity);
        List<AuditionPost> response = auditionIntegrationClient.getPostsByUserId(null);
        verify(restTemplate, times(1)).exchange("baseurl/posts", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<AuditionPost>>() {
            });
        assertEquals(9, response.size());
    }

    @Test
    void shouldGetPostsForQueryParam() {
        // mock posts end point
        List<AuditionPost> filteredPosts = posts.stream().filter(post -> Integer.parseInt("2") == post.getUserId())
            .collect(Collectors.toList());
        ResponseEntity<List<AuditionPost>> responseEntity = new ResponseEntity<>(filteredPosts,
            HttpStatus.OK);
        Mockito.when(restTemplate.exchange("baseurl/posts?userId=2", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<AuditionPost>>() {
                }))
            .thenReturn(responseEntity);
        List<AuditionPost> response = auditionIntegrationClient.getPostsByUserId("2");
        verify(restTemplate, times(1)).exchange("baseurl/posts?userId=2", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<AuditionPost>>() {
            });
        assertEquals(5, response.size());
    }

    @Test
    void shouldGetPostsByPostId() {
        // mock posts end point
        AuditionPost filteredPost = posts.stream().filter(post -> Integer.parseInt("7") == post.getId())
            .findFirst().get();
        ResponseEntity<AuditionPost> responseEntity = new ResponseEntity<>(filteredPost,
            HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity("baseurl/posts/7", AuditionPost.class))
            .thenReturn(responseEntity);
        AuditionPost response = auditionIntegrationClient.getPostById("7");
        verify(restTemplate, times(1)).getForEntity("baseurl/posts/7", AuditionPost.class);
        assertEquals(7, response.getId());
    }

    @Test
    void shouldThrowExceptionWhenGetPostsByPostIdNotFound() {
        // mock posts end point
        Mockito.when(restTemplate.getForEntity("baseurl/posts/17", AuditionPost.class))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "not found"));
        verify(restTemplate, times(0)).getForEntity("baseurl/posts/17", AuditionPost.class);
        SystemException exception = assertThrows(SystemException.class,
            () -> auditionIntegrationClient.getPostById("17"));
        assertEquals("Cannot find a Post with id 17", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenGetPostsByPostId() {
        // mock posts end point
        Mockito.when(restTemplate.getForEntity("baseurl/posts/17", AuditionPost.class))
            .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "API error"));
        verify(restTemplate, times(0)).getForEntity("baseurl/posts/17", AuditionPost.class);
        Exception exception = assertThrows(Exception.class,
            () -> auditionIntegrationClient.getPostById("17"));
        assertEquals("500 API error", exception.getMessage());
    }

    @Test
    void shouldGetCommentsByPostId() {
        // mock posts end point
        AuditionPost filteredPost = posts.stream().filter(post -> Integer.parseInt("7") == post.getId())
            .findFirst().get();
        ResponseEntity<AuditionPost> auditionPostResponseEntity = new ResponseEntity<>(filteredPost,
            HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity("baseurl/posts/7", AuditionPost.class))
            .thenReturn(auditionPostResponseEntity);
        // mock comments end point
        List<AuditionCommentBase> filteredComments = auditionComments.stream()
            .filter(comment -> Integer.parseInt("7") == comment.getPostId())
            .collect(Collectors.toList());
        ResponseEntity<List<AuditionCommentBase>> auditionCommentResponseEntity = new ResponseEntity<>(filteredComments,
            HttpStatus.OK);
        Mockito.when(restTemplate.exchange("baseurl/posts/7/comments", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<AuditionCommentBase>>() {
                }))
            .thenReturn(auditionCommentResponseEntity);
        AuditionPost response = auditionIntegrationClient.getCommentsByPostId("7");
        verify(restTemplate, times(1)).getForEntity("baseurl/posts/7", AuditionPost.class);
        verify(restTemplate, times(1)).exchange("baseurl/posts/7/comments", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<AuditionCommentBase>>() {
            });
        assertEquals(2, response.getComments().size());
        assertEquals(7, response.getId());
    }

    @Test
    void shouldGetCommentsWithQueryParamIsNull() {
        // mock comments end point
        ResponseEntity<List<AuditionComment>> auditionCommentResponseEntity = new ResponseEntity<>(auditionComments,
            HttpStatus.OK);
        Mockito.when(restTemplate.exchange("baseurl/comments", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<AuditionComment>>() {
                }))
            .thenReturn(auditionCommentResponseEntity);
        List<AuditionComment> response = auditionIntegrationClient.getComments(null);
        verify(restTemplate, times(0)).getForEntity("baseurl/posts/7", AuditionPost.class);
        verify(restTemplate, times(1)).exchange("baseurl/comments", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<AuditionComment>>() {
            });
        assertEquals(22, response.size());
    }

    @Test
    void shouldGetCommentsWithQueryParam() {
        // mock posts end point
        AuditionPost filteredPost = posts.stream().filter(post -> Integer.parseInt("7") == post.getId())
            .findFirst().get();
        ResponseEntity<AuditionPost> auditionPostResponseEntity = new ResponseEntity<>(filteredPost,
            HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity("baseurl/posts/7", AuditionPost.class))
            .thenReturn(auditionPostResponseEntity);
        // mock comments end point
        List<AuditionComment> filteredComments = auditionComments.stream()
            .filter(comment -> Integer.parseInt("7") == comment.getPostId())
            .collect(Collectors.toList());
        ResponseEntity<List<AuditionComment>> auditionCommentResponseEntity = new ResponseEntity<>(filteredComments,
            HttpStatus.OK);
        Mockito.when(restTemplate.exchange("baseurl/comments?postId=7", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<AuditionComment>>() {
                }))
            .thenReturn(auditionCommentResponseEntity);
        List<AuditionComment> response = auditionIntegrationClient.getComments("7");
        verify(restTemplate, times(1)).getForEntity("baseurl/posts/7", AuditionPost.class);
        verify(restTemplate, times(1)).exchange("baseurl/comments?postId=7", HttpMethod.GET, null,
            new ParameterizedTypeReference<List<AuditionComment>>() {
            });
        assertEquals(2, response.size());
        assertEquals(7, response.get(0).getPostId());
    }


}
