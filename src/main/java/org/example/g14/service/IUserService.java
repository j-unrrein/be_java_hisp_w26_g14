package org.example.g14.service;

import org.example.g14.dto.UserFollowedDto;

import org.example.g14.model.User;
import org.example.g14.dto.UserWithFollowersCountDto;

import org.example.g14.dto.UserFollowersDto;

public interface IUserService {
    UserFollowedDto getListOfFollowedSellers(int userId, String order);
    User follow(int userId, int userIdToFollow);
    public UserWithFollowersCountDto countFollowersBySeller(int id);
    UserFollowersDto getAllFolowers(int id, String order);
}
