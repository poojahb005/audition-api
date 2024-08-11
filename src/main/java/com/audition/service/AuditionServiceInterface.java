package com.audition.service;

import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import java.util.List;

public interface AuditionServiceInterface {

    List<AuditionPost> getPosts(final String userId);

    AuditionPost getPostById(final String postId);

    AuditionPost getCommentsByPostId(final String postId);

    List<AuditionComment> getComments(final String postId);
}
