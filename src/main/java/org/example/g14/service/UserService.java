package org.example.g14.service;

import org.example.g14.exception.BadRequestException;
import org.example.g14.exception.NotFoundException;
import org.example.g14.model.User;
import org.example.g14.repository.IPostRepository;
import org.example.g14.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IPostRepository postRepository;


    @Override
    public void unfollowSeller(int followerUserId, int sellerUserId) {

        User followerUser = userRepository.getById(followerUserId)
            .orElseThrow(() -> new NotFoundException("No se encontró un Usuario con id=" + followerUserId));

        if (userRepository.getById(sellerUserId).isEmpty())
            throw new NotFoundException("No se encontró un Usuario con id=" + sellerUserId);

        // 'Integer.valueof' is needed because List.remove has an overload por a plain int parameter
        // that treats that parameter as an index in the List, not as the Object we are trying to remove.
        boolean wasFollowing = followerUser.getIdFollows().remove(Integer.valueOf(sellerUserId));

        if (!wasFollowing)
            throw new BadRequestException(
                "El Usuario con id=%d no sigue al Usuario con id=%d".formatted(followerUserId, sellerUserId)
            );

        userRepository.save(followerUser);
    }
}
