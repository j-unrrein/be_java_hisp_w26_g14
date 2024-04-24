package org.example.g14.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.g14.dto.UserWithFollowersCountDto;
import org.example.g14.exception.NotFoundException;
import org.example.g14.model.User;
import org.example.g14.repository.IPostRepository;
import org.example.g14.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Optional<User> userOptional = userRepository.getById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserWithFollowersCountDto(
                    user.getId(),
                    user.getName(),
                    (int) user.getIdFollowers().stream().count());
        } else {
            throw new NotFoundException("Usuario no encontrado para el ID proporcionado: " + id);
        }
    }
}
