package com.rapidtech.productservices.controller;

import com.rapidtech.productservices.dto.ProductReq;
import com.rapidtech.productservices.dto.ProductRes;
import com.rapidtech.productservices.dto.ProductStockRes;
import com.rapidtech.productservices.model.Product;
import com.rapidtech.productservices.service.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductControllers {
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductReq productReq){
        productService.createProduct(productReq);
    }

    @PutMapping("/addStock/{id}/addQuantity/{addQuantity}")
    public String addStock(@PathVariable("id") Long id, @PathVariable("addQuantity") Double addQuantity) {
        Product product = getById(id);
        return productService.addStock(product, addQuantity);
    }

    @GetMapping("/allproducts")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductRes> getAllProducts(){

        return productService.getAllProducts();
    }

    @GetMapping("/getbyId/{id}")
    public Product getById(@PathVariable("id") Long id) {
        return productService.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductStockRes> isInStock(@RequestParam List<String> productCode){
        return productService.isInStock(productCode);
    }
}
