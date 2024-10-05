package com.olim.gura.order;

import com.olim.gura.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findAllByUser(User user);


    Optional<Order> findByUserAndCheckedIsFalse(User user);

    List<Order> findAllByUserAndCheckedIsTrue(User user);

    List<Order> findAllByUserAndCheckedIsFalse(User user);
    List<Order> findAllByCheckedIsTrue();
}
