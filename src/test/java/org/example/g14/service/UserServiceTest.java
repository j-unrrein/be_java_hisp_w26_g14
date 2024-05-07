package org.example.g14.service;

import org.example.g14.dto.response.UserFollowedResponseDto;
import org.example.g14.dto.response.UserFollowersCountResponseDto;
import org.example.g14.dto.response.UserFollowersResponseDto;
import org.example.g14.dto.response.UserResponseDto;
import org.example.g14.exception.NotSellerException;
import org.example.g14.exception.OrderInvalidException;
import org.example.g14.exception.UserNotFoundException;
import org.example.g14.model.Post;
import org.example.g14.model.User;
import org.example.g14.repository.IPostRepository;
import org.example.g14.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    IPostRepository postRepository;
    @InjectMocks
    UserService userService;

    @Test
    public void userToFollowExists() {
        // Arrange
        int userId = 1;
        int userIdToFollow = 20;

        User user = User.builder()
            .id(userId)
            .idFollows(new ArrayList<>())
            .build();
        User userToFollow = User.builder()
            .id(userIdToFollow)
            .idFollowers(new ArrayList<>())
            .build();

        when(userRepository.getById(userId)).thenReturn(Optional.of(user));
        when(userRepository.getById(userIdToFollow)).thenReturn(Optional.of(userToFollow));
        when(postRepository.findAllByUser(userIdToFollow)).thenReturn(List.of(new Post()));

        UserFollowedResponseDto expectedResponse = UserFollowedResponseDto.builder()
            .user_id(userId)
            .followed(List.of(UserResponseDto.builder().user_id(20).build()))
            .build();

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
            User.builder().id(userId).build()
        ));
        when(userRepository.getById(userIdToFollow)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(
            UserNotFoundException.class,
            () -> userService.follow(userId, userIdToFollow)
        );
    }

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

    @Test
    @DisplayName("US-0008: Verificar que el tipo de ordenamiento exista en getListOfFollowedSellers. OK")
    void testTypeOfOrderExistInGetListOfFollowedSellers() {
        //Arrange
        String orderDesc = "name_desc";
        String orderAsc = "name_asc";
        int userId = 1;
        User user = new User(1, "Juan", List.of(), List.of(1));
        //Act
        when(userRepository.getById(1)).thenReturn(java.util.Optional.of(user));

        UserFollowedResponseDto resultAscFollowed = userService.getListOfFollowedSellers(userId, orderAsc);
        UserFollowedResponseDto resultDescFollowed = userService.getListOfFollowedSellers(userId, orderDesc);

        //Assert
        Assertions.assertNotNull(resultAscFollowed, "El test por orden ascendente en getListOfFollowedSellers finalizó con éxito");
        Assertions.assertNotNull(resultDescFollowed, "El test por orden descendente en getListOfFollowedSellers finalizó con éxito");
    }

    @Test
    @DisplayName("US-0008: Verificar que el tipo de ordenamiento no exista en getListOfFollowedSellers. BAD REQUEST")
    void testTypeOfOrderNotExistInGetListOfFollowedSellers() {
        //Arrange
        String orderDesc = "desc";
        String orderAsc = "asc";
        int userId = 1;

        Assertions.assertThrows(OrderInvalidException.class, () -> userService.getListOfFollowedSellers(userId, orderAsc));
        Assertions.assertThrows(OrderInvalidException.class, () -> userService.getListOfFollowedSellers(userId, orderDesc));
    }

    @Test
    @DisplayName("US-0008: Verificar que el tipo de ordenamiento exista en getAllFollowers. OK")
    void testTypeOfOrderExistInGetAllFollowers() {
        //Arrange
        String orderDesc = "name_desc";
        String orderAsc = "name_asc";
        int userId = 1;
        User user = new User(1, "Juan", List.of(), List.of());
        //Act
        when(userRepository.getById(userId)).thenReturn(java.util.Optional.of(user));
        when(postRepository.findAllByUser(userId)).thenReturn(List.of(new Post()));

        UserFollowersResponseDto resultAscFollowers = userService.getAllFolowers(userId, orderAsc);
        UserFollowersResponseDto resultDescFollowers = userService.getAllFolowers(userId, orderDesc);

        //Assert
        Assertions.assertNotNull(resultAscFollowers, "El test por orden ascendente en getAllFolowers finalizó con éxito");
        Assertions.assertNotNull(resultDescFollowers, "El test por orden descendente en getAllFolowers finalizó con éxito");
    }

    @Test
    @DisplayName("US-0008: Verificar que el tipo de ordenamiento no exista en getAllFollowers. BAD REQUEST")
    void testTypeOfOrderNotExistInGetAllFollowers() {
        //Arrange
        String orderDesc = "desc";
        String orderAsc = "asc";
        int userId = 1;

        Assertions.assertThrows(OrderInvalidException.class, () -> userService.getAllFolowers(userId, orderAsc));
        Assertions.assertThrows(OrderInvalidException.class, () -> userService.getAllFolowers(userId, orderDesc));
    }
}
