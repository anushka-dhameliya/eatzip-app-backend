package com.eatzip.repository;

import com.eatzip.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurant_Id(Long id);

    List<MenuItem> findByRestaurant_IdOrderByIdAsc(Long id);

    List<MenuItem> findByRestaurant_IdOrderByMenuCategory_IdAsc(Long id);

    List<MenuItem> findByNameLikeIgnoreCaseOrIsVegetarianOrMenuCategory_NameLikeIgnoreCase(String name, boolean isVegetarian, String name1);


}