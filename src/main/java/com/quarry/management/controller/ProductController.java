package com.quarry.management.controller;

import com.quarry.management.DTO.AddProductReq;
import com.quarry.management.DTO.LoadResponseDTO;
import com.quarry.management.entity.Product;
import com.quarry.management.entity.Quarry;
import com.quarry.management.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/addProduct/{quarryId}")
    public ResponseEntity<Product> addProduct(@PathVariable("quarryId") Long quarryId, @RequestBody AddProductReq addProductReq) {
        return productService.addProduct(quarryId, addProductReq);
    }

    @GetMapping("/admin/getAllProduct/{quarryId}")
    public List<Product> getAllProduct(@PathVariable("quarryId") Long quarryId) {
        return productService.getAllProduct(quarryId);
    }

    @GetMapping("/admin/getAllActiveProduct/{quarryId}")
    public List<Product> getAllActiveProduct(@PathVariable("quarryId") Long quarryId) {
        return productService.getAllActiveProduct(quarryId);
    }

    @PutMapping("/admin/editProduct/{productId}")
    public Product updateProductById(@PathVariable("productId") Long productId, @RequestBody Product product) {
        return productService.updateProductById(productId, product);
    }

    @GetMapping("/editProductStatus/{productId}/{isActive}")
    public Product updateProductStatusById(@PathVariable("productId") Long productId, @PathVariable("isActive") Boolean isActive) {
        return productService.updateProductStatusById(productId, isActive);
    }

}
