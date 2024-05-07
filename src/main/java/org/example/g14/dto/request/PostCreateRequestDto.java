package org.example.g14.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.g14.dto.ProductDto;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateRequestDto {

    @JsonProperty("user_id")
    private int idUser;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    private ProductDto product;

    private int category;

    private double price;
}
