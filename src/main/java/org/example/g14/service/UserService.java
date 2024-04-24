package org.example.g14.service;

import org.example.g14.dto.UserDto;
import org.example.g14.dto.UserFollowedDto;
import org.example.g14.exception.NotFoundException;
import org.example.g14.model.User;
import org.example.g14.repository.IPostRepository;
import org.example.g14.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService{
    @Autowired
    IUserRepository userRepository;

    @Autowired
    IPostRepository postRepository;

    @Override
    public UserFollowedDto listOfFollowedSellers(int userId) {
        Optional<User> user = userRepository.getById(userId);
        if (user.isEmpty()) throw new NotFoundException("No existe usuario con ese id");

        UserFollowedDto usersDto = new UserFollowedDto();

        usersDto.setUser_id(user.get().getId());
        usersDto.setUser_name(user.get().getName());

        List<UserDto> listFollowed = new ArrayList<>();
        for(int followed : user.get().getIdFollows()){
            Optional<User> findedUser = userRepository.getById(followed);

            if(findedUser.isEmpty()) continue;

            UserDto followedUserDto = new UserDto();
            followedUserDto.setUser_name(findedUser.get().getName());
            followedUserDto.setUser_id(findedUser.get().getId());
            listFollowed.add(followedUserDto);
        }
        usersDto.setFollowed(listFollowed);

        return usersDto;
    }
}
