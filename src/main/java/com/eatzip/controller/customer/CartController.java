package com.eatzip.controller.customer;


import com.eatzip.exception.CustomException;
import com.eatzip.model.Cart;
import com.eatzip.model.CartItem;
import com.eatzip.model.User;
import com.eatzip.request.AddCartItemRequest;
import com.eatzip.response.MessageResponse;
import com.eatzip.service.cart.CartService;
import com.eatzip.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @PutMapping("/cart-item/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddCartItemRequest request,
                                                  @RequestHeader("Authorization") String jwt) throws CustomException {
        User user = userService.findUserByJwtToken(jwt);
        CartItem savedCartItem = cartService.addItemToCart(request, user);
        return new ResponseEntity<>(savedCartItem, HttpStatus.CREATED);
    }

    @PutMapping("/cart-item/update/{id}")
    public ResponseEntity<CartItem> updateCartItemQuantity(@PathVariable Long id,
                                                           @RequestParam(name = "quantity") int quantity,
                                                           @RequestHeader("Authorization") String jwt) throws CustomException{
        CartItem updateCartItem = cartService.updateCartItemQuantity(id, quantity);
        return new ResponseEntity<>(updateCartItem, HttpStatus.OK);
    }

    @DeleteMapping("/cart-item/remove/{id}")
    public ResponseEntity<Cart> removeCartItemFromCart(@PathVariable Long id,
                                                       @RequestHeader("Authorization") String jwt) throws CustomException{
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.removeCartItemFromCart(id, user);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping("/total/{cartId}")
    public ResponseEntity<String> calculateCartTotal(@PathVariable Long cartId,
                                             @RequestHeader("Authorization") String jwt) throws CustomException{
        User user = userService.findUserByJwtToken(jwt);
        Long cartTotal = cartService.calculateCartTotal(user.getCart());
        return new ResponseEntity<>("cartTotal = " + cartTotal, HttpStatus.OK);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> findCartById(@PathVariable Long cartId,
                                             @RequestHeader("Authorization") String jwt) throws CustomException{
        Cart cart = cartService.findCartById(cartId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Cart> findCartByUserId(@PathVariable Long userId,
                                                 @RequestHeader("Authorization") String jwt) throws CustomException{
        Cart cart = cartService.findCartByUserId(userId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/clear/{userId}")
    public ResponseEntity<MessageResponse> clearCart(@PathVariable Long userId,
                                                     @RequestHeader("Authorization") String jwt) throws CustomException {
        cartService.clearCart(userId);
        MessageResponse response = MessageResponse.builder()
                .message("Cart cleared success").build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
