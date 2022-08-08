package com.ikem.blog.blogrestapi.controller;

import com.ikem.blog.blogrestapi.entity.Post;
import com.ikem.blog.blogrestapi.payload.PostDto;
import com.ikem.blog.blogrestapi.payload.PostResponse;
import com.ikem.blog.blogrestapi.service.PostService;
import com.ikem.blog.blogrestapi.utils.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private PostService postService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save-posts")
    public ResponseEntity<List<PostDto>> createListOfPosts(@RequestBody List<PostDto> postDto){
        return new ResponseEntity<>(postService.createListOfPosts(postDto), HttpStatus.CREATED);
    }

    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
            ){
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public PostDto getPost(@PathVariable Long id){
        return postService.getPostById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public PostDto updatedPost(@Valid @RequestBody PostDto postDto, @PathVariable long id){
       return postService.updatePost(postDto, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable long id){
        postService.deletePostById(id);
        return "Post deleted successfully.";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public String deleteAllPosts(){
        postService.deleteAllPosts();
        return "All posts deleted successfully.";
    }
}
