package org.example.g14.service;

import org.example.g14.dto.response.UserFollowersCountResponseDto;
import org.example.g14.exception.NotSellerException;
import org.example.g14.model.Post;
import org.example.g14.model.User;
import org.example.g14.repository.IPostRepository;
import org.example.g14.repository.UserRepository;
import org.example.g14.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private IPostRepository postRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCountFollowersBySeller_NoPosts_ThrowsNotSellerException() {
        // Arrange
        int userId = 1;
        User user = new User();
        user.setId(userId);
        user.setIdFollowers(Collections.singletonList(2));
        when(postRepository.findAllByUser(userId)).thenReturn(Collections.emptyList());
        when(userRepository.getById(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(NotSellerException.class, () -> userService.countFollowersBySeller(userId));
    }

    @Test
    public void testCountFollowersBySeller_WithPosts_ReturnsCorrectCount() {
        // Arrange
        int userId = 1;
        User user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setIdFollowers(List.of(2, 3, 4));
        when(postRepository.findAllByUser(userId)).thenReturn(Collections.singletonList(new Post()));
        when(userRepository.getById(userId)).thenReturn(Optional.of(user));

        // Act
        UserFollowersCountResponseDto response = userService.countFollowersBySeller(userId);

        // Assert
        assertEquals(userId, response.getUser_id());
        assertEquals("Test User", response.getUser_name());
        assertEquals(3, response.getFollowers_count());
    }
}
