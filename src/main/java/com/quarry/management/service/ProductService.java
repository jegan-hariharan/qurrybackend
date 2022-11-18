package com.quarry.management.service;

import com.quarry.management.DTO.AddProductReq;
import com.quarry.management.DTO.LoadResponseDTO;
import com.quarry.management.entity.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<Product> addProduct(Long quarryId, AddProductReq addProductReq);

    List<Product> getAllProduct(Long quarryId);

    Product updateProductById(Long productId, Product product);

    Product updateProductStatusById(Long productId, Boolean isActive);

    List<Product> getAllActiveProduct(Long quarryId);
}
