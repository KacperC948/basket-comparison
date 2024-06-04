package com.example.basketcomparison.shop;

import com.example.basketcomparison.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopDto {
    private long shopId;
    private String name;
    private List<ProductDto> products;
}