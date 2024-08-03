package br.com.blz.testjava.controller;

import br.com.blz.testjava.model.Product;
import br.com.blz.testjava.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{sku}")
    public ResponseEntity<Product> getProduct(@PathVariable Long sku) {
        Optional<Product> product = productService.getProductBySku(sku);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{sku}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long sku, @RequestBody Product product) {
        if (!productService.getProductBySku(sku).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        product.setSku(sku);
        return ResponseEntity.ok(productService.updateProduct(product));
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long sku) {
        if (!productService.getProductBySku(sku).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteProductBySku(sku);
        return ResponseEntity.noContent().build();
    }


}
