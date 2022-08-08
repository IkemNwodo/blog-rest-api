package com.ikem.blog.blogrestapi.service.impl;

import com.ikem.blog.blogrestapi.entity.Comment;
import com.ikem.blog.blogrestapi.entity.Post;
import com.ikem.blog.blogrestapi.exception.BlogAPIException;
import com.ikem.blog.blogrestapi.exception.ResourceNotFoundException;
import com.ikem.blog.blogrestapi.payload.CommentDto;
import com.ikem.blog.blogrestapi.repository.CommentRepository;
import com.ikem.blog.blogrestapi.repository.PostRepository;
import com.ikem.blog.blogrestapi.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {


    private ModelMapper mapper;

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        // retrieve post entity by id
        Post post = postRepository.findPostById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // then set post to comment entity
        comment.setPost(post);

        // comment entity to DB
        Comment newComment = commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        // retrieve comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);

        // convert list of comment entities to list of comment dto's
        return comments.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        // retrieve post entity by id
        postRepository.findPostById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));
        log.info(String.format("Post id is %s", postId));

        Comment comment = commentRepository.findCommentById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId)
        );
        log.info(String.format("Comment's post id is %s", comment.getPost().getId()));

        // check if comment belongs to a post
        if (!comment.getPost().getId().equals(postId)){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentRequest) {
        // retrieve post entity by id
        postRepository.findPostById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));
        log.info(String.format("Post id is %s", postId));

        Comment comment = commentRepository.findCommentById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId)
        );
        log.info(String.format("Comment's post id is %s", comment.getPost().getId()));

        // check if comment belongs to a post
        if (!comment.getPost().getId().equals(postId)){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        //log.info(String.format("CommentRequest body is %s", commentRequest.getBody()));

        Comment updatedComment = commentRepository.save(comment);
        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        postRepository.findPostById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));
        log.info(String.format("Post id is %s", postId));

        Comment comment = commentRepository.findCommentById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId)
        );
        log.info(String.format("Comment's post id is %s", comment.getPost().getId()));

        // check if comment belongs to a post
        if (!comment.getPost().getId().equals(postId)){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        commentRepository.delete(comment);
    }

    private CommentDto mapToDTO(Comment comment){
        return mapper.map(comment, CommentDto.class);
    }

    private Comment mapToEntity(CommentDto commentDto){
        return mapper.map(commentDto, Comment.class);
    }
}
