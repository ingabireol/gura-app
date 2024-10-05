package com.olim.gura.product;

import com.olim.gura.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Transactional
    @Modifying
    @Query("update Product p set p.quantity = p.quantity - :quantity where p.id = :productId")
    int updateProductQuantity(@Param("productId") Long productId, @Param("quantity") int quantity);

    @Transactional
    @Modifying
    @Query("update Product p set p.quantity = p.quantity + :quantity where p.id = :productId")
    int addProductQuantity(@Param("productId") Long productId, @Param("quantity") int quantity);


    List<Product> findAllByUserAndAvailableIsTrue(User user);
    List<Product> findByCategoryContainingIgnoreCaseOrNameContainingIgnoreCase(String category,String name);
    List<Product> findAllByAvailableIsTrue();
}
