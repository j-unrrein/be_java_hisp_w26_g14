package org.example.g14.service;

import org.example.g14.model.User;
import org.example.g14.dto.UserWithFollowersCountDto;
import org.example.g14.dto.UserFollowersDto;

public interface IUserService {
    User follow(int userId, int userIdToFollow);
    public UserWithFollowersCountDto countFollowersBySeller(int id);
    UserFollowersDto getAllFolowers(int id);
}
