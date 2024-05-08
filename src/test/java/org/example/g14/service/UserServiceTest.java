package org.example.g14.service;

import org.example.g14.dto.response.UserFollowedResponseDto;
import org.example.g14.exception.UserNotFoundException;
import org.example.g14.model.User;
import org.example.g14.repository.IUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    IUserRepository userRepository;

    @InjectMocks
    private UserService userService;


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
}