package org.example.g14.service;

import org.example.g14.dto.UserFollowedDto;

import org.example.g14.model.User;
import org.example.g14.dto.UserWithFollowersCountDto;

import org.example.g14.dto.UserFollowersDto;


public interface IUserService {
    UserFollowedDto getListOfFollowedSellers(int userId);
    User follow(int userId, int userIdToFollow);
    void unfollowSeller(int followerUserId, int sellerUserId);
    UserWithFollowersCountDto countFollowersBySeller(int id);
    UserFollowersDto getAllFolowers(int id);

}
