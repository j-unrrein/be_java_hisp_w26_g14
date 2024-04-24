package org.example.g14.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.g14.dto.UserWithFollowersCountDto;
import org.example.g14.exception.NotFoundException;
import org.example.g14.model.User;
import org.example.g14.dto.UserDto;
import org.example.g14.dto.UserFollowersDto;
import org.example.g14.exception.BadRequestException;
import org.example.g14.dto.UserFollowedDto;
import org.example.g14.repository.IPostRepository;
import org.example.g14.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService{
    @Autowired
    IUserRepository userRepository;

    @Autowired
    IPostRepository postRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public UserWithFollowersCountDto countFollowersBySeller(int id) {
        User user = getUserById(id);
        return new UserWithFollowersCountDto(
                user.getId(),
                user.getName(),
                (int) user.getIdFollowers().stream().count());
    }
    @Override
    public UserFollowersDto getAllFolowers(int id) {
        User user = getUserById(id);

        if(user.getIdFollowers().size() == 0)
            throw new BadRequestException("No es un vendedor");

        UserFollowersDto userFollowersDto = new UserFollowersDto(user.getId(),
                                                                    user.getName(),
                                                                    new ArrayList<>());
        List<UserDto> userDtos = new ArrayList<>();

        for (Integer idUser : user.getIdFollowers()) {
            UserDto userDto = transferToUserDto(getUserById(idUser));
            userDtos.add(userDto);
        }

        userFollowersDto.setFollowers(userDtos);

        return userFollowersDto;
    }
    public UserDto transferToUserDto(User user){
       return new UserDto(user.getId(), user.getName());
    }
    public User getUserById(int id){
        Optional<User> user = userRepository.getById(id);
        if(user.isEmpty())
            throw new NotFoundException("No se encontro el usuario");
        return user.get();
    }

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
