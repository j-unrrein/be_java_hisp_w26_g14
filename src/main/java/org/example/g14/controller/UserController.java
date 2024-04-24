package org.example.g14.controller;

import org.example.g14.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    IUserService userService;

    
    @PostMapping("/{userId}/unfollow/{userIdToUnfollow}")
    public ResponseEntity<?> unfollowSeller(
        @PathVariable("userId") int followerUserId,
        @PathVariable("userIdToUnfollow") int sellerUserId
    ) {
        userService.unfollowSeller(followerUserId, sellerUserId);

        return ResponseEntity.ok().build();
    }
}
