package com.rapidtech.productservices.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRes {
    private Long id;
    private String productCode;
    private BigDecimal price;
    private Double quantity;
}
