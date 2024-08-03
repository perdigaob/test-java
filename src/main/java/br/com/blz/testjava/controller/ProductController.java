package br.com.blz.testjava.controller;

import br.com.blz.testjava.exception.ProductNotFoundException;
import br.com.blz.testjava.model.Product;
import br.com.blz.testjava.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{sku}")
    public ResponseEntity<Product> getProduct(@PathVariable Long sku) throws ProductNotFoundException {
        return ResponseEntity.ok(productService.getProductBySku(sku));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    @PutMapping("/{sku}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long sku, @RequestBody Product product) throws ProductNotFoundException {
        if (!sku.equals(product.getSku())) {
            throw new InvalidParameterException("SKUs from path and request body don't match.");
        }
        return ResponseEntity.ok(productService.updateProduct(sku, product));
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long sku) throws ProductNotFoundException {
        productService.deleteProductBySku(sku);
        return ResponseEntity.noContent().build();
    }

}
