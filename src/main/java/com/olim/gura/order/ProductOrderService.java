package com.olim.gura.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductOrderService {
    @Autowired
    private ProductOrderRepo productOrderRepo;

    public ProductOrder saveProductOrder(ProductOrder productOrder) {
        return productOrderRepo.save(productOrder);
    }
    public boolean deleteProductOrder(Long id){
        productOrderRepo.deleteById(id);
        return !productOrderRepo.findById(id).isPresent();
    }
    public ProductOrder getProductOrder(Long id)  {
        return productOrderRepo.findById(id).get();
    }
    public List<ProductOrder> getAllProductOrders() {
        return productOrderRepo.findAll();
    }
}
