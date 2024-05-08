package org.example.g14.service;

import org.example.g14.dto.response.UserFollowedResponseDto;
import org.example.g14.dto.response.UserFollowersResponseDto;
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
import utils.UsersList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
