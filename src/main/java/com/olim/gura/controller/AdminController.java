package com.olim.gura.controller;


import com.olim.gura.dto.ProductDto;
import com.olim.gura.order.Order;
import com.olim.gura.order.OrderService;
import com.olim.gura.product.Product;
import com.olim.gura.product.ProductService;
import com.olim.gura.user.UserService;
import jakarta.jws.WebParam;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;

    @GetMapping("/home")
    public String showHomePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Product> productList = productService.getAllUserProducts(userService.findUserByEmail(userDetails.getUsername()).getId());
        model.addAttribute("productList", productList);
        model.addAttribute("welcomeMessage", "Welcome to our online store! Explore our exciting range of products.");
        return "adminHome";
    }

    @GetMapping("/product-delete/{id}")
    public String deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return "redirect:/admin/home";
    }
    @GetMapping("/product-details/{id}")
    public String getProductDetails(Model model, @PathVariable Long id){
        Product product = productService.getProduct(id);
        model.addAttribute("product",product);
        return "productDetails";
    }
    @GetMapping("/update-status")
    public String updateStatus(@RequestParam Long orderId, @RequestParam String status) {
        orderService.updateStatus(orderId, status);
        return "redirect:/admin/orders-all";
    }

    @GetMapping("/product-register")
    public String getProductRegistrationPage(Model model){
        model.addAttribute("product",new ProductDto());
        return "productForm";
    }
    @PostMapping("/product-register")
    public String saveProduct(@Valid @ModelAttribute("productDto") ProductDto productDto, @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        MultipartFile pictureFile = productDto.getPicture();
//        byte[] pictureData = pictureFile.getBytes();

        productDto.setUser(userService.findUserByEmail(userDetails.getUsername()));
        productService.saveProduct(productDto);
        return "redirect:/admin/product-all";
    }
    @GetMapping("/product-update/{id}")
    public String updateProduct(@PathVariable Long id, Model model,@AuthenticationPrincipal UserDetails userDetails){
        Product product = productService.getProduct(id);
        ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productDto.setName(product.getName());
        productDto.setCategory(product.getCategory());
        productDto.setPicture(productDto.getPicture());
        productDto.setDescription(productDto.getDescription());
        productDto.setUser(userService.findUserByEmail(userDetails.getUsername()));
        model.addAttribute("product",productDto);
        return "updateProduct";

    }
    @GetMapping("/product-all")
    public String getAllProducts(Model model, @AuthenticationPrincipal UserDetails userDetails){
        model.addAttribute("productList",productService.getAllUserProducts(userService.
                findUserByEmail(userDetails.getUsername())
                .getId()));
        return "allProducts";

    }
    @GetMapping("/orders-all")
    public String getAllOrders(@AuthenticationPrincipal UserDetails userDetails,Model model){
        List<Order> orders = orderService.getAllCheckedOrders();
        model.addAttribute("orders",orders);
        return "adminOrders";

    }
}
