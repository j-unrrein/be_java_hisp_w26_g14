package org.example.g14.service;

import org.example.g14.dto.response.UserFollowedResponseDto;
import org.example.g14.dto.response.UserFollowersCountResponseDto;
import org.example.g14.dto.response.UserFollowersResponseDto;
import org.example.g14.dto.response.UserResponseDto;
import org.example.g14.exception.NotSellerException;
import org.example.g14.exception.OrderInvalidException;
import org.example.g14.exception.UserNotFoundException;
import org.example.g14.model.Post;
import org.example.g14.model.Product;
import org.example.g14.model.User;
import org.example.g14.repository.IUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.example.g14.repository.IPostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.UsersList;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IPostRepository postRepository;

    @InjectMocks
    private UserService userService;


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

    @Test
    @DisplayName("Tanto el Usuario que sigue como el Usuario a dejar de seguir existen")
    void unfollowSellerOk() {

        int followerUserId = 1;
        int followedUserId = 2;

        User followerUser = User.builder()
            .id(followerUserId)
            .idFollows(new ArrayList<>(List.of(followedUserId)))
            .build();

        User followedUser = User.builder()
            .id(followedUserId)
            .idFollowers(new ArrayList<>(List.of(followerUserId)))
            .build();

        UserFollowedResponseDto expectedResult = UserFollowedResponseDto.builder()
            .user_id(followerUserId)
            .followed(List.of())
            .build();

        when(userRepository.getById(followerUserId)).thenReturn(Optional.of(followerUser));
        when(userRepository.getById(followedUserId)).thenReturn(Optional.of(followedUser));

        UserFollowedResponseDto actualResult = userService.unfollowSeller(followerUserId, followedUserId);

        verify(userRepository, times(1)).save(followerUser);
        verify(userRepository, times(1)).save(followedUser);

        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(followerUser.getIdFollows()).isEmpty();
        assertThat(followedUser.getIdFollowers()).isEmpty();
    }

    @Test
    @DisplayName("El Usuario que sigue existe, pero el Usuario a dejar de seguir no existe")
    void unfollowSellerFollowedUserDoesntExist() {

        int followerUserId = 1;
        int followedUserId = 2;

        User followerUser = User.builder().id(followerUserId).build();

        when(userRepository.getById(followerUserId)).thenReturn(Optional.of(followerUser));
        when(userRepository.getById(followedUserId)).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> userService.unfollowSeller(followerUserId, followedUserId));

        verify(userRepository, never()).save(any());

        assertThat(thrown)
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining(Integer.toString(followedUserId));
    }

    @Test
    @DisplayName("US 0003 - NAME_DESC OK")
    public void getAllFolowersDescTest() {

        // arrange
        User foundUser = new User(1, "Juan", List.of(2, 3, 4), new ArrayList<>());
        Post post = new Post(LocalDate.now(), 2.0, 3, new Product(), 2);

        List<User> followers = UsersList.getMockedUsers();
        List<Post> sellerPosts = new ArrayList<>();
        sellerPosts.add(post);
        List<User> sortedFollowers = followers.stream().sorted(Comparator.comparing(User::getName).reversed()).toList();

        stablishMocks(foundUser, followers);

        Mockito.when(postRepository.findAllByUser(1)).thenReturn(sellerPosts);

        // act
        UserFollowersResponseDto obtainedUsers = userService.getAllFolowers(1, "name_desc");

        //assert
        assertionsFeature0003(sortedFollowers, obtainedUsers);
    }

    @Test
    @DisplayName("US 0003 - NAME_ASC OK")
    public void getAllFolowersAscTest() {

        // arrange
        User foundUser = new User(1, "Juan", List.of(2, 3, 4), new ArrayList<>());
        Post post = new Post(LocalDate.now(), 2.0, 3, new Product(), 2);

        List<User> followers = UsersList.getMockedUsers();
        List<Post> sellerPosts = new ArrayList<>();
        sellerPosts.add(post);
        List<User> sortedFollowers = followers.stream().sorted(Comparator.comparing(User::getName)).toList();

        stablishMocks(foundUser, followers);

        Mockito.when(postRepository.findAllByUser(1)).thenReturn(sellerPosts);

        // act
        UserFollowersResponseDto obtainedUsers = userService.getAllFolowers(1, "name_asc");

        //assert
        assertionsFeature0003(sortedFollowers, obtainedUsers);
    }

    @Test
    @DisplayName("US 0004 - NAME_DESC OK")
    public void getListOfFollowedSellersDescTest() {

        // arrange
        User foundUser = new User(1, "Juan", new ArrayList<>(), List.of(2, 3, 4));

        List<User> followeds = UsersList.getMockedUsers();
        List<User> sortedFolloweds = followeds.stream().sorted(Comparator.comparing(User::getName).reversed()).toList();

        stablishMocks(foundUser, followeds);

        // act
        UserFollowedResponseDto obtainedUsers = userService.getListOfFollowedSellers(1, "name_desc");

        //assert
        assertionsFeature0004(sortedFolloweds, obtainedUsers);
    }

    @Test
    @DisplayName("US 0004 - NAME_ASC OK")
    public void getListOfFollowedSellersAscTest() {

        // arrange
        User foundUser = new User(1, "Juan", new ArrayList<>(), List.of(2, 3, 4));

        List<User> followeds = UsersList.getMockedUsers();
        List<User> sortedFolloweds = followeds.stream().sorted(Comparator.comparing(User::getName)).toList();

        stablishMocks(foundUser, followeds);

        // act
        UserFollowedResponseDto obtainedUsers = userService.getListOfFollowedSellers(1, "name_asc");

        //assert
        assertionsFeature0004(sortedFolloweds, obtainedUsers);
    }

    private void stablishMocks(User foundUser, List<User> relatedUsers){
        Mockito.when(userRepository.getById(1)).thenReturn(Optional.of(foundUser));
        for (User user : relatedUsers) {
            Mockito.when(userRepository.getById(user.getId())).thenReturn(Optional.of(user));
        }
    }

    private void assertionsFeature0003(List<User> sortedFollowers, UserFollowersResponseDto obtainedUsers){
        for (int index = 0; index < sortedFollowers.size(); index++) {
            Assertions.assertEquals(sortedFollowers.get(index).getName(),
                    obtainedUsers.getFollowers().get(index).getUser_name());
        }
    }

    private void assertionsFeature0004(List<User> sortedFolloweds, UserFollowedResponseDto obtainedUsers){
        for (int index = 0; index < sortedFolloweds.size(); index++) {
            Assertions.assertEquals(sortedFolloweds.get(index).getName(),
                    obtainedUsers.getFollowed().get(index).getUser_name());
        }
    }
}
