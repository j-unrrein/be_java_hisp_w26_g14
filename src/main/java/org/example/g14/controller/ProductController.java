package org.example.g14.controller;

import org.example.g14.dto.request.PostCreateRequestDto;
import org.example.g14.dto.response.PostResponseDto;
import org.example.g14.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    IPostService postService;

    @PostMapping("/post")
    public ResponseEntity<?> createPost(
        @RequestBody PostCreateRequestDto postCreateRequestDto
    ) {
        return new ResponseEntity<>(postService.
                add(postCreateRequestDto), HttpStatus.OK);
    }

    @GetMapping("/followed/{user_id}/list")
    public ResponseEntity<List<PostResponseDto>> getPostsFromFollowed(@PathVariable("user_id") int userId,
                                                                      @RequestParam(required = false) String order){
        List<PostResponseDto> posts = postService.getPostsFromFollowed(userId, order);
        return ResponseEntity.ok(posts);
    }
}
