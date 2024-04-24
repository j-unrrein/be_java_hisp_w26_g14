package org.example.g14.service;

import org.example.g14.model.User;

public interface IUserService {
    User follow(int userId, int userIdToFollow);
}
