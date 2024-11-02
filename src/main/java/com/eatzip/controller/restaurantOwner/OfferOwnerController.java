package com.eatzip.controller.restaurantOwner;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Offer;
import com.eatzip.request.CreateOfferRequest;
import com.eatzip.response.MessageResponse;
import com.eatzip.service.offer.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/offer")
public class OfferOwnerController {

    @Autowired
    private OfferService offerService;

    @PostMapping
    public ResponseEntity<Offer> createOffer(@RequestBody CreateOfferRequest offerRequest,
                                             @RequestHeader("Authorization") String jwtToken) throws CustomException {
        Offer savedOffer = offerService.createOfferForRestaurant(offerRequest);
        return new ResponseEntity<>(savedOffer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Offer> updateOffer(@PathVariable Long id,
                                             @RequestBody CreateOfferRequest offerRequest,
                                             @RequestHeader("Authorization") String jwtToken) throws CustomException{
        Offer updatedOffer = offerService.updateOffer(id, offerRequest);
        return new ResponseEntity<>(updatedOffer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteOffer(@PathVariable Long id,
                                                       @RequestHeader("Authorization") String jwtToken) throws CustomException{
        offerService.deleteOffer(id);
        MessageResponse response = MessageResponse.builder()
                .message("Offer deleted success").build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offer> getOfferById(@PathVariable Long id,
                                                       @RequestHeader("Authorization") String jwtToken) throws CustomException{
        Offer offer = offerService.getOfferById(id);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Offer>> getOffersForRestaurant(@PathVariable Long restaurantId,
                                              @RequestHeader("Authorization") String jwtToken) throws CustomException {
        List<Offer> offers = offerService.getAllOffersForRestaurant(restaurantId);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

}
