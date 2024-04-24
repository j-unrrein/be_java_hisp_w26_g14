package org.example.g14.service;

import org.example.g14.dto.CreatePostDto;
import org.example.g14.dto.PostDto;
import org.example.g14.exception.NotFoundException;
import org.example.g14.model.Post;
import org.example.g14.model.User;
import org.example.g14.repository.IPostRepository;
import org.example.g14.repository.IUserRepository;
import org.example.g14.utils.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService implements IPostService {
    @Autowired
    IPostRepository postRepository;

    @Autowired
    IUserRepository userRepository;

    @Override
    public void add(CreatePostDto createPostDto) {
        PostMapper postMapper = new PostMapper();
        Post post = postMapper.createPostDtoToPost(createPostDto);

        Optional<User> usuario = userRepository.getById(post.getIdUser());
        if(usuario.isEmpty()) {
            throw new NotFoundException("No se encontró el usuario con id: " + post.getIdUser());
        }

        postRepository.save(post);
    }

    @Override
    public List<PostDto> getPostsFromFollowed(int userId) {
        User user = userRepository.getById(userId).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        List<Integer> followedVendors = user.getIdFollows();

        List<Post> allPosts = new ArrayList<>();
        for (Integer vendorId : followedVendors) {
            allPosts.addAll(postRepository.findAllByUser(vendorId));
        }

        LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);
        List<Post> recentPosts = allPosts.stream()
                .filter(post -> post.getDate().isAfter(twoWeeksAgo))
                .sorted(Comparator.comparing(Post::getDate).reversed()) // Orden descendente
                .toList();

        return recentPosts.stream()
                .map(post -> new PostDto(
                        post.getIdUser(),
                        post.getId(),
                        post.getDate(),
                        post.getProduct(),
                        post.getCategory(),
                        post.getPrice()
                ))
                .collect(Collectors.toList());
    }
}
