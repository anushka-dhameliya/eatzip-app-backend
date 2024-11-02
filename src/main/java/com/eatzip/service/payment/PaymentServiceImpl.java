package com.eatzip.service.payment;

import com.eatzip.model.Order;
import com.eatzip.response.PaymentReceiptResponse;
import com.eatzip.response.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.ChargeSearchResult;
import com.stripe.model.checkout.Session;
import com.stripe.param.ChargeSearchParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Value("${strip.apiKey}")
    private String stripPaymentSecretKey;

    @Override
    public PaymentResponse createPaymentRequest(Order order) throws Exception {


            Stripe.apiKey = stripPaymentSecretKey;

            SessionCreateParams params = SessionCreateParams.builder()
                    .setCustomerEmail(order.getCustomer().getEmail())
                    .putMetadata("order_id", "#ORDER_ID_" + order.getId())
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:3000/user/payment/success/" + order.getId())
                    .setCancelUrl("http://localhost:3000/user/payment/error")
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("usd")
                                    .setUnitAmount((long) order.getTotalAmount() * 100)
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName("EatZip").build())
                                    .build())
                            .build())
                    .setPaymentIntentData(SessionCreateParams.PaymentIntentData.builder().setReceiptEmail("anushkadhameliya1998@gmail.com").build())
                    .setPaymentIntentData(SessionCreateParams.PaymentIntentData.builder()
                            .putMetadata("order_id", "#ORDER_ID_" + order.getId())
                            .build())
                    .build();

            Session session = Session.create(params);

            PaymentResponse response = new PaymentResponse();
            response.setPayment_url(session.getUrl());

            return response;
    }

    @Override
    public PaymentReceiptResponse getReceiptUrl(Long orderId) throws Exception {

        Stripe.apiKey = stripPaymentSecretKey;

        ChargeSearchParams params =
                ChargeSearchParams.builder()
                        .setQuery("metadata['order_id']:'#ORDER_ID_" + orderId + "'")
                        .build();

        ChargeSearchResult charges = Charge.search(params);

        PaymentReceiptResponse paymentReceiptResponse = new PaymentReceiptResponse();

        if(!charges.getData().isEmpty()){
            paymentReceiptResponse.setPayment_receipt_url(charges.getData().get(0).getReceiptUrl());
        }

        return paymentReceiptResponse;
    }
}
