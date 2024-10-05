package com.olim.gura.controller;

import com.olim.gura.order.Order;
import com.olim.gura.order.OrderService;
import com.olim.gura.order.ProductOrder;
import com.olim.gura.order.ProductOrderService;
import com.olim.gura.product.Product;
import com.olim.gura.product.ProductService;
import com.olim.gura.user.User;
import com.olim.gura.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @Autowired
    ProductOrderService productOrderService;

    @GetMapping("/home")
    public String getUserHome(Model model){
        List<Product> recentProducts = productService.getAllProducts();
        model.addAttribute("recentProducts",recentProducts);
        model.addAttribute("category",null);
        return "userHome";
    }
    @PostMapping("/filter-products")
    public String filterProducts(@RequestParam String category,Model model){
        if(category.equalsIgnoreCase("all")){
            List<Product> products = productService.getAllProducts();
            model.addAttribute("recentProducts",products);
            model.addAttribute("category",null);
            return "userHome";
        }
        List<Product> products = productService.searchProducts(category);
        model.addAttribute("recentProducts",products);
        model.addAttribute("category",category);
        return "userHome";
    }

    @GetMapping("/product-addToCart")
    public String addProductToCart(@RequestParam("id") Long id,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   RedirectAttributes redirectAttributes,
                                   HttpServletResponse response){
        User user = userService.findUserByEmail(userDetails.getUsername());
        Order order = orderService.getOrderByUser(user);

        if(order!=null) {
            if (orderService.productOrderExists(id,order.getId())) {
                redirectAttributes.addFlashAttribute("message", "Product Already exists in the cart");
//                response.set
                return "redirect:/user/home";
            }
        }

        ProductOrder productOrder = new ProductOrder();
        productOrder.setProduct(productService.getProduct(id));
        productOrder.setQuantity(1);
        productOrder = productOrderService.saveProductOrder(productOrder);
        if(order == null){
            order = new Order();
            order.setProductOrders(List.of(productOrder));
            order.setUser(user);
            if(orderService.saveOrder(order)) {
                redirectAttributes.addFlashAttribute("success","Order saved successfully");
            }
            else {
                redirectAttributes.addFlashAttribute("error","Order not saved");
            }
            return "redirect:/user/home";
        }

        List<ProductOrder> existingProductOrders = order.getProductOrders();
        existingProductOrders.add(productOrder);
        order.setProductOrders(existingProductOrders);

        if(orderService.saveOrder(order)) {
            redirectAttributes.addFlashAttribute("success","Product added successfully");
        }
        else {
            redirectAttributes.addFlashAttribute("error","Product was not added");
        }
        return "redirect:/user/home";
    }

//    @PostMapping("/save")
//    public ResponseEntity<String> saveOrder(@RequestBody Order order){
//        if(order == null){
//            return ResponseEntity.badRequest().body("Order Must not be null");
//        }
//        List<ProductOrder> productOrders = order.getProductOrders();
//        if(productOrders == null){
//            return ResponseEntity.badRequest().body("Product orders Must not be null");
//        }
//
//        for(ProductOrder productOrder:productOrders){
//            if(productService.productExist(productOrder.getProduct().getId())){
//                productOrder.setProduct(productService.getProduct(productOrder.getProduct().getId()));
//            }
//            else {
//                return ResponseEntity.status(410).body("Product "+productOrder.getProduct().getId()+" Does not exist");
//            }
//            System.out.println("product id:"+productOrder.getProduct().getId()+"product wanted: "+productOrder.getQuantity()+"while product available :"+productOrder.getProduct().getQuantity());
//            if(productOrder.getQuantity() > productOrder.getProduct().getQuantity()){
//                return ResponseEntity.status(410).body("Product "+productOrder.getProduct().getName()+" is  not above available");
//            }
//            productOrderService.saveProductOrder(productOrder);
//        }
//
//        if(orderService.saveOrder(order)){
//            return ResponseEntity.ok("Order saved Successfully");
//        }
//        return ResponseEntity.ok("Order Not Saved");
//    }
    @GetMapping("/carts")
    public String getUserCarts(@AuthenticationPrincipal UserDetails userDetails,Model model){
        User user = userService.findUserByEmail(userDetails.getUsername());
        Order order = orderService.getOrderByUser(user);
        model.addAttribute("username",user.getName());
//        model.addAttribute("orderIsNull",)
        model.addAttribute("order",order);
        return "userCarts";
    }
    @PostMapping("/updateQuantity")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateQuantity(@RequestParam Long orderId, @RequestParam Long productOrderId, @RequestParam int quantity) {
        Map<String, Object> response = new HashMap<>();
        try {
            orderService.updateProductOrderQuantity(orderId, productOrderId, quantity);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/product-checkout/{id}")
    public String checkOutOrder(@PathVariable Long id, RedirectAttributes redirectAttributes){
        Order order = orderService.getOrder(id);
        List<ProductOrder> productOrders = order.getProductOrders();
//        for(ProductOrder productOrder:productOrders) {
//            productService.updateQuantity(productOrder.getProduct().getId(),productOrder.getQuantity());
//        }
        String message = "Check out failed";
        if(orderService.checkOutOrder(order)){

            message = "Product Checked out successfully";
            redirectAttributes.addFlashAttribute("success",message);

        }else{
            redirectAttributes.addFlashAttribute("error",message);
        }
        return "redirect:/user/carts";
    }
    @GetMapping("/remove-product/{id}")
    public String removeProductFromCart(@PathVariable Long id,@AuthenticationPrincipal UserDetails userDetails,RedirectAttributes redirectAttributes){
        User user = userService.findUserByEmail(userDetails.getUsername());
        Order order = orderService.getOrderByUser(user);
        if(orderService.removeProductOrder(id,order)){
            redirectAttributes.addFlashAttribute("success","Removed successfully");
        }
        else {
            redirectAttributes.addFlashAttribute("error", "product not removed");
        }
        return "redirect:/user/carts";
    }
    @GetMapping("/orders")
    public String getAllOrders(@AuthenticationPrincipal UserDetails userDetails,Model model){
        User user = userService.findUserByEmail(userDetails.getUsername());
        List<Order> orders = orderService.getCheckedOrders(user);
        model.addAttribute("orders",orders);
        return "userOrders";
    }
    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        if(StringUtils.isEmpty(query)){
            model.addAttribute("products", new ArrayList<>());
            return "searchResults";
        }
        List<Product> products = productService.searchProducts(query);
        model.addAttribute("products", products);
        return "searchResults";
    }


}
