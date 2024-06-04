package com.example.basketcomparison.product;

import com.example.basketcomparison.shop.Shop;

import java.util.List;

public interface ProductService {
    List<Product> getAll();

    Product findProductById(long productId);

    List<Product> findAllByShop(Shop shop);

    List<Product> findAllByName(String name);
}