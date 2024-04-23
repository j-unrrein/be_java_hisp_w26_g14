package org.example.g14.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.g14.model.Post;
import org.example.g14.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Repository
public class PostRepository implements IPostRepository{
    private List<Post> listOfPosts;
    public PostRepository() throws IOException {
        //load list
        File file;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        List<Post> posts;

        file = ResourceUtils.getFile("classpath:PostsDB.json");
        posts = objectMapper.readValue(file, new TypeReference<List<Post>>() {
        });

        listOfPosts = posts;
    }

    @Override
    public List<Post> findAllByUser(int idUser) {
        return null;
    }

    @Override
    public void save(Post post) {

    }
}
