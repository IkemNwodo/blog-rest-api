package com.ikem.blog.blogrestapi.controller;

import com.ikem.blog.blogrestapi.payload.CommentDto;
import com.ikem.blog.blogrestapi.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/")
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;

    @PostMapping("posts/{postId}/comments")
    public CommentDto createComment(@PathVariable long postId,
                                    @Valid @RequestBody CommentDto commentDto){
        return commentService.createComment(postId, commentDto);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable long postId){
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping ("/posts/{postId}/comments/{commentId}")
    public CommentDto getCommentById(@PathVariable long postId,
                                     @PathVariable long commentId){
        return commentService.getCommentById(postId, commentId);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public CommentDto updateComment(@PathVariable long commentId,
                                    @PathVariable long postId,
                                    @Valid @RequestBody CommentDto commentDto){
        return commentService.updateComment(postId, commentId, commentDto);
    }
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public String deleteComment(@PathVariable long commentId, @PathVariable long postId){
        commentService.deleteComment(postId, commentId);

        return "Comment deleted successfully.";
    }
}
