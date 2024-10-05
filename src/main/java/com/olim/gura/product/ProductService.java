package com.olim.gura.product;

import com.olim.gura.dto.ProductDto;
import com.olim.gura.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageUpload imageUpload;

    public boolean saveProduct(ProductDto productDto) {

        try {
            Product product = new Product();
            product.setDescription(productDto.getDescription());
            product.setUser(productDto.getUser());
            product.setName(productDto.getName());
            product.setAvailable(productDto.isAvailable());
            product.setUser(productDto.getUser());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setQuantity(productDto.getQuantity());
            if (productDto.getPicture() == null) {
                product.setPicture(null);
            }
            else {
//                imageUpload.uploadImage(productDto.getPicture());
                product.setPicture(productDto.getPicture().getOriginalFilename());
            }
            product = productRepository.save(product);
            return productRepository.findById(product.getId()).isPresent();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteProduct(Long id){

        Product product = productRepository.findById(id).get();
        product.setAvailable(false);
        productRepository.save(product);
        return !productRepository.findById(id).isPresent();
    }
    public Product getProduct(Long id)  {
        return productRepository.findById(id).get();
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public List<Product> getAllUserProducts(Long id) {
        return productRepository.findAllByUserAndAvailableIsTrue(userRepository.findById(id).get());
    }
    public boolean updateQuantity(Long id,int Quantity){
        return productRepository.updateProductQuantity(id,Quantity) > 0;
    }
    public boolean addProductQuantity(Long id,int Quantity){
        return productRepository.addProductQuantity(id,Quantity) > 0;
    }
    public boolean productExist(Long id){
        return productRepository.existsById(id);
    }
    public List<Product> searchProducts(String query){
        return productRepository.findByCategoryContainingIgnoreCaseOrNameContainingIgnoreCase(query,query);
    }
}
