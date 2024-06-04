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
    public ResponseEntity<CalcuatedValueOfBasket> calculateMinimumPriceForBasket(@RequestBody List<ProductInBasket> productsInBasket) {
        CalcuatedValueOfBasket calcuatedValueOfBasket = new CalcuatedValueOfBasket();
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

        Map<String, List<Product>> productsGroupedByNameAndSortedByPrice = products.stream()
                .collect(Collectors.groupingBy(Product::getName,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    list.sort(Comparator.comparing(Product::getPrice));
                                    return list;
                                }
                        )
                ));
        double amountOfWholeBasket = 0;
        List<ProductDto> productDtos = new ArrayList<>();
        for(int i = 0; i < productsInBasket.size(); i++){
            double priceOfProductsMultipleByQuantity = productsGroupedByNameAndSortedByPrice
                    .get(productsInBasket.get(i).getProductName())
                    .get(0).getPrice() * productsInBasket.get(i).getQuantity();
            amountOfWholeBasket += priceOfProductsMultipleByQuantity;
            Product product = productsGroupedByNameAndSortedByPrice
                    .get(productsInBasket.get(i).getProductName())
                    .get(0);
            ProductDto productDto = convertToDto(product);
            productDto.setAmountOfProducts(priceOfProductsMultipleByQuantity);
            productDtos.add(productDto);
        }

        calcuatedValueOfBasket.setAmount(amountOfWholeBasket);
        calcuatedValueOfBasket.setShopId(productsGroupedByNameAndSortedByPrice
                .get(productsInBasket.get(0).getProductName())
                .get(0).getShop().getId());
        calcuatedValueOfBasket.setShopName(productsGroupedByNameAndSortedByPrice
                .get(productsInBasket.get(0).getProductName())
                .get(0).getShop().getName());
        calcuatedValueOfBasket.setProducts(productDtos);

        shopsWithProduct.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(calcuatedValueOfBasket);
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