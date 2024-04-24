package org.example.g14.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.g14.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository implements IUserRepository{
    private List<User> listOfUsers;

    public UserRepository() throws IOException {
        //load list
        File file;
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> users;

        file = ResourceUtils.getFile("classpath:UsersDB.json");
        users = objectMapper.readValue(file, new TypeReference<List<User>>() {
        });

        listOfUsers = users;
    }

    @Override
    public List<User> getAll() {
        return listOfUsers;
    }

    @Override
    public Optional<User> getById(int id) {
        return listOfUsers
                .stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    @Override
    public void save(User user) {
        listOfUsers.add(user);
    }
}
