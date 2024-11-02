package com.eatzip.service.offer;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Cart;
import com.eatzip.model.Offer;
import com.eatzip.request.CreateOfferRequest;

import java.util.List;

public interface OfferService {

    public Offer createOfferForRestaurant(CreateOfferRequest request) throws CustomException;

    public Offer updateOffer(Long offerId, CreateOfferRequest request) throws CustomException;

    public void deleteOffer(Long offerId) throws CustomException;

    public Long applyOffer(Offer offer, Cart cart) throws CustomException;

    public Offer getOfferById(Long offerId) throws CustomException;

    public List<Offer> getAllOffers() throws CustomException;

    public List<Offer> getAllOffersForRestaurant(Long restaurantId) throws CustomException;

    public List<Offer> getAvailableOffersForRestaurant(Long restaurantId) throws CustomException;
}
