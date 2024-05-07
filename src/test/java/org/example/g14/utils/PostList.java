package org.example.g14.utils;

import org.example.g14.model.Post;
import org.example.g14.model.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostList {
    public static List<Post> getPostResponse(){
        Product play = new Product();
        play.setId(1);
        play.setName("play station");
        play.setType("Videojuego");
        play.setBrand("PS");
        play.setColor("negro");
        play.setNotes("");

        Post playStation = new Post(
                LocalDate.of(2024, 05, 05),
                22,
                1,
                play,
                1
        );

        Product xbox = new Product();
        xbox.setId(2);
        xbox.setName("xbox");
        xbox.setType("Videojuego");
        xbox.setBrand("PS");
        xbox.setColor("blanco");
        xbox.setNotes("");

        Post xboxSeries = new Post(
                LocalDate.of(2024, 05, 01),
                22,
                1,
                xbox,
                1
        );

        Product pc = new Product();
        pc.setId(3);
        pc.setName("pc");
        pc.setType("Videojuego");
        pc.setBrand("PS");
        pc.setColor("blanco");
        pc.setNotes("");

        Post pcPost = new Post(
                LocalDate.of(2024, 05, 03),
                22,
                1,
                xbox,
                1
        );

        List<Post> posts = new ArrayList<>();
        posts.add(playStation);
        posts.add(xboxSeries);
        posts.add(pcPost);

        return posts;
    }
}
