package com.example.basketcomparison.shop;

import com.example.basketcomparison.product.Product;

import java.util.List;

public interface ShopService {
    List<Shop> getAll();

    Shop findShopById(long shopId);

//    List<Shop> findShopsByProductName(String productName);

    List<Shop> findShopsByProducts(List<Product> products);
}