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

        when(userRepository.getById(userId)).thenReturn(Optional.of(
            User.builder().id(userId).name("John Doe")
                .idFollowers(new ArrayList<>(){{add(2);add(3);add(4);}})
                .idFollows(new ArrayList<>(){{add(5);add(6);add(7);}})
                .build()
        ));
        when(userRepository.getById(userIdToFollow)).thenReturn(Optional.of(
            User.builder().id(userIdToFollow).name("Aria Sanchez")
                .idFollowers(new ArrayList<>(){{add(18);add(19);}})
                .idFollows(new ArrayList<>(){{add(21);add(22);}})
                .build()
        ));

        when(userRepository.getById(5)).thenReturn(
            Optional.of(User.builder().id(5).name("William Taylor").idFollowers(Arrays.asList(2, 3)).idFollows(Arrays.asList(1, 4)).build())
        );
        when(userRepository.getById(6)).thenReturn(
            Optional.of(User.builder().id(6).name("Olivia Martinez").idFollowers(Arrays.asList(1, 2, 5)).idFollows(Arrays.asList(3, 7)).build())
        );
        when(userRepository.getById(7)).thenReturn(
            Optional.of(User.builder().id(7).name("James Anderson").idFollowers(Arrays.asList(3, 4)).idFollows(Arrays.asList(1, 6)).build())
        );
        when(postRepository.findAllByUser(userIdToFollow)).thenReturn(List.of(new Post()));

        List<UserResponseDto> followedUsers = new ArrayList<>() {{
            add(new UserResponseDto(5, "William Taylor"));
            add(new UserResponseDto(6, "Olivia Martinez"));
            add(new UserResponseDto(7, "James Anderson"));
            add(new UserResponseDto(20, "Aria Sanchez"));
        }};

        UserFollowedResponseDto expectedResponse = new UserFollowedResponseDto(userId, "John Doe", followedUsers);

        // Act
        UserFollowedResponseDto response = userService.follow(userId, userIdToFollow);

        // Assert
        Assertions.assertEquals(expectedResponse, response);
        verify(userRepository, times(2)).save(any());
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
                .idFollowers(new ArrayList<>(){{add(2);add(3);add(4);}})
                .idFollows(new ArrayList<>(){{add(5);add(6);add(7);}})
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
