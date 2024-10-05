package com.olim.gura.product;

import com.olim.gura.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private String description;
    private Double price;
    private int quantity;
    private boolean available = true;
    @Column(columnDefinition = "TEXT")
    private String picture;
    @ManyToOne
    private User user;

}
