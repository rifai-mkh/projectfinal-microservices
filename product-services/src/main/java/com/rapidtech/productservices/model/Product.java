package com.rapidtech.productservices.model;

import lombok.*;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productCode;
    private BigDecimal price;
    private Double quantity;
}
