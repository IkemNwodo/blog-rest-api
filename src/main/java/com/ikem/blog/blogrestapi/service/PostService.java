package com.ikem.blog.blogrestapi.service;

import com.ikem.blog.blogrestapi.entity.Post;
import com.ikem.blog.blogrestapi.payload.PostDto;
import com.ikem.blog.blogrestapi.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);

    List<PostDto> createListOfPosts(List<PostDto> postDTOs);
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPostById(long id);

    PostDto updatePost(PostDto postDto, Long id);

    void deletePostById(long id);

    void deleteAllPosts();
}
