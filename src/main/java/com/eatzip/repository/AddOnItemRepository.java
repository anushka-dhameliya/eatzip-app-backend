package com.eatzip.repository;

import com.eatzip.model.AddOnItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddOnItemRepository extends JpaRepository<AddOnItem, Long> {
    List<AddOnItem> findByRestaurant_Id(Long id);
}