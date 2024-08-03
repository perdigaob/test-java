package br.com.blz.testjava.service;

import br.com.blz.testjava.exception.ProductNotFoundException;
import br.com.blz.testjava.model.Product;
import br.com.blz.testjava.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product getProductBySku(Long sku) throws ProductNotFoundException {
        return productRepository.getProductBySku(sku);
    }

    public Product createProduct(Product Product) throws Exception {
        return productRepository.createProduct(Product);
    }

    public Product updateProduct(Long sku, Product product) throws ProductNotFoundException {
        return productRepository.updateProduct(sku, product);
    }

    public void deleteProductBySku(Long sku) throws ProductNotFoundException {
        productRepository.deleteProductBySku(sku);
    }

}
