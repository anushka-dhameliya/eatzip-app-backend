package com.eatzip.service.cart;


import com.eatzip.exception.CustomException;
import com.eatzip.model.AddOnItem;
import com.eatzip.model.Cart;
import com.eatzip.model.CartItem;
import com.eatzip.model.User;
import com.eatzip.repository.CartItemRepository;
import com.eatzip.repository.CartRepository;
import com.eatzip.request.AddCartItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;


    @Override
    public CartItem addItemToCart(AddCartItemRequest request, User user) throws CustomException {
        try {
            double totalAmount = request.getMenuItem().getPrice() * request.getQuantity();
            if(!request.getAddOnItems().isEmpty()){
                for(AddOnItem item : request.getAddOnItems()){
                    totalAmount += item.getPrice();
                }
            }
            CartItem cartItem = CartItem.builder()
                    .menuItem(request.getMenuItem())
                    .addOnItems(request.getAddOnItems())
                    .totalAmount(totalAmount)
                    .quantity(request.getQuantity())
                    .build();


            Cart cart = user.getCart();
            cartItem.setCart(cart);

            boolean isPresent = !cart.getCartItems().isEmpty() && cart.getCartItems().stream().anyMatch(i -> i.getMenuItem().getId() == cartItem.getMenuItem().getId());
            if(isPresent){
                Optional<CartItem> cartItemDB = cart.getCartItems().stream().filter(i -> i.getMenuItem().getId() == cartItem.getMenuItem().getId()).findFirst();
                if(cartItemDB.isPresent()){
                    cartItemDB.get().setQuantity(cartItem.getQuantity() + cartItem.getQuantity());
                    CartItem savedCartItem = cartItemRepository.save(cartItemDB.get());
                    return savedCartItem;
                }
                return new CartItem();
            }
            else{
                CartItem savedCartItem = cartItemRepository.save(cartItem);
                cart.getCartItems().add(savedCartItem);
                cart.setTotalItems(cart.getTotalItems() + 1);
                cartRepository.save(cart);
                return savedCartItem;
            }

        }
        catch (Exception e){
            e.printStackTrace();
            throw new CustomException("Error while adding new cart item in cart for user with id = " + user.getId());
        }
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws CustomException {
        CartItem cartItemDB = findCartItemById(cartItemId);
        cartItemDB.setQuantity(quantity);
        double singleItemPrice = cartItemDB.getMenuItem().getPrice();
        if(!cartItemDB.getAddOnItems().isEmpty()){
            for(AddOnItem item : cartItemDB.getAddOnItems()){
                singleItemPrice += item.getPrice();
            }
        }
        cartItemDB.setTotalAmount(singleItemPrice * quantity);
        cartItemDB = cartItemRepository.save(cartItemDB);
        return cartItemDB;
    }

    @Override
    public Cart removeCartItemFromCart(Long cartItemId, User user) throws CustomException {
        CartItem cartItemDB = findCartItemById(cartItemId);
        Cart cart = user.getCart();
        cart.getCartItems().remove(cartItemDB);
        cartItemRepository.deleteById(cartItemId);
        cart.setTotalItems(cart.getTotalItems() - 1);
        cartRepository.save(cart);
        return cart;
    }

    public CartItem findCartItemById(Long cartItemId) throws CustomException{
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if(cartItem.isEmpty()){
            throw new CustomException("Cart Item not present with id = " + cartItemId);
        }
        return cartItem.get();
    }

    @Override
    public Long calculateCartTotal(Cart cart) throws CustomException {
        long totalAmount = 0L;
        for(CartItem cartItem : cart.getCartItems()){
            totalAmount += (long) (cartItem.getTotalAmount());
        }
        return totalAmount;
    }

    @Override
    public Cart findCartById(Long id) throws CustomException {
        Optional<Cart> cart = cartRepository.findById(id);
        if(cart.isEmpty()){
            throw new CustomException("Cart not present with id = " + id);
        }
        return cart.get();
    }

    @Override
    public Cart findCartByUserId(Long userId) throws CustomException {
        return cartRepository.findByCustomer_Id(userId);
    }

    @Override
    public void clearCart(Long userId) throws CustomException {
        Cart cart = cartRepository.findByCustomer_Id(userId);
        List<CartItem> cartItems = cart.getCartItems();
        cart.getCartItems().clear();

        for(CartItem cartItem : cartItems){
            cartItemRepository.deleteById(cartItem.getId());
        }
        cart.setTotalItems(0);
        cartRepository.save(cart);
    }
}
