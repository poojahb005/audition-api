package com.audition.service;

import com.audition.common.annotation.LogEntryExit;
import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@LogEntryExit
public class AuditionService implements AuditionServiceInterface {

    @Autowired
    private AuditionIntegrationClient auditionIntegrationClient;

    public List<AuditionPost> getPosts(final String userId) {
        return auditionIntegrationClient.getPostsByUserId(userId);
    }

    public AuditionPost getPostById(final String postId) {
        return auditionIntegrationClient.getPostById(postId);
    }

    @Override
    public AuditionPost getCommentsByPostId(String postId) {
        return auditionIntegrationClient.getCommentsByPostId(postId);
    }

    @Override
    public List<AuditionComment> getComments(String postId) {
        return auditionIntegrationClient.getComments(postId);
    }

}
