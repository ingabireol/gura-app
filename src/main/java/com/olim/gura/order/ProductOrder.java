package com.olim.gura.order;


import com.olim.gura.product.Product;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Product product;
    private int quantity;
}
