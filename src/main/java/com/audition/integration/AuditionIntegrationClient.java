package com.audition.integration;

import com.audition.common.annotation.LogEntryExit;
import com.audition.common.exception.SystemException;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionCommentBase;
import com.audition.model.AuditionPost;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@LogEntryExit
public class AuditionIntegrationClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${audition.posts.url}")
    private String baseurl;


    /**
     * restTemplate call to get Posts from <a
     * href="https://jsonplaceholder.typicode.com/posts?userId={userId}">...</a>;
     *
     * @param userId user Id
     * @return {@link AuditionPost}
     */
    public List<AuditionPost> getPostsByUserId(final String userId) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("userId", userId);
        String baseUrl = baseurl + "/posts";
        String uri = buildUriForQueryParams(baseUrl, uriVariables);
        ResponseEntity<List<AuditionPost>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null,
            new ParameterizedTypeReference<>() {
            });
        return responseEntity.getBody();
    }

    /**
     * restTemplate call to get post by post ID call from <a
     * href="https://jsonplaceholder.typicode.com/posts/{postId}">...</a>
     *
     * @param postId post id
     * @return {@link AuditionPost}
     */
    public AuditionPost getPostById(final String postId) {
        String url = baseurl + "/posts/" + postId;
        ResponseEntity<AuditionPost> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(url, AuditionPost.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + postId, "Resource Not Found",
                    404);
            } else {
                throw e;
            }
        }
        return responseEntity.getBody();
    }

    /**
     * GET comments for a post from <a href="https://jsonplaceholder.typicode.com/posts/{postId}/comments">...</a>
     *
     * @param id post id
     * @return {@link AuditionPost}
     */
    public AuditionPost getCommentsByPostId(final String id) {
        AuditionPost auditionPost = getPostById(id);
        String url = baseurl + "/posts/" + id + "/comments";
        ResponseEntity<List<AuditionCommentBase>> commentsResponseEntity = restTemplate.exchange(url, HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {
            });
        // build response
        // comments is returned as part of the post.
        auditionPost.setComments(commentsResponseEntity.getBody());
        return auditionPost;
    }

    /**
     * GET comments for a particular Post from <a
     * href="https://jsonplaceholder.typicode.com/comments?postId={postId}">...</a>
     *
     * @param postId post id
     * @return {@link List<AuditionComment>}
     */
    public List<AuditionComment> getComments(String postId) {
        // check if postId exists
        if (null != postId) {
            getPostById(postId);
        }
        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("postId", postId);
        String baseUrl = baseurl + "/comments";
        String uri = buildUriForQueryParams(baseUrl, uriVariables);
        ResponseEntity<List<AuditionComment>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null,
            new ParameterizedTypeReference<>() {
            });
        return responseEntity.getBody();
    }

    private String buildUriForQueryParams(String baseUrl, Map<String, String> queryParams) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl);
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            if (null != entry.getValue()) {
                uriBuilder.queryParam(entry.getKey(), entry.getValue());
            }
        }
        return uriBuilder.build().toUriString();
    }
}
