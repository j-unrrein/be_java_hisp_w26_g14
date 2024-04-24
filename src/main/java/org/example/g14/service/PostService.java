package org.example.g14.service;

import org.example.g14.dto.CreatePostDto;
import org.example.g14.exception.NotFoundException;
import org.example.g14.model.Post;
import org.example.g14.model.User;
import org.example.g14.repository.IPostRepository;
import org.example.g14.repository.IUserRepository;
import org.example.g14.utils.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService implements IPostService {
    @Autowired
    IPostRepository postRepository;

    @Autowired
    IUserRepository userRepository;

    @Override
    public void add(CreatePostDto createPostDto) {
        PostMapper postMapper = new PostMapper();
        Post post = postMapper.createPostDtoToPost(createPostDto);

        Optional<User> usuario = userRepository.getById(post.getIdUser());
        if(usuario.isEmpty()) {
            throw new NotFoundException("No se encontró el usuario con id: " + post.getIdUser());
        }

        postRepository.save(post);
    }
}
