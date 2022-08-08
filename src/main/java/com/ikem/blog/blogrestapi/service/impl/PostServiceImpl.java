package com.ikem.blog.blogrestapi.service.impl;

import com.ikem.blog.blogrestapi.entity.Post;
import com.ikem.blog.blogrestapi.exception.ResourceNotFoundException;
import com.ikem.blog.blogrestapi.payload.PostDto;
import com.ikem.blog.blogrestapi.payload.PostResponse;
import com.ikem.blog.blogrestapi.repository.PostRepository;
import com.ikem.blog.blogrestapi.service.PostService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    private ModelMapper mapper;

    @Override
    public PostDto createPost(PostDto postDto) {

        Post post = mapToPost(postDto);

        Post newPost = postRepository.save(post);

        // Return post response to controller
        return mapToDTO(newPost);
    }

    @Override
    public List<PostDto> createListOfPosts(List<PostDto> postDTOs) {
        List<Post> posts = postDTOs.stream().map(this::mapToPost).toList();
        List<Post> postsResponse = postRepository.saveAll(posts);
        return postsResponse.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        // create Sort object using sortBy and sortDir parameters
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository
                .findAll(pageable);

        List<PostDto> content = posts.stream().map(this::mapToDTO)
                .collect(Collectors.toList());

        return PostResponse.builder()
                .content(content)
                .pageNo(posts.getNumber())
                .pageSize(posts.getSize())
                .totalElements(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .last(posts.isLast())
                .build();

    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findPostById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        Post post = postRepository.findPostById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);
        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepository.findPostById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    @Override
    public void deleteAllPosts() {
        postRepository.deleteAll();
    }

    private PostDto mapToDTO(Post post){
        return mapper.map(post, PostDto.class);
    }

    private Post mapToPost(PostDto postDto){
        return mapper.map(postDto, Post.class);
    }
}
