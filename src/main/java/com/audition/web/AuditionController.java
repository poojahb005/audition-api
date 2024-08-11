package com.audition.web;

import com.audition.common.annotation.LogEntryExit;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.audition.service.AuditionServiceInterface;
import com.audition.validation.PositiveInteger;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@LogEntryExit
@Validated
public class AuditionController {

    @Autowired
    AuditionServiceInterface auditionService;

    @GetMapping(value = "posts")
    @Operation(summary = "Get posts", description = "return posts based on optional user Id")
    public List<AuditionPost> getPosts(@RequestParam(required = false) @PositiveInteger String userId) {
        return auditionService.getPosts(userId);
    }

    @GetMapping(value = "posts/{id}")
    @Operation(summary = "Get post by post id", description = "return posts based on post Id")
    public AuditionPost getPostByPostId(@PathVariable("id") @NotNull @PositiveInteger final String postId) {
        return auditionService.getPostById(postId);
    }

    @GetMapping(value = "posts/{id}/comments")
    @Operation(summary = "Get post with comments by post id", description = "return post along with comments based on post Id")
    public AuditionPost getCommentsByPostId(@PathVariable("id") @NotNull @PositiveInteger final String postId) {
        return auditionService.getCommentsByPostId(postId);
    }


    @GetMapping(value = "comments")
    @Operation(summary = "Get comments for particular post", description = "return comments based on optional post Id")
    public List<AuditionComment> getComments(@RequestParam(required = false) @PositiveInteger String postId) {
        return auditionService.getComments(postId);
    }

}
