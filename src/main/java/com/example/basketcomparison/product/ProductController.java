package com.example.basketcomparison.product;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<List<ProductDto>> getAllProducts () {
        List<Product> products = productService.getAll();
        return ResponseEntity.ok(products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
    }

    private ProductDto convertToDto(Product product) {
        return ProductDto.builder()
                .productId(product.getId())
                .price(product.getPrice())
                .shopId(product.getShop().getId())
                .productTypeId(product.getProductType().getId())
                .name(product.getName())
                .build();
    }
}
