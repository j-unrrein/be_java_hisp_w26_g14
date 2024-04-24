package org.example.g14.service;

import org.example.g14.dto.UserFollowedDto;

public interface IUserService {
    UserFollowedDto listOfFollowedSellers(int userId);
}
