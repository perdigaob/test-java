package br.com.blz.testjava.controller;

import br.com.blz.testjava.model.Product;
import br.com.blz.testjava.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{sku}")
    public ResponseEntity<Product> getProduct(@PathVariable Long sku) {
        return ResponseEntity.ok(productService.getProductBySku(sku));
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) throws Exception {
        return productService.createProduct(product);
    }

    @PutMapping("/{sku}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long sku, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(product));
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long sku) {
        productService.deleteProductBySku(sku);
        return ResponseEntity.noContent().build();
    }


}
