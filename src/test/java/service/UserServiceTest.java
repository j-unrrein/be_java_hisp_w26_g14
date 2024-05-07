package service;

import org.example.g14.dto.response.UserFollowedResponseDto;
import org.example.g14.dto.response.UserFollowersResponseDto;
import org.example.g14.dto.response.UserResponseDto;
import org.example.g14.model.Post;
import org.example.g14.model.Product;
import org.example.g14.model.User;
import org.example.g14.repository.IPostRepository;
import org.example.g14.repository.IUserRepository;
import org.example.g14.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import utils.UsersList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IPostRepository postRepository;

    @InjectMocks
    private UserService userService;


    @Test
    @DisplayName("US 0003 - NAME_DESC OK")
    public void getAllFolowersDescTest() {

        // arrange
        User foundUser = new User(1, "Juan", List.of(2, 3, 4), new ArrayList<>());
        Post post = new Post(LocalDate.now(), 2.0, 3, new Product(), 2);

        List<User> followers = UsersList.getMockedUsers();  // traer lista de usuarios de utils
        List<Post> sellerPosts = new ArrayList<>();
        sellerPosts.add(post);

        List<User> sortedFollowers = followers.stream().sorted(Comparator.comparing(User::getName).reversed()).toList();

        Mockito.when(userRepository.getById(1)).thenReturn(Optional.of(foundUser));
        Mockito.when(userRepository.getById(2)).thenReturn(Optional.of(followers.get(0)));
        Mockito.when(userRepository.getById(3)).thenReturn(Optional.of(followers.get(1)));
        Mockito.when(userRepository.getById(4)).thenReturn(Optional.of(followers.get(2)));

        Mockito.when(postRepository.findAllByUser(1)).thenReturn(sellerPosts);

        // act
        UserFollowersResponseDto obtainedUsers = userService.getAllFolowers(1, "name_desc");

        //assert
        Assertions.assertEquals(sortedFollowers.get(0).getName(), obtainedUsers.getFollowers().get(0).getUser_name());
        Assertions.assertEquals(sortedFollowers.get(1).getName(), obtainedUsers.getFollowers().get(1).getUser_name());
        Assertions.assertEquals(sortedFollowers.get(2).getName(), obtainedUsers.getFollowers().get(2).getUser_name());
    }

    @Test
    @DisplayName("US 0003 - NAME_ASC OK")
    public void getAllFolowersAscTest() {

        // arrange
        User foundUser = new User(1, "Juan", List.of(2, 3, 4), new ArrayList<>());
        Post post = new Post(LocalDate.now(), 2.0, 3, new Product(), 2);

        List<User> followers = UsersList.getMockedUsers();  // traer lista de usuarios de utils
        List<Post> sellerPosts = new ArrayList<>();
        sellerPosts.add(post);

        List<User> sortedFollowers = followers.stream().sorted(Comparator.comparing(User::getName)).toList();

        Mockito.when(userRepository.getById(1)).thenReturn(Optional.of(foundUser));
        Mockito.when(userRepository.getById(2)).thenReturn(Optional.of(followers.get(0)));
        Mockito.when(userRepository.getById(3)).thenReturn(Optional.of(followers.get(1)));
        Mockito.when(userRepository.getById(4)).thenReturn(Optional.of(followers.get(2)));

        Mockito.when(postRepository.findAllByUser(1)).thenReturn(sellerPosts);

        // act
        UserFollowersResponseDto obtainedUsers = userService.getAllFolowers(1, "name_asc");

        //assert
        Assertions.assertEquals(sortedFollowers.get(0).getName(), obtainedUsers.getFollowers().get(0).getUser_name());
        Assertions.assertEquals(sortedFollowers.get(1).getName(), obtainedUsers.getFollowers().get(1).getUser_name());
        Assertions.assertEquals(sortedFollowers.get(2).getName(), obtainedUsers.getFollowers().get(2).getUser_name());
    }

    @Test
    @DisplayName("US 0004 - NAME_DESC OK")
    public void getListOfFollowedSellersDescTest() {

        // arrange
        User foundUser = new User(1, "Juan", new ArrayList<>(), List.of(2, 3, 4));

        List<User> followeds = UsersList.getMockedUsers();  // traer lista de usuarios de utils

        List<User> sortedFollowers = followeds.stream().sorted(Comparator.comparing(User::getName).reversed()).toList();

        Mockito.when(userRepository.getById(1)).thenReturn(Optional.of(foundUser));
        Mockito.when(userRepository.getById(2)).thenReturn(Optional.of(followeds.get(0)));
        Mockito.when(userRepository.getById(3)).thenReturn(Optional.of(followeds.get(1)));
        Mockito.when(userRepository.getById(4)).thenReturn(Optional.of(followeds.get(2)));


        // act
        UserFollowedResponseDto obtainedUsers = userService.getListOfFollowedSellers(1, "name_desc");

        //assert
        Assertions.assertEquals(sortedFollowers.get(0).getName(), obtainedUsers.getFollowed().get(0).getUser_name());
        Assertions.assertEquals(sortedFollowers.get(1).getName(), obtainedUsers.getFollowed().get(1).getUser_name());
        Assertions.assertEquals(sortedFollowers.get(2).getName(), obtainedUsers.getFollowed().get(2).getUser_name());
    }
}
