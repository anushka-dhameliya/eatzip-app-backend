package com.eatzip.repository;

import com.eatzip.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findByCustomer_Id(Long id);


}
