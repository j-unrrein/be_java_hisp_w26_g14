package org.example.g14.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.g14.dto.UserDto;
import org.example.g14.dto.UserFollowersDto;
import org.example.g14.exception.BadRequestException;
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
    public UserFollowersDto getAllFolowers(int id) {
        Optional<User> user = getUserById(id);

        if(user.get().getIdFollowers().size() == 0)
            throw new BadRequestException("No es un vendedor");

        UserFollowersDto userFollowersDto = new UserFollowersDto(user.get().getId(),
                                                                    user.get().getName(),
                                                                    new ArrayList<>());
        List<UserDto> userDtos = new ArrayList<>();

        for (Integer idUser : user.get().getIdFollowers()) {
            UserDto userDto = transferToUserDto(getUserById(idUser).get());
            userDtos.add(userDto);
        }

        userFollowersDto.setFollowers(userDtos);

        return userFollowersDto;
    }
    public UserDto transferToUserDto(User user){
       return new UserDto(user.getId(), user.getName());
    }
    public Optional<User> getUserById(int id){
        Optional<User> user = userRepository.getById(id);
        if(user.isEmpty())
            throw new NotFoundException("No se encontro el usuario");
        return user;
    }

}
