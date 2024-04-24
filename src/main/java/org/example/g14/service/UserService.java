package org.example.g14.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.g14.dto.UserDto;
import org.example.g14.dto.UserFollowedDto;
import org.example.g14.dto.UserFollowersDto;
import org.example.g14.dto.UserWithFollowersCountDto;
import org.example.g14.exception.BadRequestException;
import org.example.g14.exception.ConflictException;
import org.example.g14.exception.NotFoundException;
import org.example.g14.model.User;
import org.example.g14.repository.IPostRepository;
import org.example.g14.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    public User follow(int userId, int userIdToFollow) {
        Optional<User> userOptional = userRepository.getById(userId);
        Optional<User> userToFollowOptional = userRepository.getById(userIdToFollow);

        if (userOptional.isEmpty() || userToFollowOptional.isEmpty()) {
            throw new NotFoundException("No se encontró uno o ambos usuarios");
        }

        User user = userOptional.get();
        User userToFollow = userToFollowOptional.get();

        if(user.getIdFollows().contains(userIdToFollow)){
            throw new ConflictException("El usuario con id " + userId + " ya sigue al usuario con id " + userIdToFollow);
        }


        user.getIdFollows().add(userToFollow.getId());

        userRepository.save(user);
        return user;
    }

    private enum NameOrder{
        NAME_ASC,
        NAME_DESC
    }

    @Override
    public UserFollowedDto getListOfFollowedSellers(int userId, String order) {

        NameOrder orderEnum = null;

        if(order != null ){
            try {
                orderEnum = NameOrder.valueOf(order.toUpperCase());
            }
            catch (IllegalArgumentException e){
                throw new BadRequestException("Parametro de ordenamiento invalido");
            }
        }

        User user = getUserById(userId);
        UserFollowedDto usersDto = new UserFollowedDto();

        usersDto.setUser_id(user.getId());
        usersDto.setUser_name(user.getName());

        List<UserDto> listFollowed = new ArrayList<>();
        for(int followed : user.getIdFollows()){
            User foundUser = getUserById(followed);
            UserDto followedUserDto = new UserDto();
            followedUserDto.setUser_name(foundUser.getName());
            followedUserDto.setUser_id(foundUser.getId());
            listFollowed.add(followedUserDto);
        }

        if(orderEnum == NameOrder.NAME_ASC)
            listFollowed.sort(Comparator.comparing(UserDto::getUser_name));
        else if (orderEnum == NameOrder.NAME_DESC)
            listFollowed.sort(Comparator.comparing(UserDto::getUser_name).reversed());

        usersDto.setFollowed(listFollowed);

        return usersDto;
    }

    @Override
    public UserFollowersDto getAllFolowers(int id, String order) {
        NameOrder orderEnum = null;

        if(order != null ){
            try {
                orderEnum = NameOrder.valueOf(order.toUpperCase());
            }
            catch (IllegalArgumentException e){
                throw new BadRequestException("Parametro de ordenamiento invalido");
            }
        }

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

        if(orderEnum == NameOrder.NAME_ASC)
            userDtos.sort(Comparator.comparing(UserDto::getUser_name));
        else if (orderEnum == NameOrder.NAME_DESC)
            userDtos.sort(Comparator.comparing(UserDto::getUser_name).reversed());

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
}
