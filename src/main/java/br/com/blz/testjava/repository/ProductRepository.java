package br.com.blz.testjava.repository;

import br.com.blz.testjava.exception.ProductExistsException;
import br.com.blz.testjava.exception.ProductNotFoundException;
import br.com.blz.testjava.model.Product;
import br.com.blz.testjava.model.Warehouse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Repository
public class ProductRepository {

    private final HashMap<Long, Product> productsMap = new HashMap<>();
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        try {
            List<Product> products = objectMapper.readValue(new File("src/main/resources/starterProductsList.json"), new TypeReference<List<Product>>() {
            });
            for (Product product : products) {
                productsMap.put(product.getSku(), product);
            }
        } catch (IOException ex) {
            System.out.println("Failed to load products. " + ex.getMessage());
        }
    }


    public Product getProductBySku(Long sku) throws ProductNotFoundException {
        Product product = productsMap.get(sku);
        if (product == null) {
            throw new ProductNotFoundException("Product not found with SKU: " + sku);
        }
        product.getInventory().setQuantity(calculateProductQuantity(product));
        product.setIsMarketable(isProductMarketable(product));
        return product;
    }

    public Product createProduct(Product product) throws ProductExistsException, ProductNotFoundException {
        if (productsMap.containsKey(product.getSku())) {
            throw new ProductExistsException(String.format("Product with SKU: %d already exists.", product.getSku()));
        }
        productsMap.put(product.getSku(), product);
        return getProductBySku(product.getSku());
    }

    public Product updateProduct(Long sku, Product product) throws ProductNotFoundException {
        if (!productsMap.containsKey(sku)) {
            throw new ProductNotFoundException("Product not found with SKU: " + product.getSku());
        }
        productsMap.put(sku, product);
        return getProductBySku(sku);
    }

    public void deleteProductBySku(Long sku) throws ProductNotFoundException {
        if (!productsMap.containsKey(sku)) {
            throw new ProductNotFoundException("Product not found with SKU: " + sku);
        }
        productsMap.remove(sku);
    }

    private Integer calculateProductQuantity(Product product) {
        return product.getInventory().getWarehouses().stream().mapToInt(Warehouse::getQuantity).sum();
    }

    private Boolean isProductMarketable(Product product) {
        return (calculateProductQuantity(product) > 0);
    }
}
