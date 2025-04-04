package com.blog.controller;


import com.blog.payload.PostDto;
import com.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

//this is constructor based injection
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

//http://localhost:8080/api/posts

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDto postDto, BindingResult bindingResult) {
if(bindingResult.hasErrors()){
    // Object class can be interchangeable with ?
    return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
}
        PostDto dto = postService.createPost(postDto);
        return new ResponseEntity<>(dto,HttpStatus.CREATED);


    }

//    http://localhost:8080/api/posts/2(id.no.)

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable long id){

        postService.deletePost(id);
        return new ResponseEntity<>("Post is deleted", HttpStatus.OK);
    }

//http://localhost:8080/api/posts?pageNo=0&pageSize=5&sortBy=title
    @GetMapping
    public  ResponseEntity<List<PostDto>>getAllPosts(
            @RequestParam(name = "pageNo",defaultValue = "0",required = false) int pageNo,
            @RequestParam(name = "pageSize",defaultValue = "3",required = false) int pageSize,
            @RequestParam(name= "sortBy",defaultValue = "id",required = false) String sortBy,
            @RequestParam(name="sortDir",defaultValue = "asc",required = false) String sortDir
// always check spelling and this is case-sensitive .so it should be exact matched with name
    ){

        List<PostDto> postDtos = postService.getAllPost(pageNo,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(postDtos,HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<PostDto> updatePost(@RequestParam("postId") long postId, @RequestBody PostDto postDto)
    {
        PostDto dto = postService.updatePost(postId, postDto);

        return new ResponseEntity<>(dto,HttpStatus.OK);
    }



}
