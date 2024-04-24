package org.example.g14.service;

import org.example.g14.dto.UserWithFollowersCountDto;

import org.example.g14.dto.UserFollowersDto;

public interface IUserService {

    public UserWithFollowersCountDto countFollowersBySeller(int id);
    UserFollowersDto getAllFolowers(int id);
}
