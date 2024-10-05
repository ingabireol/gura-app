package com.olim.gura.dto;

import com.olim.gura.user.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductDto {
    private Long id;
    @NotEmpty
    @NotNull
    private String name;
    @NotEmpty
    @NotNull
    private String description ;
    @NotEmpty
    @NotNull
    private String category;
//    @NotEmpty
    @NotNull
    private Double price;
//    @NotEmpty
    @NotNull
    private int quantity;
    private boolean available =true;
//    @NotEmpty
    @NotNull
    private MultipartFile picture;
    private User user;

}
