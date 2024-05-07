package org.example.g14.service;

import org.example.g14.dto.response.UserFollowedResponseDto;
import org.example.g14.dto.response.UserResponseDto;
import org.example.g14.exception.UserNotFoundException;
import org.example.g14.model.Post;
import org.example.g14.model.User;
import org.example.g14.repository.PostRepository;
import org.example.g14.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PostRepository postRepository;
    @InjectMocks
    UserService userService;

    @Test
    public void userToFollowExists() {
        // Arrange
        int userId = 1;
        int userIdToFollow = 20;

        User user = User.builder()
            .id(userId)
            .name("John Doe")
            .idFollowers(new ArrayList<>())
            .idFollows(new ArrayList<>())
            .build();
        User userToFollow = User.builder()
            .id(userIdToFollow)
            .name("Aria Sanchez")
            .idFollowers(new ArrayList<>())
            .idFollows(new ArrayList<>())
            .build();

        when(userRepository.getById(userId)).thenReturn(Optional.of(user));
        when(userRepository.getById(userIdToFollow)).thenReturn(Optional.of(userToFollow));
        when(postRepository.findAllByUser(userIdToFollow)).thenReturn(List.of(new Post()));

        UserFollowedResponseDto expectedResponse = new UserFollowedResponseDto(
            userId,
            "John Doe",
            new ArrayList<>(List.of(new UserResponseDto(20, "Aria Sanchez")))
        );

        // Act
        UserFollowedResponseDto response = userService.follow(userId, userIdToFollow);

        // Assert
        Assertions.assertEquals(expectedResponse, response);
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).save(userToFollow);
    }

    @Test
    public void userToFollowDoesntExist() {
        // Arrange
        int userId = 1;
        int userIdToFollow = 36;

        when(userRepository.getById(userId)).thenReturn(Optional.of(
            User.builder()
                .id(userId)
                .name("John Doe")
                .idFollowers(new ArrayList<>())
                .idFollows(new ArrayList<>())
                .build()
        ));
        when(userRepository.getById(userIdToFollow)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(
            UserNotFoundException.class,
            () -> userService.follow(userId, userIdToFollow)
        );
    }
}
