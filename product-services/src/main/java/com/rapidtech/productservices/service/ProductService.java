package com.rapidtech.productservices.service;

import com.rapidtech.productservices.dto.ProductReq;
import com.rapidtech.productservices.dto.ProductRes;
import com.rapidtech.productservices.dto.ProductStockRes;
import com.rapidtech.productservices.model.Product;
import com.rapidtech.productservices.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepo productRepo;

    public void createProduct(ProductReq productReq){
        Product product = Product.builder()
                .productCode(productReq.getProductCode())
                .price(productReq.getPrice())
                .quantity(productReq.getQuantity()).build();
        productRepo.save(product);
        log.info("Product {} is saved",product.getId());
    }

    public List<ProductRes> getAllProducts(){
        List<ProductRes> productResList = new ArrayList<>();
        List<Product> productList = productRepo.findAll();
        for(Product product : productList){
            productResList.add(ProductRes.builder()
                    .id(product.getId())
                    .productCode(product.getProductCode())
                    .price(product.getPrice())
                    .quantity(product.getQuantity()).build());
        }
        return productResList;

        //return products.stream().map(this::mapToProductRes).toList();
    }


    /*private ProductRes mapToProductRes(Product product){
        return ProductRes.builder()
                .id(product.getId())
                .productCode(product.getProductCode())
                .price(product.getPrice())
                .quantity(product.getQuantity()).build();
    }

     */


    public List<ProductStockRes> isInStock(List<String> productCode){
        return productRepo.findByProductCodeIn(productCode).stream()
                .map(r ->
                        ProductStockRes.builder()
                                .productCode(r.getProductCode())
                                .price(r.getPrice())
                                .isInStock(r.getQuantity()>0)
                                .build()).toList();
    }
}
