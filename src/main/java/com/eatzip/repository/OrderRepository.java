package com.eatzip.repository;


import com.eatzip.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer_Id(Long id);

    List<Order> findByRestaurant_Id(Long id);

    List<Order> findByCustomer_IdOrderByIdDesc(Long id);

    List<Order> findByRestaurant_IdOrderByIdDesc(Long id);
}