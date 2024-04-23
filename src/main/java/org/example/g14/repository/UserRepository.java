package org.example.g14.repository;

import org.example.g14.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository implements IUserRepository{
    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public Optional<User> getById(int id) {
        return Optional.empty();
    }

    @Override
    public void save(User user) {

    }
}
