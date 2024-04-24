package org.example.g14.service;

import org.example.g14.dto.PostDto;
import org.example.g14.exception.NotFoundException;
import org.example.g14.model.Post;
import org.example.g14.model.User;
import org.example.g14.repository.IPostRepository;
import org.example.g14.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService{
    @Autowired
    IUserRepository userRepository;

    @Autowired
    IPostRepository postRepository;

    @Override
    public List<PostDto> getPostsFromFollowed(int userId) {
        // Obtener al usuario por ID
        User user = userRepository.getById(userId).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Obtener la lista de vendedores a los que sigue
        List<Integer> followedVendors = user.getIdFollows();

        // Obtener las publicaciones de los vendedores seguidos
        List<Post> allPosts = new ArrayList<>();
        for (Integer vendorId : followedVendors) {
            allPosts.addAll(postRepository.findAllByUser(vendorId));
        }

        // Filtrar publicaciones de las últimas dos semanas
        LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);
        List<Post> recentPosts = allPosts.stream()
                .filter(post -> post.getDate().isAfter(twoWeeksAgo))
                .sorted(Comparator.comparing(Post::getDate).reversed()) // Orden descendente
                .toList();

        // Convertir a DTO para la respuesta
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
