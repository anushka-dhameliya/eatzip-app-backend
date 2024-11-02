package com.eatzip.service.notification;

public enum NotificationMessage {

    USER_CREATION("Welcome to Eatzip application! Your account is ready. Start browsing and order delicious food now!"),
    RESTAURANT_CREATION("Congratulations! Your restaurant [RESTAURANT_NAME] is now live on Eatzip. Complete your restaurant profile to attract more customers! Log in and add details like your menu, hours, and delivery zones."),
    ORDER_CONFIRMATION("Thank you! Your order [#ORDER_ID] has been successfully placed. Get ready to enjoy your meal soon!"),
    ORDER_STATUS_CHANGE("Your order #[ORDER_ID] has been updated by the restaurant. Check the updated details in your order summary."),
    ORDER_RESTAURANT_CONFIRMATION("New order [#ORDER_ID] from [CUSTOMER_NAME] has been placed. Please begin preparation"),
    ORDER_DISPATCHED("Good news! Your order is completed by the restaurant and is on the way."),
    ORDER_DELIVERED("Your order has been delivered! We hope you enjoy your meal."),
    ORDER_RESTAURANT_DELIVERED("Order #[ORDER_ID] has been delivered to [CUSTOMER_NAME]. Thanks for your hard work!"),
    ORDER_CANCELED("Your order #[ORDER_ID] has been canceled. Refund processed. Check email for details."),
    ORDER_SYSTEM_CANCELED("Unfortunately, your order [#ORDER_ID] has been canceled."),
    ORDER_RESTAURANT_OWNER_CANCELED("Order #[ORDER_ID] has been canceled by the customer. No further preparation required."),
    REFUND_PROCESSED("Your refund for order [#ORDER_ID] has been processed. It may take 3-5 business days to reflect in your account."),
    FAILED_PAYMENT("Oops! Your payment for order [#ORDER_ID] was unsuccessful. Please try again or use a different payment method.")
    ;

    public final String message;

    NotificationMessage(String message) {
        this.message = message;
    }
}
