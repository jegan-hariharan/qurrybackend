package com.quarry.management.service;

import com.quarry.management.DTO.AddProductReq;
import com.quarry.management.DTO.LoadResponseDTO;
import com.quarry.management.entity.Product;
import com.quarry.management.entity.Quarry;
import com.quarry.management.entity.Role;
import com.quarry.management.entity.UnitCost;
import com.quarry.management.exception.InternalServerErrorException;
import com.quarry.management.exception.RecordNotFoundException;
import com.quarry.management.repository.ProductRepository;
import com.quarry.management.repository.QuarryRespository;
import com.quarry.management.repository.UnitCostRespository;
import com.quarry.management.utils.ResponseMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductService productService;

    @Autowired
    private QuarryRespository quarryRespository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UnitCostRespository unitCostRespository;
    @Override
    public ResponseEntity<Product> addProduct(Long quarryId, AddProductReq addProductReq) {
        try {
            Product product = new Product();
            product.setProductName(addProductReq.getProductName());
            product.setCreatedDate(new Timestamp(new Date().getTime()));
            product.setIsActive(true);
            product.setQuarry(findQuarryById(quarryId));
            product = productRepository.save(product);

            UnitCost unitCost = new UnitCost();
            unitCost.setUnit(1F);
            unitCost.setUnitCost(addProductReq.getUnitCost());
            unitCost.setCreatedDate(new Timestamp(new Date().getTime()));
            unitCost.setQuarry(findQuarryById(quarryId));
            unitCost.setProduct(product);
            unitCost = unitCostRespository.save(unitCost);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @Override
    public List<Product> getAllProduct(Long quarryId) {
        return productRepository.findAllByQuarry(findQuarryById(quarryId));
    }

    @Override
    public Product updateProductById(Long productId, Product product) {
        Product productDB = productRepository.findById(productId).get();
        try {
            productDB.setProductName(product.getProductName());
            return productRepository.save(productDB);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @Override
    public Product updateProductStatusById(Long productId, Boolean isActive) {
        Product productDB = productRepository.findById(productId).get();
        try {
            productDB.setIsActive(isActive);
            return productRepository.save(productDB);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @Override
    public List<Product> getAllActiveProduct(Long quarryId) {
        return productRepository.findAllByQuarryAndIsActive(findQuarryById(quarryId), true);
    }

    private Quarry findQuarryById(Long quarryId) {
        Optional<Quarry> quarry = quarryRespository.findById(quarryId);
        if (!quarry.isPresent()) {
            throw new RecordNotFoundException(ResponseMessages.MINERALS_NOT_FOUND);
        }
        return quarry.get();
    }
}
