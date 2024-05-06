package org.example.g14.service;

import org.example.g14.dto.request.PostCreateRequestDto;
import org.example.g14.dto.response.PostResponseDto;

import java.util.List;

public interface IPostService {
    void add(PostCreateRequestDto postCreateRequestDto);
    List<PostResponseDto> getPostsFromFollowed(int userId, String order);

}
