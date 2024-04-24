package org.example.g14.service;

import org.example.g14.dto.UserFollowersDto;

import java.util.List;

public interface IUserService {
    UserFollowersDto getAllFolowers(int id);
}
