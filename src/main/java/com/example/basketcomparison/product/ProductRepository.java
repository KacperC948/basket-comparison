package com.example.basketcomparison.product;

import com.example.basketcomparison.shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductById(long productId);

    List<Product> findAllByShop(Shop shop);

    List<Product> findAllByName(String name);
}
