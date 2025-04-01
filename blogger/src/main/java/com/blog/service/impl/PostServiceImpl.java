package com.blog.service.impl;

import com.blog.entity.Post;
import com.blog.exception.ResourceNotFoundException;
import com.blog.payload.PostDto;
import com.blog.repository.PostRepository;
import com.blog.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

// ---you can also use @Autowired to set the PostRepository

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


//------------these are the override methods-----------------


    //this is createPost override method.
    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = new Post();

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

// this is saved contents which is referred by 'saved'.
        Post saved = postRepository.save(post);

//this is for copy data to dto so we can customise the data
        PostDto dto = new PostDto();
        dto.setId(saved.getId());
        dto.setTitle(saved.getTitle());
        dto.setContent(saved.getContent());
        dto.setDescription(saved.getDescription());

        return dto;

        /* instead of this we can also write like that below
        PostDto dto = mapToDto(saved);

         */


    }

    //this is deletePost override method.

    @Override
    public void deletePost(long id) {
            postRepository.findById(id).orElseThrow(
                    ()-> new ResourceNotFoundException("Post not found with id: " +id)

            );

            postRepository.deleteById(id);

    }

    @Override
    public List<PostDto> getAllPost(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?
                Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> pagePosts = postRepository.findAll(pageable);
        List<Post> posts = pagePosts.getContent();
        List<PostDto> dtos = posts.stream().map(p -> mapToDto(p)).collect(Collectors.toList());
        return dtos;

    }

    @Override
    public PostDto updatePost(long postId, PostDto postDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found with id: " + postId)
        );

        post.setTitle(postDto.getContent());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());

        Post saved = postRepository.save(post);
        PostDto dto = mapToDto(saved);

        return dto;
    }

    PostDto mapToDto(Post post) {
    PostDto dto = new PostDto();
    dto.setId(post.getId());
    dto.setTitle(post.getTitle());
    dto.setContent(post.getContent());
    dto.setDescription(post.getDescription());

    return dto;

}
}