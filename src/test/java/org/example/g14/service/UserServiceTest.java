package org.example.g14.service;

import org.example.g14.dto.response.UserFollowedResponseDto;
import org.example.g14.dto.response.UserFollowersResponseDto;
import org.example.g14.exception.OrderInvalidException;
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

import java.util.List;

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
