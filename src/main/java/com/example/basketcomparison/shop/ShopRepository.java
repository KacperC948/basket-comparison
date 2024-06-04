package com.example.basketcomparison.shop;

import com.example.basketcomparison.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Shop findShopById(long productId);

//    @Query("SELECT s FROM Shop s JOIN s.products p WHERE p.name = :productName")
//    List<Shop> findShopsByProductName(@Param("productName") String productName);

    List<Shop> findShopsByProducts(List<Product> products);

}