package com.eatzip.repository;

import com.eatzip.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByRestaurant_Id(Long id);

    List<Offer> findByToDateBefore(LocalDateTime toDate);
}