package com.example.basketcomparison.response.body;

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
public class CalcuatedValueOfBasket {
    long shopId;
    String shopName;
    double amount;
    List<ProductDto> products;
}