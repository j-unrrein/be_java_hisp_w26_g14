package org.example.g14.service;

import org.example.g14.dto.UserWithFollowersCountDto;

public interface IUserService {

    public UserWithFollowersCountDto countFollowersBySeller(int id);
}
