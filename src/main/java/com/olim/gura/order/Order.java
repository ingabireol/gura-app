package com.olim.gura.order;

import com.olim.gura.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_date")
    private Date orderDate = new Date();
    @ManyToOne
    private User user;
    @OneToMany
    @Column(name = "product_orders")
    private List<ProductOrder> productOrders;
    private boolean checked;
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;
}
