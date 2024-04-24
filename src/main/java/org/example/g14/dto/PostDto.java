package org.example.g14.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.g14.model.Product;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private int user_id;
    private int post_id;
    private LocalDate date;
    private Product product; // El DTO para el producto
    private int category;
    private double price;
}
