package org.example.g14.service;

import org.example.g14.dto.UserFollowedDto;

import java.util.List;

public interface IUserService {
    List<UserFollowedDto> listOfFollowersOfASellers(int userId);
}
