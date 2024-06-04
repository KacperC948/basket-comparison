package com.example.basketcomparison.producttype;

import java.util.List;

public interface ProductTypeService {
    List<ProductType> getAll();

    ProductType findProductTypeById(long productTypeId);
}