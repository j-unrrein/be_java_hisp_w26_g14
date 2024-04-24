package org.example.g14.controller;

import org.example.g14.dto.PostDto;
import org.example.g14.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IUserService userService;

    @GetMapping("/followed/{user_id}/list")
    public ResponseEntity<List<PostDto>> getPostsFromFollowed(@PathVariable("user_id") int userId){
        List<PostDto> posts = userService.getPostsFromFollowed(userId);
        return ResponseEntity.ok(posts);
    }
}
