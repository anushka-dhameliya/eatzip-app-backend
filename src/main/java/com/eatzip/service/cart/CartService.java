package com.eatzip.service.cart;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Cart;
import com.eatzip.model.CartItem;
import com.eatzip.model.User;
import com.eatzip.request.AddCartItemRequest;

public interface CartService {

    public CartItem addItemToCart(AddCartItemRequest request, User user) throws CustomException;

    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws CustomException;

    public Cart removeCartItemFromCart(Long cartItemId, User user) throws CustomException;

    public Long calculateCartTotal(Cart cart) throws CustomException;

    public Cart findCartById(Long id) throws CustomException;

    public CartItem findCartItemById(Long cartItemId) throws CustomException;

    public Cart findCartByUserId(Long userId) throws CustomException;

    public void clearCart(Long userId) throws CustomException;
}
