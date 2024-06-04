package com.example.basketcomparison.shop;

import com.example.basketcomparison.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    ShopRepository shopRepository;

    @Override
    public List<Shop> getAll() {
        return shopRepository.findAll();
    }

    @Override
    public Shop findShopById(long shopId) {
        return shopRepository.findShopById(shopId);
    }

//    @Override
//    public List<Shop> findShopsByProductName(String productName) {
//        return shopRepository.findShopsByProductName(productName);
//    }

    @Override
    public List<Shop> findShopsByProducts(List<Product> products) {
        return shopRepository.findShopsByProducts(products);
    }
}