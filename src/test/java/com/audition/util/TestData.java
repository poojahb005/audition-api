package com.audition.util;

import com.audition.model.AuditionComment;
import com.audition.model.AuditionCommentBase;
import com.audition.model.AuditionPost;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TestData {

    public static List<AuditionPost> getPosts() {

        List<AuditionPost> posts = new ArrayList<>();
        IntStream.range(1, 5).forEach(postId -> {
            AuditionPost auditionPost = new AuditionPost();
            auditionPost.setId(postId);
            auditionPost.setUserId(1);
            // each post has 3 comments
            List<AuditionCommentBase> comments = new ArrayList<>();
            IntStream.range(1, 4).forEach(commentId -> {
                AuditionComment auditionComment = new AuditionComment();
                auditionComment.setPostId(postId);
                auditionComment.setId(commentId);
                auditionComment.setName("comment");
                auditionComment.setBody("erwrrt");
                auditionComment.setEmail("@.email");
                comments.add(auditionComment);
            });
            auditionPost.setComments(comments);
            posts.add(auditionPost);
        });
        IntStream.range(5, 10).forEach(postId -> {
            AuditionPost auditionPost = new AuditionPost();
            auditionPost.setId(postId);
            auditionPost.setUserId(2);
            // each post has 2 comments
            List<AuditionCommentBase> comments = new ArrayList<>();
            IntStream.range(4, 6).forEach(commentId -> {
                AuditionComment auditionComment = new AuditionComment();
                auditionComment.setPostId(postId);
                auditionComment.setId(commentId);
                comments.add(auditionComment);
            });
            auditionPost.setComments(comments);
            posts.add(auditionPost);
        });
        return posts;
    }

    public static List<AuditionComment> getComments(List<AuditionPost> posts) {
        List<AuditionComment> auditionComments = new ArrayList<>();
        for (AuditionPost post : posts) {
            for (AuditionCommentBase comment : post.getComments()) {
                AuditionComment auditionComment = new AuditionComment();
                auditionComment.setPostId(post.getId());
                auditionComment.setId(comment.getId());
                auditionComments.add(auditionComment);
            }
        }
        return auditionComments;
    }

}
