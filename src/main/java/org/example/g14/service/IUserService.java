package org.example.g14.service;

import org.example.g14.dto.PostDto;

import java.util.List;

public interface IUserService {
    List<PostDto> getPostsFromFollowed(int userId);

}
