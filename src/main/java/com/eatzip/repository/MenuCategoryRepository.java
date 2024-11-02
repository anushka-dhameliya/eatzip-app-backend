package com.eatzip.repository;

import com.eatzip.model.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    List<MenuCategory> findByRestaurant_Id(Long id);

    List<MenuCategory> findByRestaurant_IdOrderByIdAsc(Long id);


}