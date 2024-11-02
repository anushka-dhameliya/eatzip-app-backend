package com.eatzip.service.offer;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Cart;
import com.eatzip.model.Offer;
import com.eatzip.model.OfferDeductionType;
import com.eatzip.model.Restaurant;
import com.eatzip.repository.OfferRepository;
import com.eatzip.request.CreateOfferRequest;
import com.eatzip.service.cart.CartService;
import com.eatzip.service.restaurant.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService{

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CartService cartService;

    @Override
    public Offer createOfferForRestaurant(CreateOfferRequest request) throws CustomException {
        try{
            Restaurant restaurant = restaurantService.findRestaurantById(request.getRestaurantId());
            Offer offer = Offer.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .type(request.getType())
                    .amount(request.getAmount())
                    .percentage(request.getPercentage())
                    .restaurant(restaurant)
                    .fromDate(request.getFromDate())
                    .toDate(request.getToDate())
                    .build();

            Offer savedOffer = offerRepository.save(offer);
            restaurant.getOffers().add(savedOffer);
            return savedOffer;
        }
        catch (Exception e){
            throw new CustomException("Error while creating offer...");
        }
    }

    @Override
    public Offer updateOffer(Long offerId, CreateOfferRequest request) throws CustomException {
        Offer offerDB = getOfferById(offerId);
        if(request.getName() != null){
            offerDB.setName(request.getName());
        }
        if(request.getDescription() != null){
            offerDB.setDescription(request.getDescription());
        }
        if(request.getPercentage() != offerDB.getPercentage()){
            offerDB.setPercentage(request.getPercentage());
        }
        if(request.getAmount() != offerDB.getAmount()){
            offerDB.setAmount(request.getAmount());
        }
        if(!request.getType().equals(offerDB.getType())){
            offerDB.setType(request.getType());
        }
        if(request.getToDate() != null){
            offerDB.setToDate(request.getToDate());
        }
        if(request.getFromDate() != null){
            offerDB.setFromDate(request.getFromDate());
        }

        Offer updatedOffer = offerRepository.save(offerDB);
        return updatedOffer;
    }

    @Override
    public void deleteOffer(Long offerId) throws CustomException {
        Offer offerDB = getOfferById(offerId);
        Restaurant restaurant = restaurantService.findRestaurantById(offerDB.getRestaurant().getId());
        restaurant.getOffers().remove(offerDB);
        offerRepository.deleteById(offerId);
    }

    @Override
    public Offer getOfferById(Long offerId) throws CustomException {
        Optional<Offer> offerDB = offerRepository.findById(offerId);
        if(offerDB.isEmpty()){
            throw new CustomException("Offer not found with id = " + offerId);
        }
        return offerDB.get();
    }

    @Override
    public List<Offer> getAllOffers() throws CustomException {
        return offerRepository.findAll();
    }

    @Override
    public List<Offer> getAllOffersForRestaurant(Long restaurantId) throws CustomException {
        return offerRepository.findByRestaurant_Id(restaurantId);
    }

    @Override
    public List<Offer> getAvailableOffersForRestaurant(Long restaurantId) throws CustomException{
        LocalDateTime previousDate = LocalDateTime.now().minusDays(1);
        List<Offer> allOffers = getAllOffersForRestaurant(restaurantId);
        List<Offer> validOffers = allOffers.stream().filter(i -> !i.getToDate().isBefore(previousDate)).toList();
        return validOffers;
    }

    public Long applyOffer(Offer offer, Cart cart) throws CustomException{

        Long totalAmount = cartService.calculateCartTotal(cart);
        if(offer.getType().equals(OfferDeductionType.AMOUNT)){
            totalAmount -= (long) offer.getAmount();
        }
        else if(offer.getType().equals(OfferDeductionType.PERCENTAGE)){
            totalAmount -= (long)(totalAmount * offer.getPercentage() / 100);
        }

        return totalAmount;
    }
}
