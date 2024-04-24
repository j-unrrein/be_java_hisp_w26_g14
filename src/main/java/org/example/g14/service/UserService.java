package org.example.g14.service;

import org.example.g14.exception.BadRequestException;
import org.example.g14.exception.NotFoundException;
import org.example.g14.model.User;
import org.example.g14.repository.IPostRepository;
import org.example.g14.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements IUserService{
    @Autowired
    IUserRepository userRepository;

    @Autowired
    IPostRepository postRepository;

    @Override
    public User follow(int userId, int userIdToFollow) {
        Optional<User> userOptional = userRepository.getById(userId);
        Optional<User> userToFollowOptional = userRepository.getById(userIdToFollow);

        if (userOptional.isEmpty() || userToFollowOptional.isEmpty()) {
            throw new NotFoundException("No se encontró uno o ambos usuarios");
        }

        User user = userOptional.get();
        User userToFollow = userToFollowOptional.get();

        if(user.getIdFollows().contains(userIdToFollow)){
            throw new BadRequestException("El usuario con id " + userId + " ya sigue al usuario con id " + userIdToFollow);
        }


        user.getIdFollows().add(userToFollow.getId());

        userRepository.save(user);
        return user;
    }
}
