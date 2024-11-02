package com.eatzip.controller.customer;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Offer;
import com.eatzip.service.offer.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offer")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @GetMapping("/{id}")
    public ResponseEntity<Offer> getOfferById(@PathVariable Long id,
                                              @RequestHeader("Authorization") String jwtToken) throws CustomException {
        Offer offer = offerService.getOfferById(id);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Offer>> getOffersForRestaurant(@PathVariable Long restaurantId,
                                                              @RequestHeader("Authorization") String jwtToken) throws CustomException {
        List<Offer> offers = offerService.getAllOffersForRestaurant(restaurantId);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @GetMapping("/availableOffers/restaurant/{restaurantId}")
    public ResponseEntity<List<Offer>> getAvailableOffersForRestaurant(@PathVariable Long restaurantId,
                                                              @RequestHeader("Authorization") String jwtToken) throws CustomException{
        List<Offer> offers = offerService.getAvailableOffersForRestaurant(restaurantId);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Offer>> getOffers(@RequestHeader("Authorization") String jwtToken) throws CustomException{
        List<Offer> offers = offerService.getAllOffers();
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }
}
