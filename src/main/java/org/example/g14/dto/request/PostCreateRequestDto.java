package org.example.g14.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.g14.dto.ProductDto;
import org.example.g14.utils.CustomLocalDateDeserializer;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateRequestDto {
    @JsonProperty("user_id")
    private int idUser;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate date;
    private ProductDto product;
    private int category;
    private double price;
}