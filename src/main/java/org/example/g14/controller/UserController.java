package org.example.g14.controller;

import org.example.g14.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    IUserService iUserService;


    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<?> countFollowersBySeller(@PathVariable int userId){
        return new ResponseEntity<>(iUserService.countFollowersBySeller(userId), HttpStatus.OK);
    }
}
