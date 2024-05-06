package org.example.g14.utils;

import lombok.NoArgsConstructor;
import org.example.g14.dto.ProductDto;
import org.example.g14.dto.request.PostCreateRequestDto;
import org.example.g14.dto.response.PostResponseDto;
import org.example.g14.exception.BadRequestException;
import org.example.g14.model.Post;
import org.example.g14.model.Product;

@NoArgsConstructor
public class PostMapper {
    public static Post createPostDtoToPost(PostCreateRequestDto postCreateRequestDto) {
        if(postCreateRequestDto.getDate() == null || postCreateRequestDto.getCategory() == 0 ||
            postCreateRequestDto.getPrice() == 0 || postCreateRequestDto.getIdUser() == 0) {
            throw new BadRequestException("Campos inválidos y/o faltantes.");
        }
        if(postCreateRequestDto.getProduct() == null ||
            postCreateRequestDto.getProduct().getId() == 0 || postCreateRequestDto.getProduct().getName() == null ||
            postCreateRequestDto.getProduct().getType() == null || postCreateRequestDto.getProduct().getBrand() == null ||
            postCreateRequestDto.getProduct().getColor() == null || postCreateRequestDto.getProduct().getNotes() == null
        ) {
            throw new BadRequestException("Campos inválidos y/o faltantes.");
        }

        return new Post(
            postCreateRequestDto.getDate(),
            postCreateRequestDto.getPrice(),
            postCreateRequestDto.getCategory(),
            new Product(
                postCreateRequestDto.getProduct().getId(),
                postCreateRequestDto.getProduct().getName(),
                postCreateRequestDto.getProduct().getType(),
                postCreateRequestDto.getProduct().getBrand(),
                postCreateRequestDto.getProduct().getColor(),
                postCreateRequestDto.getProduct().getNotes()
            ),
            postCreateRequestDto.getIdUser()
        );
    }

    public static PostResponseDto postToPostResponseDto(Post post){
        ProductDto productDto = new ProductDto();
        productDto.setId(post.getProduct().getId());
        productDto.setName(post.getProduct().getName());
        productDto.setType(post.getProduct().getType());
        productDto.setBrand(post.getProduct().getBrand());
        productDto.setColor(post.getProduct().getColor());
        productDto.setNotes(post.getProduct().getNotes());

        return new PostResponseDto(
                post.getIdUser(),
                post.getId(),
                post.getDate(),
                productDto,
                post.getCategory(),
                post.getPrice()
        );
    }
}
