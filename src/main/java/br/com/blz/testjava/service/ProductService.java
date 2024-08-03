package br.com.blz.testjava.service;

import br.com.blz.testjava.model.Product;
import br.com.blz.testjava.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Optional<Product> getProductBySku(Long sku) {
        return productRepository.getProductBySku(sku);
    }

    public Product createProduct(Product Product) {
        return productRepository.createProduct(Product);
    }

    public void deleteProductBySku(Long sku) {
        productRepository.deleteProductBySku(sku);
    }

    public Product updateProduct(Product product) {
        return productRepository.updateProduct(product);
    }
}
