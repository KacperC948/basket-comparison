package com.example.basketcomparison.product;

import com.example.basketcomparison.shop.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findProductById(long productId) {
        return productRepository.findProductById(productId);
    }

    @Override
    public List<Product> findAllByShop(Shop shop) {
        return productRepository.findAllByShop(shop);
    }

    @Override
    public List<Product> findAllByName(String name) {
        return productRepository.findAllByName(name);
    }

}