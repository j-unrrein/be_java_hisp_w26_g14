package org.example.g14.service;

import org.example.g14.dto.UserFollowedDto;

import org.example.g14.model.User;
import org.example.g14.dto.UserWithFollowersCountDto;

import org.example.g14.dto.UserFollowersDto;

import org.example.g14.dto.PostDto;

import java.util.List;

public interface IUserService {
    UserFollowedDto getListOfFollowedSellers(int userId);
    User follow(int userId, int userIdToFollow);
    public UserWithFollowersCountDto countFollowersBySeller(int id);
    UserFollowersDto getAllFolowers(int id);

}
