package com.example.basketcomparison.shop;

import com.example.basketcomparison.product.Product;
import com.example.basketcomparison.product.ProductDto;
import com.example.basketcomparison.product.ProductService;
import com.example.basketcomparison.request.body.ProductInBasket;
import com.example.basketcomparison.response.body.CalcuatedValueOfBasket;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
public class ShopController {

    @Autowired
    ShopService shopService;

    @Autowired
    ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<List<ShopDto>> getAllShops() {
        List<Shop> shops = shopService.getAll();
        return ResponseEntity.ok(shops.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/calculateBasket")
    public ResponseEntity<List<CalcuatedValueOfBasket>> calculateMinimumPriceForBasket(@RequestBody List<ProductInBasket> productsInBasket) {
        List<CalcuatedValueOfBasket> calcuatedValueOfBasketList = new ArrayList<>();
        List<Shop> shopsWithProduct = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        for(ProductInBasket productInBasket : productsInBasket){
            products.addAll(productService.findAllByName(productInBasket.getProductName()));
        }
        List<Long> listWithDuplicates = new ArrayList<>();
        for(Product product : products){
            listWithDuplicates.add(product.getShop().getId());
        }
        List<Long> listWithoutDuplicates = new ArrayList<>(
                new HashSet<>(listWithDuplicates));

        for(Long id : listWithoutDuplicates){
            shopsWithProduct.add(shopService.findShopById(id));
        }

        for (Shop shop : shopsWithProduct) {
            CalcuatedValueOfBasket calcuatedValueOfBasket = new CalcuatedValueOfBasket();
            calcuatedValueOfBasket.setShopName(shop.getName());
            calcuatedValueOfBasket.setShopId(shop.getId());
            calcuatedValueOfBasket.setAmount(0);
            List<Product> productsForShop = products.stream()
                    .filter(product -> product.getShop().getId() == shop.getId())
                    .toList();
            List<ProductDto> productList = new ArrayList<>();
            for (ProductInBasket productInBasket : productsInBasket) {
                for (Product product : productsForShop) {
                    if (productInBasket.getProductName().equals(product.getName())) {
                        ProductDto productDto = convertToDto(product);
                        productDto.setAmountOfProducts(product.getPrice() * productInBasket.getQuantity());
                        productDto.setQuantity(productInBasket.getQuantity());
                        productList.add(productDto);
                    }
                }
            }
            if(productList.size() == productsInBasket.size()){
                calcuatedValueOfBasket.setProducts(productList);
                calcuatedValueOfBasket.setAmount(productList.stream().mapToDouble(ProductDto::getAmountOfProducts).sum());
                calcuatedValueOfBasketList.add(calcuatedValueOfBasket);
            }
        }

        if(calcuatedValueOfBasketList.isEmpty()){
            return ResponseEntity.ok(calcuatedValueOfBasketList);
        } else {
            return ResponseEntity.ok(calcuatedValueOfBasketList.stream()
                    .sorted(Comparator.comparingDouble(CalcuatedValueOfBasket::getAmount)).limit(10)
                    .collect(Collectors.toList()));
        }
    }

    private ShopDto convertToDto(Shop shop) {
        List<Product> products = productService.findAllByShop(shop);
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product product : products){
            productDtos.add(ProductDto.builder()
                .name(product.getName())
                .price(product.getPrice())
                .productId(product.getId())
                .shopId(product.getShop().getId())
                .productTypeId(product.getProductType().getId())
                .build());
        }

        return ShopDto.builder()
                .shopId(shop.getId())
                .name(shop.getName())
                .products(productDtos)
                .build();
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