package com.eatzip.request;

import com.eatzip.model.OfferDeductionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CreateOfferRequest {

    private String name;

    private String description;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    private OfferDeductionType type;

    private double percentage;

    private double amount;

    private Long restaurantId;
}
