package com.olim.gura.order;


import com.olim.gura.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    ProductOrderService productOrderService;

    public boolean saveOrder(Order order) {
        order = orderRepository.save(order);
        return orderRepository.findById(order.getId()).isPresent();
    }
    public boolean deleteOrder(Long id){
        orderRepository.deleteById(id);
        return !orderRepository.findById(id).isPresent();
    }
    public Order getOrder(Long id)  {
        return orderRepository.findById(id).get();
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public boolean checkOutOrder(Order order) {
        order.setChecked(true);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(new Date());
        orderRepository.save(order);
        return orderRepository.existsById(order.getId());
    }
    public void updateStatus(Long orderId,String status){
        Order order = orderRepository.findById(orderId).get();
        switch (status.toLowerCase()) {
            case "delivered" -> order.setStatus(OrderStatus.DELIVERED);
            case "received" -> order.setStatus(OrderStatus.RECEIVED);
            default -> order.setStatus(OrderStatus.PENDING);
        }
        orderRepository.save(order);
    }
    public List<Order> getUserOrders(User user){
        return orderRepository.findAllByUser(user);
    }
    public List<Order> getCheckedOrders(User user){
        return orderRepository.findAllByUserAndCheckedIsTrue(user);
    }
    public List<Order> getUnCheckedOrders(User user){
        return orderRepository.findAllByUserAndCheckedIsFalse(user);
    }

    public void updateProductOrderQuantity(Long orderId, Long productOrderId, int quantity) {
        Order order = orderRepository.findById(orderId).get();
        ProductOrder productOrder = productOrderService.getProductOrder(order.getProductOrders().get(0).getId());
        productOrder.setQuantity(quantity);
        productOrderService.saveProductOrder(productOrder);
//        orderRepository.save(order);
    }

    public boolean productOrderExists(Long productId,Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        List<ProductOrder> productOrders = order.getProductOrders();
        return  productOrders.stream()
                .anyMatch(productOrder -> productOrder
                        .getProduct()
                        .getId()
                        .equals(productId));
    }

    public Order getOrderByUser(User user) {
        return  orderRepository.findByUserAndCheckedIsFalse(user).orElse(null);
    }

    public boolean removeProductOrder(Long id, Order order) {
        List<ProductOrder> productOrders = order.getProductOrders();
        Optional<ProductOrder> optionalProductOrder = productOrders.stream()
                .filter(productOrder -> productOrder
                .getProduct()
                        .getId()
                        .equals(id))
                .findFirst();
        if(!optionalProductOrder.isPresent()){
            return false;
        }
        productOrders.remove(optionalProductOrder.get());
        order.setProductOrders(productOrders);
        orderRepository.save(order);
        return true;
    }
    public List<Order> getAllCheckedOrders(){
        return orderRepository.findAllByCheckedIsTrue();
    }
}
