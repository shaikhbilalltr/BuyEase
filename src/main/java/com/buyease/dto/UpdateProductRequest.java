package com.buyease.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductRequest {

    private String name;

    private String description;

    private BigDecimal price;

    private Integer quantity;

    private String category;

    private Boolean isActive;
}
