package org.example.g14.service;

import org.example.g14.dto.UserFollowedDto;

import org.example.g14.dto.UserWithFollowersCountDto;

import org.example.g14.dto.UserFollowersDto;

public interface IUserService {
    UserFollowedDto listOfFollowedSellers(int userId);

    public UserWithFollowersCountDto countFollowersBySeller(int id);
    UserFollowersDto getAllFolowers(int id);
}
