package com.example.basketcomparison.producttype;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
    ProductType findProductTypeById(long productTypeId);
}
