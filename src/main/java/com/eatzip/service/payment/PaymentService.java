package com.eatzip.service.payment;


import com.eatzip.model.Order;
import com.eatzip.response.PaymentReceiptResponse;
import com.eatzip.response.PaymentResponse;

public interface PaymentService {

    public PaymentResponse createPaymentRequest(Order order) throws Exception;

    public PaymentReceiptResponse getReceiptUrl(Long orderId) throws Exception;
}
